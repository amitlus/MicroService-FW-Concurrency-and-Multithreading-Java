package bgu.spl.mics.application.objects;
import bgu.spl.mics.application.services.CPUService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private static BlockingQueue<DataBatch> dataToProcess;
	private ArrayList<GPU> GPUArray;
	private static ArrayList<CPU> CPUArray;
	public static Object[] Statistics = new Object[4];

	public static void terminateCPUS() {
		for (int i =0; i< CPUArray.size(); i++){
			dataToProcess.add(new DataBatch(null ,0, null, 0, 0));
		}
	}

	private static class SingletonHolder {
		private static Cluster instance = new Cluster();
	}

	private Cluster() {
		GPUArray = new ArrayList<GPU>();
		CPUArray = new ArrayList<CPU>();
		dataToProcess = new LinkedBlockingQueue<DataBatch>();
		Statistics[0] = new ArrayList<String>(); //Names of all models trained
		Statistics[1] = new AtomicInteger(); //Total number of data batches processed by the CPUs
		Statistics[2] = new AtomicInteger(); //Number of CPU time units used
		Statistics[3] = new AtomicInteger(); //Number of GPU time units used
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

	public static BlockingQueue<DataBatch> getDataToProcessList() {
		return dataToProcess;
	}


	//SENDS BACK THE PROCESSED DATA BATCH TO ITS SOURCE GPU
	public void sendProcessedDataBatch(GPU gpu, DataBatch dataBatch) throws InterruptedException {
		((AtomicInteger)Statistics[1]).addAndGet(1);
//		System.out.println("GPU'S dataProcessedList SIZE is "+gpu.processedDataList.size());
		gpu.getProcessedDataList().add(dataBatch);
//		System.out.println("GPU'S dataProcessedList SIZE is "+gpu.processedDataList.size());

	}
}