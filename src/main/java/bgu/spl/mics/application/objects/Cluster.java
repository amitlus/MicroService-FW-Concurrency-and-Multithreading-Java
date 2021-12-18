package bgu.spl.mics.application.objects;
import java.util.concurrent.LinkedBlockingQueue;
import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayList;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private static LinkedBlockingQueue<DataBatch> dataToProcess;
//	private static LinkedBlockingQueue<DataBatch> dataProcessed;

	private static class SingletonHolder {
		private static Cluster instance = new Cluster();
	}

	private Cluster() {
		this.dataToProcess = new LinkedBlockingQueue<DataBatch>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Cluster getInstance() {
		return Cluster.SingletonHolder.instance;
	}


	public LinkedBlockingQueue<DataBatch> getDataToProcessList() {
		return dataToProcess;
	}


	//SENDS BACK THE PROCESSED DATA BATCH TO ITS SOURCE GPU
	public void sendProcessedDataBatch(GPU gpu, DataBatch dataBatch) {
		gpu.getProcessedDataList().add(dataBatch);
	}
}