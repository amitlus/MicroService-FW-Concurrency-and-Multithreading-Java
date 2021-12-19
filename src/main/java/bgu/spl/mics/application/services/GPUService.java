package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

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
        if(gpu.isCounting){
            if (gpu.getProcessedDataList().peek().getTrainingTimeLeft() > 0)
                gpu.trainDataBatch(gpu.getProcessedDataList().peek());
            if (gpu.getProcessedDataList().peek().getTrainingTimeLeft() == 0) {
                gpu.getTrainedDataList().add(gpu.getProcessedDataList().remove());
            }
            if(gpu.isCounting==false)
                msb.complete(trainEvent, gpu.getModel().getStatus());
        }

        else {
            Message e;
            while(!gpu.getFastMessages().isEmpty()) {
                e = gpu.getFastMessages().remove();
                Callback<Message> call = (Callback<Message>) this.getMsgToCalls().get(e.getClass());
                call.call(e);
            }
            if(!gpu.getTrainEvents().isEmpty()) {
                trainEvent = (Event<TrainModelEvent>) gpu.getTrainEvents().remove();
                Callback<Message> call = (Callback<Message>) this.getMsgToCalls().get(trainEvent.getClass());
                call.call(trainEvent);
            }
        }
        });


        //SUBSCRIBE TO TRAIN EVENTS
        subscribeEvent(TrainModelEvent.class, (TrainModelEvent)->

        { gpu.setModel(TrainModelEvent.model);
            gpu.getModel().setStatus(Model.Status.Training);
            gpu.setCounting(true);
            BlockingQueue<Message> queue = gpu.getMsb().getMicroToMsg().get(this);
            while(queue.peek().getClass()!=TickBroadcast.class){
                if(queue.peek().getClass() == TrainModelEvent.getClass())
                    gpu.getTrainEvents().add(queue.remove());
                else
                    gpu.getFastMessages().add(queue.remove());
            }
            gpu.makeDataList(); gpu.sendDataBatch();
        });

        //SUBSCRIBE TO TEST EVENTS
        subscribeEvent(TestModelEvent.class, (TestModelEvent)->{gpu.Test();
            msb.complete(TestModelEvent, gpu.getModel().getResult());

        });

        //SUBSCRIBE TO TERMINATE BROADCAST
        subscribeBroadcast(TerminateBroadcast .class, (TerminateBroadcast)->{terminate();});

    }
}
