package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
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
    ArrayList<Model> listOfModels;
    boolean notNull = false;
    public StudentService(String name, Student student) {
        super(name);
        this.student = student;
        this.listOfModels = student.getListOfModels();;
    }

    @Override
    protected void initialize() {

        //WE WANT TO ENSURE THAT ALL THE GPUS SUBSCRIBED TO TRAIN MODEL EVENT BEFORE WE START
        int size = 0;
        while(!notNull)
            try{
                size = MessageBusImpl.getMessageToSubs().get(TrainModelEvent.class).size();
                notNull = true;
            }catch (NullPointerException e){}
        while (size < CRMSRunner.gpuSize)
            size = MessageBusImpl.getMessageToSubs().get(TrainModelEvent.class).size();

        //SUBSCRIBE TO TICK BROADCAST
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast)->{student.updateTick();
        if(TickBroadcast.isFinish() == true)
            terminate();
        if(!listOfModels.isEmpty()){
            //SEND TRAIN MODEL EVENT
            if(student.listOfModels.get(0).getStatus()== Model.Status.PreTrained){
                listOfModels.get(0).setStatus(Model.Status.Training);
                System.out.println("NEW MODEL "+listOfModels.get(0).getName()+" SENT by "+student.getName());
                sendEvent(new TrainModelEvent(listOfModels.get(0)));

            }

                //SEND TEST MODEL EVENT
            else if(student.listOfModels.get(0).getStatus()== Model.Status.Trained) {
                System.out.println("TEST EVENT OF "+listOfModels.get(0).getName()+" SENT");
                sendEvent(new TestModelEvent());
                student.listOfModels.get(0).setStatus(Model.Status.Tested);

            }
                //SEND PUBLISH RESULT EVENT
            else if(student.listOfModels.get(0).getStatus()== Model.Status.Tested) {
                if(listOfModels.get(0).getResult() == Model.Results.Good) {
                    System.out.println("PUBLISH RESULT OF "+listOfModels.get(0).getName()+" SENT");
                    sendEvent(new PublishResultsEvent(student.listOfModels.get(0)));
                    student.successfulModels.add(student.listOfModels.remove(0));
                }
                else
                    student.failedModels.add(student.listOfModels.remove(0));
            }
         }});



        //SUBSCRIBE TO PUBLISH CONFERENCE BROADCAST
        subscribeBroadcast(PublishConferenceBroadcast.class, (PublishConferenceBroadcast)->{student.updateStudentResume(PublishConferenceBroadcast.getSucessfullModels());
        });

    }
}
