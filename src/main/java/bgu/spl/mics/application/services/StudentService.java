package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
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
        ArrayList<Model> listOfModels = student.getListOfModels();

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{student.updateTick();

        if(student.listOfModels.get(0).getStatus()== Model.Status.PreTrained)
            sendEvent(new TrainModelEvent(listOfModels.get(0)));
        else if(student.listOfModels.get(0).getStatus()== Model.Status.Trained)
            sendEvent(new TestModelEvent());
        else if(student.listOfModels.get(0).getStatus()== Model.Status.Tested)
            sendEvent(new PublishResultsEvent());

        }




            });



        subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast)->{student.terminate();});

        subscribeBroadcast(PublishConferenceBroadcast.class, (PublishConferenceBroadcast)->{student.updatePublications();
            student.updatePapersRead();
        });

        for(int i=0; i<listOfModels.size(); i++){
            sendEvent(new TrainModelEvent(listOfModels.get(i)));

            sendEvent(new TestModelEvent());

            sendEvent(new PublishResultsEvent());
        }


    }
}
