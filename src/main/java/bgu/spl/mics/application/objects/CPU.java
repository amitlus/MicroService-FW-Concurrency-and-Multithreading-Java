package bgu.spl.mics.application.objects;
import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cores;
    private Cluster cluster = Cluster.getInstance();
    private boolean isCounting = false;
    private int processTime;
    private int currentTick;

    public CPU (int cores, Cluster cluster){
        this.cores = cores;
        this.cluster = cluster;
    }

    public void setCounting(boolean counting) {
        isCounting = counting;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public boolean isCounting() {
        return isCounting;
    }

    public int getCores (){
        return cores;
    }

    public void updateTick() {
        currentTick++;
    }


}
