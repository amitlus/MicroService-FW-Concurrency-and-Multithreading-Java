package bgu.spl.mics.application.objects;
import bgu.spl.mics.application.services.CPUService;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cores;
    private Collection<DataBatch> data;
    private Cluster cluster = Cluster.getInstance();
    private DataBatch currentBatch;
    private boolean isCounting = false;
    private int processTime;
    private int currentTick;
    int CPUTimeUnits;

    public CPU (int cores, Cluster cluster){
        this.cores = cores;
        this.cluster = cluster;
        this.data = data;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void getUnprocessedDataBatch() throws InterruptedException {

        try {
            currentBatch = cluster.getDataToProcessList().take();

        } catch (InterruptedException e) {}

//        System.out.println("CPU received DB from " +currentBatch.getSource().getType());
//        System.out.println("CLUSTER'S dataToProcessList SIZE is "+cluster.getDataToProcessList().size());

        if(currentBatch.getDataParts() == -1)
           return;

        Data.Type dataType = currentBatch.data.getType();
            if (dataType == Data.Type.Images)
                processTime = (32 / cores) * 4;
            else if (dataType == Data.Type.Text)
                processTime = (32 / cores) * 2;
            else
                processTime = (32 / cores);
            isCounting = true;
        }


    //WHEN FINISH PROCESSING DATA BATCH, IMMEDIATELY SENDS IT TO THE CLUSTER- THEN TO THE GPU
    public void sendDataBatch() throws InterruptedException {
//            System.out.println("CPU sent PROCESSED db "+currentBatch.getSource().getModel().getName());
            cluster.sendProcessedDataBatch(currentBatch.getSource(), currentBatch);
        }


    public boolean isCounting() {
        return isCounting;
    }

    public void decreaseProcessTime(){
        processTime--;
    }

    public int getCores (){
        return cores;
    }

    public Collection<DataBatch> getData(){
        return data;
    }

    public void updateTick() {
        currentTick++;
    }

    public void processCurrentBatch() throws InterruptedException {
        decreaseProcessTime();
        CPUTimeUnits++;
        if (processTime == 0) {
            sendDataBatch();
            isCounting = false;
        }
    }

    public void terminate() {
    }
}
