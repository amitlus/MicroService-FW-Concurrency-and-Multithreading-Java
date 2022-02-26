package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    Data data;
    GPU source;
    int dataParts;
    int trainingTime;

    public DataBatch(Data data, int start_index, GPU source, int dataParts, int trainingTime){
        this.data = data;
        this.dataParts = dataParts;
        this.trainingTime = trainingTime;
        this.source = source;
    }

    public int getTrainingTime(){
        return trainingTime;
    }

    public int getDataParts() {
        return dataParts;
    }

    public Data getData(){
        return data;
    }

    public GPU getSource(){
        return source;
    }
    
}
