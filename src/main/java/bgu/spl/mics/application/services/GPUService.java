package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
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
        subscribeEvent(TrainModelEvent.class, (TrainModelEvent)->{gpu.makeDataList();
        gpu.sendDataBatches();});//Subscribe to TrainModelEvents
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{gpu.updateTick();});




        //THEN WE NEED TO TAKE ALL EVENTS FROM OUR QUEUE AND CALL THEIR CALLBACK
        // TODO Implement this

    }
}
