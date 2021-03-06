package bgu.spl.mics;
import bgu.spl.mics.example.messages.ExampleEvent;

import java.util.Iterator;
import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/** NEED TO IMPLEMENT ROUND-ROBIN PATTERN */

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	public static ConcurrentHashMap<MicroService, BlockingQueue<Message>> microToMsg;
	public static ConcurrentHashMap<Class<? extends Message>, BlockingQueue<MicroService>> messageToSubs;
	public static ConcurrentHashMap<Event<?>,Future> eventToFuture;

	public static ConcurrentHashMap<MicroService, BlockingQueue<Message>> getMicroToMsg() {
		return microToMsg;
	}

	public boolean isSubscribedToEvent(Class<? extends ExampleEvent> aClass, MicroService mes) {
		return messageToSubs.get(aClass).contains(mes);
	}

	public boolean isSubscribedToBroadcast(Class<? extends Broadcast> aClass, MicroService mes) {
		return messageToSubs.get(aClass).contains(mes);
	}

	public boolean isRegisterd(MicroService mes) {
		return microToMsg.get(mes)!=null;
	}

	public boolean isUnregisterd(MicroService mes) {
		return microToMsg.get(mes) == null;
	}

	private static class SingletonHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	// Private constructor suppresses generation of a (public) default constructor
	MessageBusImpl() {
		microToMsg = new ConcurrentHashMap<MicroService, BlockingQueue<Message>>();
		messageToSubs = new ConcurrentHashMap<Class<? extends Message>, BlockingQueue<MicroService>>();
		eventToFuture = new ConcurrentHashMap<Event<?>,Future>();
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}

	public static ConcurrentHashMap<Class<? extends Message>, BlockingQueue<MicroService>> getMessageToSubs() {
		return messageToSubs;
	}

	/**
	 * @pre: none
	 * @post: isSubscribedToEvent(@ param type, @ param m) == true
	 */

	//BACK AND FIX THIS
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(!messageToSubs.containsKey(type))
			messageToSubs.putIfAbsent(type, new LinkedBlockingQueue<MicroService>()); //putIfAbsent is an atomic method
		messageToSubs.get(type).add(m);
	}

	/**
	 * @pre: none
	 * @post: isSubscribedToBroadcast(@ param type, @ param m) == true
	 */

	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (messageToSubs) {
			if (!messageToSubs.containsKey(type))
				messageToSubs.putIfAbsent(type, new LinkedBlockingQueue<MicroService>());
			messageToSubs.get(type).add(m);
		}
	}

	/**
	 * @pre: none
	 * @post: getFuture(@ param e).get() == @param result
	 */

	@Override
	public synchronized <T> void complete(Event<T> e, T result) {

		synchronized (eventToFuture) {
			if (eventToFuture.containsKey(e))
				eventToFuture.get(e).resolve(result);
			else
				throw new IllegalArgumentException("Event was not added to Event-Future hash map");
		}
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

	public void sendBroadcast(Broadcast b) {
			if(messageToSubs.get(b.getClass()) == null)
				return;
			BlockingQueue<MicroService> q = messageToSubs.get(b.getClass());//If empty he waits (That's how LinkedBlockingQueue works)
//			synchronized (q) {
				try {
					Iterator<MicroService> iter = q.iterator();
					while (iter.hasNext())
						microToMsg.get(iter.next()).add(b);

				} catch (NullPointerException npe) {
					System.out.println("no one has ever subscribed to this kind of broadcast");
					npe.printStackTrace();
				}
			}
//		}

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

	//It's already thread safe because the data structure is thread-safe
	public <T> Future<T> sendEvent(Event<T> e) {
		//Check existence of handlers queue of the Event type
		//Checking if there is a microservice who can process the event
			if ((!messageToSubs.containsKey(e.getClass()))  || messageToSubs.get(e.getClass())==null || (messageToSubs.get(e.getClass()).isEmpty()))
				return null;
			else {
				Future<T> future = new Future<T>();
				synchronized (eventToFuture) {
					if (eventToFuture.putIfAbsent(e, future) != null)
						future = eventToFuture.get(e);
				}
					MicroService handler = messageToSubs.get(e.getClass()).remove(); //Takes the head of the queue without removing it. Return null if empty
				//It won't return null because we already ensure that the queue is not empty
				microToMsg.get(handler).add(e); // adding the event to the handler message queue
				messageToSubs.get(e.getClass()).add(handler);
				return future;
			}
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
		BlockingQueue<Message> q = new LinkedBlockingQueue<Message>();
		microToMsg.putIfAbsent(m, q);
	}

	/**
	 * @pre:
	 * @post:
	 */

	public boolean isRegistered(MicroService m) {
		BlockingQueue microServiceQ = microToMsg.get(m);
		return(microServiceQ!=null);
	}

	/**
	 * @pre: isRegistered(@ param m) == true
	 * @post: isRegistered(@ param m) == false
	 */

	//It's already thread safe because the data structure is thread-safe
	public void unregister(MicroService m) {
		//Remove m's queue
		if(microToMsg.get(m.getName())!=null){
			microToMsg.remove(m.getName());
		}

		//Removes all the instances of m from all the queues he subscribed to
		for (Map.Entry mapElement : messageToSubs.entrySet()) {
			Iterator<MicroService> listOfMicroServices = ((LinkedBlockingQueue) mapElement.getValue()).iterator();
			boolean removed = false;
			MicroService check;
			while (listOfMicroServices.hasNext() && !removed) {
				check = listOfMicroServices.next();
				if (check == m) {
					removed = true;
					((LinkedBlockingQueue<?>) mapElement.getValue()).remove(check);
				}
			}
		}
	}


	// TODO Auto-generated method stub
	/**
	 * @pre:
	 * @post:
	 */

	public boolean isUnregistered(MicroService m) {
		BlockingQueue microServiceQ = microToMsg.get(m);
		return(microServiceQ==null);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		//If there is a queue for this microservice, return a Message from it, and if it's empty, wait until a Message inserted
		if(!(microToMsg.get(m)==null))
			return microToMsg.get(m).take();
		else
			throw new IllegalArgumentException("m not registered");
	}
}

