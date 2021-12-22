package bgu.spl.mics.application.objects;
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

    public void setCounting(boolean counting) {
        isCounting = counting;
    }

    public int getCPUTimeUnits() {
        return CPUTimeUnits;
    }

    public void setCPUTimeUnits(int CPUTimeUnits) {
        this.CPUTimeUnits = CPUTimeUnits;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public DataBatch getCurrentBatch() {
        return currentBatch;
    }

    public boolean isCounting() {
        return isCounting;
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


}
