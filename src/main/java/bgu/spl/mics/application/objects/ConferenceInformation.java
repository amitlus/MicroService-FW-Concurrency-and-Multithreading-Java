package bgu.spl.mics.application.objects;

import java.util.ArrayList;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConferenceInformation {

    private String name;
    private int date;
    private int currentTick;
    ArrayList<Model> successfulModels;

    public ConferenceInformation(String name, int date){
        this.name = name;
        this.date = date;
        this.successfulModels = new ArrayList<>();
    }

    public void addSuccessfulModel(Model modelName){
        successfulModels.add(modelName);
    }

    public String getName(){
        return name;
    }

    public int getDate(){
        return date;
    }

    public void updateTick() {
        currentTick++;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public ArrayList<Model> getSuccessfulModels() {
        return successfulModels;
    }

    public void terminate() {
    }
}
