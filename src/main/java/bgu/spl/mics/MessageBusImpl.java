package bgu.spl.mics;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance = null;
	public static HashMap<String, Queue<Event<?>>> queueHashMap;

	// Private constructor suppresses generation of a (public) default constructor
	private MessageBusImpl() {
		queueHashMap = new HashMap<String, Queue<Event<?>>>();
	}

	public static MessageBusImpl getInstance() {
		if (instance == null)
			instance = new MessageBusImpl();
		return instance;
	}


	/**
	 * @pre: none
	 * @post: isSubscribedToEvent(@ param type, @ param m) == true
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre:
	 * @post:
	 */
	public <T> boolean isSubscribedToEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @pre: none
	 * @post: isSubscribedToBroadcast(@ param type, @ param m) == true
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre:
	 * @post:
	 */
	public boolean isSubscribedToBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @pre: none
	 * @post: getFuture(@ param e).get() == @param result
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre: none
	 * @post:
	 */

	public <T> boolean isComplete(Event<T> e, T result) {
		// TODO Auto-generated method stub
		return false;
	}


	/**
	 * @pre: none
	 * @post: for each microService m (isSubscribedToBroadcast(@param b.getClass(), m) == true)
	 * b.equals(awaitMessage(m))
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre: none
	 * @post:
	 */
	public boolean isBroadcastSent(Broadcast b) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @pre: none
	 * @post: for each microService m (isSubscribedToEvent(@param b.getClass(), m) == true)
	 * e.equals(awaitMessage(m))
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @pre: none
	 * @post:
	 */
	public boolean isEventSent(Event e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @pre: isRegistered(@ param m) == false
	 * @post: isRegistered(@ param m) == true
	 */
	@Override
	public void register(MicroService m) {
		Queue<Event<?>> q = new ConcurrentLinkedQueue<Event<?>>();
		queueHashMap.put(m.getName(), q);
		// TODO Auto-generated method stub

	}

	/**
	 * @pre:
	 * @post:
	 */

	public boolean isRegistered(MicroService m) {
		Queue microServiceQ = queueHashMap.get(m.getName());
		return(microServiceQ!=null);
		// TODO Auto-generated method stub
	}

	/**
	 * @pre: isRegistered(@ param m) == true
	 * @post: isRegistered(@ param m) == false
	 */
	@Override
	public void unregister(MicroService m) {
		if(queueHashMap.get(m.getName())!=null){
			queueHashMap.remove(m.getName());
		}
		// TODO Auto-generated method stub

	}

	/**
	 * @pre:
	 * @post:
	 */

	public boolean isUnregistered(MicroService m) {
		Queue microServiceQ = queueHashMap.get(m.getName());
		return(microServiceQ==null);
		// TODO Auto-generated method stub
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}


}

