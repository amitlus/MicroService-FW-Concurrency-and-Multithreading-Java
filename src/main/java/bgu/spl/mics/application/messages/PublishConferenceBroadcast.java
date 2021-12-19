package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.ConferenceInformation;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;

public class PublishConferenceBroadcast implements Broadcast {

    ArrayList<Model> successfullModels;
    public PublishConferenceBroadcast(ArrayList<Model> successfullModels)
    {
        this.successfullModels = successfullModels;
    };

    public ArrayList<Model> getSuccessfullModels() {
        return successfullModels;
    }


}
