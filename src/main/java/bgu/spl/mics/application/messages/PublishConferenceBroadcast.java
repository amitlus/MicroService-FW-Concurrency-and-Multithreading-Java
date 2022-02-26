package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;

public class PublishConferenceBroadcast implements Broadcast {

    ArrayList<Model> successfulModels;
    public PublishConferenceBroadcast(ArrayList<Model> successfulModels)
    {
        this.successfulModels = successfulModels;
    }

    public ArrayList<Model> getSuccessfulModels() {
        return successfulModels;
    }


}
