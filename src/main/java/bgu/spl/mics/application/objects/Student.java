package bgu.spl.mics.application.objects;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.StudentService;
import java.util.ArrayList;


/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {


    /**
     * Enum representing the Degree the student is studying for.
     */


    public enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications = 0;
    private int papersRead = 0;
    public ArrayList<Model> listOfModels;
    public ArrayList<Model> successfulModels;
    public ArrayList<Model> failedModels;
    int currentTick;

    public Student(String name, String department, Degree status){
        this.name = name;
        this.department = department;
        this.status = status;
        listOfModels = new ArrayList<Model>();
        successfulModels = new ArrayList<Model>();
        failedModels = new ArrayList<Model>();
    }

    public String getName(){
        return name;
    }

    public String getDepartment(){
        return department;
    }

    public Degree getStatus(){
        return status;
    }

    public int getPublications(){
        return publications;
    }

    public int getPapersRead(){
        return papersRead;
    }

    public void setStatus(Degree status){
        this.status = status;
    }

    public void setPublications(int publications){
        this.publications = publications;
    }

    public void setPapersRead(int papersRead){
        this.papersRead = papersRead;
    }

    public ArrayList<Model> getListOfModels(){
        return listOfModels;
    }

    public void updateTick() {
        currentTick++;
    }

    public void updateStudentResume(ArrayList<Model> sucModels) {
        for(int i=0; i<sucModels.size(); i++) {
            if (successfulModels.contains(sucModels.get(i)))
                publications++;
            else
                papersRead++;
        }
        System.out.println("STUDENT's "+getName()+" RESUME UPDATED");
     }

    public void terminate() {
    }
}
