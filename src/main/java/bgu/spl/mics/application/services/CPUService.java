package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CPU service is responsible for handling the {@link //DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    CPU cpu;
    DataBatch currentBatch;
    public static int finish = 0;
    public CPUService(String name, CPU cpu) {
        super(name);
        this.cpu = cpu;
        this.currentBatch = currentBatch;
    }

    //ONLY NEED TO UPDATE THE TIME FOR THE CPU
    @Override
    protected void initialize() {
        //SUBSCRIBE TO TICK BROADCAST
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{cpu.updateTick();
            if(TickBroadcast.isFinish()) {
                terminate();
            }
        if(cpu.isCounting())
            processCurrentBatch();
        //AFTER WE FINISH TO PROCESS 1 BATCH, TRY TO TAKE ANOTHER BATCH WITHOUT WAITING FOR ANOTHER TICK
        if(!cpu.isCounting())
            getUnprocessedDataBatch();
        });
    }

    public void getUnprocessedDataBatch() throws InterruptedException {

        try {
            currentBatch = Cluster.getDataToProcessList().take();

        } catch (InterruptedException e) {
        }

//        System.out.println("CPU received DB from " +currentBatch.getSource().getType());
//        System.out.println("CLUSTER'S dataToProcessList SIZE is "+cluster.getDataToProcessList().size());

        if (currentBatch.getData() == null)
            terminate();

        else {
            Data.Type dataType = currentBatch.getData().getType();
            if (dataType == Data.Type.Images)
                cpu.setProcessTime((32 / cpu.getCores()) * 4);
            else if (dataType == Data.Type.Text)
                cpu.setProcessTime((32 / cpu.getCores()) * 2);
            else
                cpu.setProcessTime((32 / cpu.getCores()));
            cpu.setCounting(true);
        }
    }

    public void sendDataBatch() throws InterruptedException {
//            System.out.println("CPU sent PROCESSED db "+currentBatch.getSource().getModel().getName());
        Cluster.getInstance().sendProcessedDataBatch(currentBatch.getSource(), currentBatch);
    }

    public void processCurrentBatch() throws InterruptedException {
        decreaseProcessTime();
        ((AtomicInteger)Cluster.Statistics[2]).addAndGet(1);
        cpu.setCPUTimeUnits(cpu.getCPUTimeUnits()+1);
        if (cpu.getProcessTime() == 0) {
            sendDataBatch();
            cpu.setCounting(false);
        }
    }

    public void decreaseProcessTime(){
        cpu.setProcessTime(cpu.getProcessTime()-1);
    }

}
