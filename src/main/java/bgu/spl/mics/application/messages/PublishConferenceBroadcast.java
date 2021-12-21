package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.ConferenceInformation;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;

public class PublishConferenceBroadcast implements Broadcast {

    ArrayList<Model> sucessfullModels;
    public PublishConferenceBroadcast(ArrayList<Model> successfullModels)
    {
        this.sucessfullModels = successfullModels;
    };

    public ArrayList<Model> getSucessfullModels() {
        return sucessfullModels;
    }


}
