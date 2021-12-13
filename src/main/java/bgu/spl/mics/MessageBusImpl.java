package bgu.spl.mics;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance = null;
	public static HashMap<MicroService, ConcurrentLinkedQueue<Message>> microToMsg;
	public static HashMap<Message, ConcurrentLinkedQueue<MicroService>> messageToSubs;



	// Private constructor suppresses generation of a (public) default constructor
	private MessageBusImpl() {
		microToMsg = new HashMap<MicroService, ConcurrentLinkedQueue<Message>>();
		messageToSubs = new HashMap<Message, ConcurrentLinkedQueue<MicroService>>();
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
		if(!isSubscribedToEvent(type, m))
			messageToSubs.get(type).add(m);
	}

	/**
	 * @pre:
	 * @post:
	 */
	public <T> boolean isSubscribedToEvent(Class<? extends Event<T>> type, MicroService m) {
		return(messageToSubs.get(type).contains(m));
	}

	/**
	 * @pre: none
	 * @post: isSubscribedToBroadcast(@ param type, @ param m) == true
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(!isSubscribedToBroadcast(type, m))
			messageToSubs.get(type).add(m);

	}

	/**
	 * @pre:
	 * @post:
	 */
	public boolean isSubscribedToBroadcast(Class<? extends Broadcast> type, MicroService m) {
		return(messageToSubs.get(type).contains(m));
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
		ConcurrentLinkedQueue<MicroService> q = messageToSubs.get(b);
		Iterator<MicroService> iter = q.iterator();
		while(iter.hasNext())
			microToMsg.get(iter.next()).add(b);
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
		ConcurrentLinkedQueue<Message> q = new ConcurrentLinkedQueue<Message>();
		microToMsg.put(m, q);
	}

	/**
	 * @pre:
	 * @post:
	 */

	public boolean isRegistered(MicroService m) {
		ConcurrentLinkedQueue microServiceQ = microToMsg.get(m);
		return(microServiceQ!=null);
	}

	/**
	 * @pre: isRegistered(@ param m) == true
	 * @post: isRegistered(@ param m) == false
	 */
	@Override
	public void unregister(MicroService m) {
		if(microToMsg.get(m.getName())!=null){
			microToMsg.remove(m.getName());
		}

		for (Queue<MicroService> i : messageToSubs.values()) {
			for(MicroService ms: i){

			}

		}
		// TODO Auto-generated method stub

	}

	/**
	 * @pre:
	 * @post:
	 */

	public boolean isUnregistered(MicroService m) {
		ConcurrentLinkedQueue microServiceQ = microToMsg.get(m);
		return(microServiceQ==null);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(microToMsg.get(m).isEmpty())
			wait();
		return microToMsg.get(m).poll();
	}


}

