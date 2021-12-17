package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.objects.Cluster;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random


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

    LinkedBlockingQueue<DataBatch> unprocessedDataList;
    LinkedBlockingQueue<DataBatch> processedDataList;
    LinkedBlockingQueue<DataBatch> TrainedDataList;

    LinkedBlockingQueue<Class<? extends Message>> fastMessages;
    LinkedBlockingQueue<Event<?>> trainEvents;


    private Type type;
    private Model model;

    public Cluster cluster;
    public MessageBus msb = MessageBusImpl.getInstance();
    private int vramSpace;
    int currentTick;
    int trainingTime;

    public boolean isCounting =  false;

    public GPU(Type type, Cluster cluster) {
        this.cluster = Cluster.getInstance();
        this.type = type;
        this.model = null;
        if (type == Type.RTX3090) {
            vramSpace = 32;
            trainingTime = 1;
        } else if (type == Type.RTX2080) {
            vramSpace = 16;
            trainingTime = 2;
        } else if (type == Type.GTX1080) {
            vramSpace = 8;
            trainingTime = 4;
        }
    }

    public String Test() {
        Random rand = new Random();
        int rand_int = rand.nextInt(11);

        if(model.student.getStatus() == Student.Degree.MSc){
            if(rand_int >= 0 && rand_int <= 1)
                return "GOOD";
            else
                return "BAD";
        }
        else {
            if (rand_int >= 0 && rand_int <= 2)
                return "GOOD";
            else
                return "BAD";
            }
        }

    public void updateTick() {
        currentTick++;
    }

    public LinkedBlockingQueue<DataBatch> getProcessedDataList(){
        return processedDataList;
    }

    public LinkedBlockingQueue<DataBatch> getTrainedDataList(){
        return TrainedDataList;
    }

    public LinkedBlockingQueue<Class<? extends Message>> getFastMessages(){
        return fastMessages;
    }

    public LinkedBlockingQueue<Event<?>> getTrainEvents(){
        return trainEvents;
    }


    //SENDING UNPROCESSED DATA BATCHES TO THE CLUSTER
    //WHEN SENDING WE AUTOMATICALLY UPDATE THE VRAM SIZE, THIS WAY WE ENSURE THAT THERE WILL BE SPACE
    //FOR THE PROCESSED BATCH TO RETURN
    public void sendDataBatch() {
        while(!unprocessedDataList.isEmpty() & vramSpace>0) {
            DataBatch dataBatch = unprocessedDataList.remove();
            vramSpace --;
            cluster.getDataToProcessList().add(dataBatch);
        }
    }

    //MAKE A LIST OF DATA BATCHES FROM THE DATA
    public void makeDataList() {
        Data data = model.getData();
        int dataSize = data.getSize();
        int index = 0;
        int check = dataSize % 1000;
        int num;
        if (check != 0)
            num = dataSize / 1000 + 1;
        else
            num = dataSize / 1000;

        for (int i = 0; i < num; i++) {
            unprocessedDataList.add(new DataBatch(data, index, this, num, trainingTime));
            index = index + 1000;
            num++;
        }
    }



        public void trainDataBatch(DataBatch dataBatch) {
            model.setStatus(Model.Status.Training);
            dataBatch.decreaseTrainingTimeLeft();
            if (dataBatch.getTrainingTimeLeft() == 0)
                //AFTER WE FINISH TRAINING A DATA BATCH, WE CHECK IF WE FINISHED TRAINING ALL THE MODEL'S DATA BATCHES
                if (TrainedDataList.size() == dataBatch.dataParts)
                    model.setStatus(Model.Status.Trained);

        }

        public Model getModel () {
            return model;
        }

        public Type getType () {
            return type;
        }

    }

