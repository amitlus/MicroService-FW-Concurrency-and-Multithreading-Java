package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.ConferenceInformation;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link //PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    ConferenceInformation conferenceInformation;
    public ConferenceService(String name, ConferenceInformation conferenceInformation) {
        super(name);
        this.conferenceInformation = conferenceInformation;
    }

    @Override
    protected void initialize() throws InterruptedException {
        //SUBSCRIBE TO TICK BROADCAST
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{conferenceInformation.updateTick();
        if(conferenceInformation.getCurrentTick() == conferenceInformation.getDate()) {
            //SEND PUBLISH CONFERENCE BROADCAST
            sendBroadcast(new PublishConferenceBroadcast(conferenceInformation.getSuccessfulModels()));
            System.out.println("PUBLISH CONFERENCE BROADCAST SENT");
            msb.unregister(this);
        }
        });

        //SUBSCRIBE TO PUBLISH RESULT EVENT
        subscribeEvent(PublishResultsEvent.class, (PublishResultsEvent)->{conferenceInformation.addSuccessfulModel(PublishResultsEvent.getModel());
        msb.complete(PublishResultsEvent, true);
            System.out.println("UPDATED RESULTS");
        });



        //SUBSCRIBE TO TERMINATE BROADCAST
        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast)->{terminate();});
    }
}
