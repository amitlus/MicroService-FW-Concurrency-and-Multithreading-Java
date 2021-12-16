package bgu.spl.mics.application.objects;


import java.util.ArrayList;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private static ArrayList<DataBatch> dataToProcess;
	private static ArrayList<DataBatch> dataProcessed;
	private static Cluster instance = null;

	private Cluster(){
		this.dataToProcess = new ArrayList<DataBatch>();
		this.dataProcessed = new ArrayList<DataBatch>();
	}

	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {
		if (instance == null)
			instance = new Cluster();
		return instance;
	}


	public static ArrayList<DataBatch> getDataToProcessList(){
		return dataToProcess;
	}

	public static ArrayList<DataBatch> getDataProcessedList(){
		return dataProcessed;
	}

	public static DataBatch getUnprocessedDataBatch(){
		DataBatch dataBatch = dataToProcess.get(0);
		dataToProcess.remove(0);
		return dataBatch;
	}

	public static DataBatch getProcessedData(){
		DataBatch dataBatch = dataProcessed.get(0);
		dataProcessed.remove(0);
		return dataBatch;
	}

}
