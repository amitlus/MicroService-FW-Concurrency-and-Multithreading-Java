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
    private Cluster cluster = Cluster.getInstance();;
    private DataBatch currentBatch;
    int tickTime;

    public CPU (int cores, Cluster cluster){
        this.cores = cores;
        this.cluster = cluster;
        this.data = data;
    }

    public void getUnprocessedDataBatch(){
        currentBatch = cluster.getUnprocessedDataBatch();
    }

    //WHEN FINISH PROCESSING DATA BATCH, IMMEDIATELY SENDS IT TO THE CLUSTER- THEN TO THE GPU
    public void sendDataBatch() {
            DataBatch dataBatch = currentBatch;
            cluster.sendProcessedDataBatch(dataBatch.getSource(), dataBatch);
        }

    public int getCores (){
        return cores;
    }

    public Collection<DataBatch> getData(){
        return data;
    }

    public void updateTick() {
    }
}
