package bgu.spl.mics.application.objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import java.util.ArrayList;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private BlockingQueue<DataBatch> dataToProcess;
	private ArrayList<GPU> GPUArray;
	private ArrayList<CPU> CPUArray;
	public static Object[] Statistics = new Object[4];


	private static class SingletonHolder {
		private static Cluster instance = new Cluster();
	}

	private Cluster() {
		GPUArray = new ArrayList<GPU>();
		CPUArray = new ArrayList<CPU>();
		dataToProcess = new LinkedBlockingQueue<DataBatch>();
		Statistics[0] = new ArrayList<String>(); //Names of all models trained
		Statistics[1] = 0; //Total number of data batches processed by the CPUs
		Statistics[2] = 0; //Number of CPU time units used
		Statistics[3] = 0; //Number of GPU time units used
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Cluster getInstance() {
		return Cluster.SingletonHolder.instance;
	}

	public ArrayList<GPU> getGPUS() {
		return GPUArray;
	}

	public ArrayList<CPU> getCPUS() {
		return CPUArray;
	}

	public BlockingQueue<DataBatch> getDataToProcessList() {
		return dataToProcess;
	}


	//SENDS BACK THE PROCESSED DATA BATCH TO ITS SOURCE GPU
	public void sendProcessedDataBatch(GPU gpu, DataBatch dataBatch) throws InterruptedException {
		Statistics[1] = (int)Statistics[1] + 1;
		//System.out.println(Statistics[1]);
//		System.out.println("GPU'S dataProcessedList SIZE is "+gpu.processedDataList.size());
		gpu.getProcessedDataList().add(dataBatch);
//		System.out.println("GPU'S dataProcessedList SIZE is "+gpu.processedDataList.size());

	}
}