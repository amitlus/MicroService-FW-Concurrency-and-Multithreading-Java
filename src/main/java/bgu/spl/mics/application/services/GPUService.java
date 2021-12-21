package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link "DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;
    private Event trainEvent;
    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
        trainEvent = null;
    }


    protected void initialize() throws InterruptedException {

        //SUBSCRIBE TO TICK BROADCAST
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{gpu.updateTick();
        if(TickBroadcast.isFinish() == true)
            terminate();
        if(gpu.isCounting) {
            if (!gpu.getProcessedDataList().isEmpty()) {
                DataBatch currentDB = gpu.getProcessedDataList().peek();
                if (currentDB.getTrainingTime() > 0) {
                    gpu.trainDataBatch(currentDB);
//                    System.out.println("Batch TRAINED. Send DB TO PROCESS");
                }

                //AFTER WE FINISH TRAINING A DATA BATCH, WE REMOVE IT FROM THE PROCESSED LIST AND ADD IT TO THE TRAINED LIST
                if (currentDB.getTrainingTime() == 0) {
                    gpu.getTrainedDataList().add(gpu.getProcessedDataList().remove());

                    //AFTER WE FINISH TRAINING A DATA BATCH, WE CHECK IF WE FINISHED TRAINING ALL THE MODEL'S DATA BATCHES
                    if (gpu.getTrainedDataList().size() == currentDB.getDataParts()) {
                        //UPDATE THE LIST OF TRAINED DATA IN THE CLUSTER'S STATISTICS
                        ArrayList<String> stat = (ArrayList<String>) Cluster.Statistics[0];
                        stat.add(gpu.getModel().getName());
                        Cluster.Statistics[0] = stat;

                        //CHANGING THE STATUS AND NOTIFY WE FINISHED COUNTING
                        gpu.getModel().setStatus(Model.Status.Trained);
                        System.out.println("MODEL " + gpu.getModel().getName() +" OF STUDENT "+gpu.getModel().getStudent().getName()+ " TRAINED by GPU "+gpu.getType());
                        gpu.getTrainedDataList().clear();
                        gpu.setCounting(false);
                    }
                }
                if(gpu.isCounting)
                     gpu.sendDataBatch();
            }
            if (gpu.isCounting == false)
                msb.complete(trainEvent, gpu.getModel().getStatus());

            else {
                gpu.sendDataBatch();
                Message e;
                while (!gpu.getFastMessages().isEmpty()) {
                    e = gpu.getFastMessages().remove();
                    Callback<Message> call = (Callback<Message>) this.getMsgToCalls().get(e.getClass());
                    call.call(e);
                }
            }
        }

        else {
            Message e = null;
            synchronized (gpu.getFastMessages()) {
                while (!gpu.getFastMessages().isEmpty()) {
                    e = gpu.getFastMessages().remove();
                    Callback<Message> call = (Callback<Message>) this.getMsgToCalls().get(e.getClass());
                    call.call(e);
                }
            }
            synchronized (gpu.getTrainEvents()) {
                if (!gpu.getTrainEvents().isEmpty()) {
                    e = (Event<TrainModelEvent>) gpu.getTrainEvents().remove();
                }
            }
            if (e != null) {
                Callback<Message> call = (Callback<Message>) this.getMsgToCalls().get(e.getClass());
                call.call(e);
            }
        }
        }
        );


        //SUBSCRIBE TO TRAIN EVENTS
        subscribeEvent(TrainModelEvent.class, (TrainModelEvent)-> {
            if(gpu.isCounting)
                gpu.getTrainEvents().add(TrainModelEvent);
            else{
                trainEvent = TrainModelEvent;
            gpu.setModel(TrainModelEvent.model);
            gpu.setCounting(true);
            BlockingQueue<Message> queue = gpu.getMsb().getMicroToMsg().get(this);
            try {
                while (queue.peek().getClass() != TickBroadcast.class) {
                    if (queue.peek().getClass() == TrainModelEvent.getClass())
                        gpu.getTrainEvents().add(queue.remove());
                    else
                        gpu.getFastMessages().add(queue.remove());
                }
            }
            catch(NullPointerException e){}

            gpu.makeDataList();
            gpu.sendDataBatch();
        }});

        //SUBSCRIBE TO TEST EVENTS
        subscribeEvent(TestModelEvent.class, (TestModelEvent)->{
            if(gpu.isCounting)
                gpu.getFastMessages().add(TestModelEvent);
            else{
                gpu.Test();
                System.out.println(gpu.getModel().getName()+" TESTED and the RESULT is "+gpu.getModel().getResult());
                msb.complete(TestModelEvent, gpu.getModel().getResult());

        }});


    }
}

