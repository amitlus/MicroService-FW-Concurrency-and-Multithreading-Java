package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.objects.Cluster;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random;


import java.util.ArrayList;

import static bgu.spl.mics.application.objects.Model.Results.Bad;
import static bgu.spl.mics.application.objects.Model.Results.Good;

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

    LinkedBlockingQueue<Message> fastMessages;
    LinkedBlockingQueue<Message> trainEvents;


    private Type type;
    private Model model;
    private Cluster cluster;
    private MessageBusImpl msb = MessageBusImpl.getInstance();
    private int vramSpace;
    private int currentTick;
    private int trainingTime;
    int GPUTimeUnits;
    int startingTime;


    public boolean isCounting =  false;

    public GPU(Type type, Cluster cluster) {
        unprocessedDataList = new LinkedBlockingQueue<DataBatch>();
        processedDataList = new LinkedBlockingQueue<DataBatch>();
        TrainedDataList = new LinkedBlockingQueue<DataBatch>();
        this.currentTick = 0;
        this.startingTime = 0;
        fastMessages = new LinkedBlockingQueue<Message>();
        trainEvents = new LinkedBlockingQueue<>();
        this.cluster = Cluster.getInstance();
        this.type = type;
        this.model = null;
        if (type == Type.RTX3090) {
            vramSpace = 1;//32
            trainingTime = 1;
        } else if (type == Type.RTX2080) {
            vramSpace = 1;//16
            trainingTime = 2;
        } else if (type == Type.GTX1080) {
            vramSpace = 1;//8
            trainingTime = 4;
        }
    }

    public int getVramSpace() {
        return vramSpace;
    }

    public void Test() {
        Random rand = new Random();
        int rand_int = rand.nextInt(10);

        if(model.getStudent().getStatus() == Student.Degree.MSc){
            if(rand_int >= 0 && rand_int <= 5)
                model.setResult(Good);
            else
                model.setResult(Bad);
        }
        else {
            if (rand_int >= 0 && rand_int <= 7)
                model.setResult(Good);
            else
                model.setResult(Bad);
            }
        }

    public void updateTick() {
        currentTick++;
    }

    public static Type getTypeFromString(String gpuType) {
        if(gpuType == "RTX3090")
            return Type.RTX3090;
        else if(gpuType == "RTX2080")
            return Type.RTX2080;
        else
            return Type.GTX1080;
    }

    public void setCounting(boolean counting) {
        isCounting = counting;
    }

    public MessageBusImpl getMsb() {
        return msb;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public LinkedBlockingQueue<DataBatch> getProcessedDataList(){
        return processedDataList;
    }

    public LinkedBlockingQueue<DataBatch> getTrainedDataList(){
        return TrainedDataList;
    }

    public LinkedBlockingQueue<Message> getFastMessages(){
        return fastMessages;
    }

    public LinkedBlockingQueue<Message> getTrainEvents(){
        return trainEvents;
    }


    //SENDING UNPROCESSED DATA BATCHES TO THE CLUSTER
    //WHEN SENDING WE AUTOMATICALLY UPDATE THE VRAM SIZE, THIS WAY WE ENSURE THAT THERE WILL BE SPACE
    //FOR THE PROCESSED BATCH TO RETURN
    public void sendDataBatch() throws InterruptedException {
        while (!unprocessedDataList.isEmpty() & vramSpace > 0) {
            DataBatch dataBatch = unprocessedDataList.poll();
            System.out.println("GPU " + unprocessedDataList.peek().source.getType() + " sent UNPROCESSED DB " + model.getName());
            vramSpace--;
            cluster.getDataToProcessList().add(dataBatch);
            System.out.println("CLUSTER'S DataToProcessList SIZE is " + cluster.getDataToProcessList().size());
        }
    }
//        if(vramSpace == 0) {
//            try {
//                trainDataBatch(processedDataList.take());
//                System.out.println("GPU'S DataProcessed SIZE is "+processedDataList.size());
//
//            }catch(NullPointerException e) {} catch (InterruptedException e) {
//                e.printStackTrace();
//            };



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
        }
    }



        public void trainDataBatch(DataBatch dataBatch) {
            System.out.println("GPU started TRAINING and the TIME LEFT is "+dataBatch.trainingTime);
            if(startingTime == 0)
                startingTime = currentTick;
            //dataBatch.decreaseTrainingTimeLeft();
            GPUTimeUnits++;
            if (currentTick - startingTime == dataBatch.getTrainingTime()) {
                dataBatch.trainingTime = 0;
               //AFTER WE FINISH TRAINING A DATA BATCH, WE REMOVE IT FROM THE PROCESSED LIST AND ADD IT TO THE TRAINED LIST

               //NOW WE CAN SEND ANOTHER DATA BATCH TO THE CLUSTER
                vramSpace++;
                //sendDataBatch();

                //AFTER WE FINISH TRAINING A DATA BATCH, WE CHECK IF WE FINISHED TRAINING ALL THE MODEL'S DATA BATCHES
                if (TrainedDataList.size() == dataBatch.dataParts) {
                    //UPDATE THE LIST OF TRAINED DATA IN THE CLUSTER'S STATISTICS
                    ArrayList<String> stat = (ArrayList<String>) Cluster.Statistics[0];
                    stat.add(model.getName());
                    Cluster.Statistics[0] = stat;

                    //CHANGING THE STATUS AND NOTIFY WE FINISHED COUNTING
                    model.setStatus(Model.Status.Trained);
                    System.out.println("MODEL "+getModel().getName()+" TRAINED");
                    isCounting = false;
                }
            }
        }

        public Model getModel () {
            return model;
        }

        public Type getType () {
            return type;
        }

        public boolean isCounting() {
            return isCounting;
        }

        public void terminate() {
        }

        public void setModel(Model model) {
        this.model = model;
    }

    }

