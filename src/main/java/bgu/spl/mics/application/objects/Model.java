package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    enum Status{
        PreTrained, Training, Trained, Tested
    }

    enum Results{
        None, Good, Bad
    }

    String name;
    Data data;
    Student student;
    Status status;
    Results result;

    public Model(String name, Data data, Student student){
        this.name = name;
        this.data = data;
        this.student = student;
        this.status = Status.PreTrained;
        this.result = Results.None;
    }

    public String getName(){
        return name;
    }

    public Data getData(){
        return data;
    }

    public Student getStudent(){
        return student;
    }

    public Status getStatus(){
        return status;
    }

    public Results getResult(){
        return result;
    }

    public void setStatus(Status status){
        this.status = status;
    }


}
