package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.callbacks.PublishConferenceBroadcastCall;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.ArrayList;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    Student student;
    public StudentService(String name, Student student) {
        super(name);
        this.student = student;
    }

    @Override
    protected void initialize() {

        subscribeBroadcast(PublishConferenceBroadcast.class, (PublishConferenceBroadcast(b)->{}));
        //ITERATE THROUGH ALL THE MODELS AND CREATE 3 EVENTS FOR EACH- TRAIN, TEST, PUBLISH & SEND THEM
        ArrayList<Model> listOfModels = student.getListOfModels();
        for(int i=0; i<listOfModels.size(); i++){
            sendEvent(new TrainModelEvent(listOfModels.get(i)));

        }


    }
}
