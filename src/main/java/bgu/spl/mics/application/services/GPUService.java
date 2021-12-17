package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    GPU gpu;
    public GPUService(String name, GPU gpu) {
        super("Change_This_Name");
        this.gpu = gpu;
    }


    protected void initialize() {

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{gpu.updateTick();
        if(gpu.isCounting){
            if (gpu.getProcessedDataList().peek().getTrainingTimeLeft() > 0)
                gpu.trainDataBatch(gpu.getProcessedDataList().peek());
            if (gpu.getProcessedDataList().peek().getTrainingTimeLeft() == 0) {
                gpu.getTrainedDataList().add(gpu.getProcessedDataList().peek());
            }
        }

        else {
            while(!gpu.getFastMessages().isEmpty()){
                gpu.getFastMessages().pop. //CALL IT'S "LAMBDA" EXPRESSION
        }
        }

        if(gpu.getModel().getStatus() == Model.Status.Trained) {

        }

        if(gpu.getModel().getStatus() == Model.Status.Tested) {

        }

        });

        subscribeBroadcast(TerminateBroadcast .class, (TerminateBroadcast)->{gpu.terminate();});

        subscribeEvent(TrainModelEvent.class, (TrainModelEvent)->
        {gpu.makeDataList(); gpu.sendDataBatch();});

        subscribeEvent(TestModelEvent.class, (TestModelEvent)->{gpu.Test();});



    }
}
