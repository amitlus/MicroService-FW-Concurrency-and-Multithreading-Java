package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	int tickTime;
	int duration;
	int currentTime = 0;
	public TimeService(int tickTime, int duration) {
		super("timeService");
		this.tickTime = tickTime;
		this.duration = duration;
	}

	@Override
	protected void initialize() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				currentTime++;
				if(currentTime == duration)
					sendBroadcast(new TerminateBroadcast());
				else
					sendBroadcast(new TickBroadcast());
			}
		},0,tickTime);
	}
}
