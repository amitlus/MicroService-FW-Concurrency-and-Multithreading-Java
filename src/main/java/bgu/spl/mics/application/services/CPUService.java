package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;

/**
 * CPU service is responsible for handling the {@link //DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    CPU cpu;
    public CPUService(String name, CPU cpu) {
        super("Change_This_Name");
        this.cpu = cpu;
        // TODO Implement this
    }

    //ONLY NEED TO UPDATE THE TIME FOR THE CPU
    @Override
    protected void initialize() {
        //SUBSCRIBE TO TICK BROADCAST
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{cpu.updateTick();
        if(cpu.isCounting())
            cpu.processCurrentBatch();
        //AFTER WE FINISH TO PROCESS 1 BATCH, TRY TO TAKE ANOTHER BATCH WITHOUT WAITING FOR ANOTHER TICK
        if(!cpu.isCounting())
            cpu.getUnprocessedDataBatch();
        });

        //SUBSCRIBE TO TERMINATE BROADCAST
        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast)->{terminate();});

    }
}
