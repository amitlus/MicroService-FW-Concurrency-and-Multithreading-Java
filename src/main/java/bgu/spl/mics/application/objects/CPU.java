package bgu.spl.mics.application.objects;
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

    public CPU (int cores, Cluster cluster){
        this.cores = cores;
        this.cluster = cluster;
        this.data = data;
    }

    public void getUnprocessedDataBatch() throws InterruptedException {
        if(cluster.getDataToProcessList().peek()==null)
            currentBatch = cluster.getDataToProcessList().take();
        else
            throw new IllegalArgumentException("No more batches to process");

        isCounting = true;

        Data.Type dataType = currentBatch.data.getType();
        if(dataType == Data.Type.Images)
            processTime = (32/cores)*4;
        else if(dataType == Data.Type.Text)
            processTime = (32/cores)*2;
        else
            processTime = (32/cores);
    }

    //WHEN FINISH PROCESSING DATA BATCH, IMMEDIATELY SENDS IT TO THE CLUSTER- THEN TO THE GPU
    public void sendDataBatch() {
            cluster.sendProcessedDataBatch(currentBatch.getSource(), currentBatch);
        }

    public void setCounting(boolean counting) {
        isCounting = counting;
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
        if (processTime == 0) {
            sendDataBatch();
            isCounting = false;
        }
    }

    public void terminate() {
    }
}
