package bgu.spl.mics.application.objects;

import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.GPUService;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    Data data;
    int start_index;
    GPU source;
    int dataParts;
    int trainingTimeLeft;

    public DataBatch(Data data, int start_index, GPU source, int dataParts, int trainingTime){
        this.data = data;
        this.start_index = start_index;
        this.dataParts = dataParts;
        this.trainingTimeLeft = trainingTimeLeft;
        this.source = source;
    }

    public void decreaseTrainingTimeLeft(){
        trainingTimeLeft--;
    }

    public int getTrainingTimeLeft(){
        return trainingTimeLeft;
    }

    public Data getData(){
        return data;
    }

    public GPU getSource(){
        return source;
    }

    public int getStart_index(){
        return start_index;
    }
    
}
