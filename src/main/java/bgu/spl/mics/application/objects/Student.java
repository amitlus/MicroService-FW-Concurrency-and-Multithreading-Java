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

    private int name;
    private String department;
    private Degree status;
    private int publications = 0;
    private int papersRead = 0;
    public ArrayList<Model> listOfModels;


    public Student(int name, String department, Degree status){
        this.name = name;
        this.department = department;
        this.status = status;
    }

    public int getName(){
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
}
