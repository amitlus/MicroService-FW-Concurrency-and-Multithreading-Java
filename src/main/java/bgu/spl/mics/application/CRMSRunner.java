package bgu.spl.mics.application;
import bgu.spl.mics.StrToInt;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) throws IOException, InterruptedException {

//        String s = "2";
//        int i = StrToInt.stoi(s);
//        System.out.println(i);

        List<MicroService> microServicesList = new ArrayList<>();
        List<Student> studentsList = new ArrayList<>();
        List<ConferenceInformation> ConferencesList = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();

        JsonParser parser = new JsonParser();
        Object obj = new JsonParser().parse(new FileReader("example_input.json"));
        JsonObject jo = (JsonObject) obj;

        JsonArray arrayOfStudents = jo.getAsJsonArray("Students");
        JsonArray arrayOfGpus = jo.getAsJsonArray("GPUS");
        JsonArray arrayOfCpus = jo.getAsJsonArray("CPUS");
        JsonArray arrayOfConferences = jo.getAsJsonArray("Conferences");
        //JsonObject tickTime = jo.getAsJsonObject("TickTime");
        //JsonObject duration = jo.getAsJsonObject("Duration");
        int tickTime = jo.getAsJsonObject("TickTime").getAsInt();
        int duration = jo.getAsJsonObject("Duration").getAsInt();

        for (int i=0; i<arrayOfStudents.size(); i++){
            JsonObject student = arrayOfStudents.get(i).getAsJsonObject();
            String studentName = student.get("name").toString();
            String studentDepartment = student.get("department").toString();
            String studentStatus = student.get("status").toString();
            JsonArray arrayOfModels = student.getAsJsonArray("models");
            Student s = new Student(StrToInt.stoi(studentName), studentDepartment, studentStatus == "MSc" ? Student.Degree.MSc : Student.Degree.PhD);// fix status enum

            for (int j=0;j<arrayOfModels.size();j++){
                JsonObject model = arrayOfModels.get(j).getAsJsonObject();
                String modelName = model.get("name").toString();
                String dataType = model.get("type").toString();
                int modelSize =  model.get("size").getAsInt();
                Data.Type daType;
                if(dataType.equals("images"))
                    daType = Data.Type.Images;
                else if(dataType.equals("Text"))
                    daType = Data.Type.Text;
                else
                    daType = Data.Type.Tabular;
                Data modelData = new Data(daType,0,modelSize);
                Model m = new Model(modelName, modelData , s);
                s.getListOfModels().add(m);
            }
            studentsList.add(s);
            microServicesList.add(new StudentService(studentName,s));
        }


        for (int i=0; i<arrayOfGpus.size(); i++){ //create gpus
            String gpuType = arrayOfGpus.get(i).toString();
            GPU.Type type = GPU.getTypeFromString(gpuType);
            GPU gpu = new GPU(type,null,cluster) ;
            cluster.getGPUS().add(gpu);
            microServicesList.add(new GPUService("GPU "+ (i+1),gpu));
        }

        for (int i=0; i<arrayOfCpus.size(); i++){  //create cpus

            int numOfCores = arrayOfCpus.get(i).getAsInt();
            CPU cpu = new CPU(numOfCores,cluster);
            cluster.getCPUS().add(cpu);
            microServicesList.add(new CPUService("CPU "+ (i+1),cpu));
        }

        for (int i=0; i<arrayOfConferences.size(); i++){ // create conferences
            JsonObject conference = arrayOfConferences.get(i).getAsJsonObject();
            String conferenceName = conference.get("name").toString();
            int conferenceDate = conference.get("date").getAsInt();
            ConferenceInformation confrenceInformation = new ConferenceInformation(conferenceName,conferenceDate);
            ConferencesList.add(confrenceInformation);
            microServicesList.add(new ConferenceService(conferenceName,confrenceInformation));
        }

        microServicesList.add(new TimeService(tickTime, duration)) ;

        for (int i = 0; i < microServicesList.size(); i++) {
            Thread thread = new Thread(microServicesList.get(i));
            threadList.add(thread);
            thread.start();
        }
}}

