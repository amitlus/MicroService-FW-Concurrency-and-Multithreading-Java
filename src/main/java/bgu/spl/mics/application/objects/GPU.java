package bgu.spl.mics.application.objects;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;

import java.util.ArrayList;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}
    ArrayList<DataBatch> unprocessedDataList;
    ArrayList<DataBatch> processedDataList;
    ArrayList<DataBatch> TrainedDataList;


    private Type type;
    private Model model;
    public MessageBus msb = MessageBusImpl.getInstance();

    private Cluster cluster = Cluster.getInstance();
    private int vramSpace;


    public GPU (Type type, Cluster cluster){
        this.type = type;
        this.cluster = cluster;
        this.model = null;
        if(type == Type.RTX3090)
            vramSpace = 32;
        else if(type == Type.RTX2080)
            vramSpace = 16;
        else if(type == Type.GTX1080)
            vramSpace = 8;
    }

    public void updateTick() {
    }

    public void sendDataBatches() {
        DataBatch dataBatch = unprocessedDataList.get(0);
        cluster.


    }

    public void makeDataList(){
        Data data = model.getData();
        int dataSize = data.getSize();
        int index = 0;
        int check = dataSize%1000;
        int num;
        if(check!=0)
            num = dataSize/1000 +1;
        else
            num = dataSize/1000;

        for(int i=0; i<num; i++){
            unprocessedDataList.add(new DataBatch(data, index, this));
            index = index+1000;
            num++;
        }
    }

    public ArrayList<DataBatch> getUnprocessedDataList(){
        return this.unprocessedDataList;
    }

    public Model trainModel (Model model){
        return model;
    }

    public Model getModel(){
        return model;
    }

    public Type getType(){
        return type;
    }

}
