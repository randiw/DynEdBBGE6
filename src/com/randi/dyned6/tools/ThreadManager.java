package com.randi.dyned6.tools;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Manage and queue threading.
 * @author Randi Waranugraha
 *
 */
public class ThreadManager {

	public final static int MAX_QUEUE = 4;
	
	private static ThreadManager instance = null;
	
	private Hashtable threadQueue;
	private int queue;
	private int queueNumber;
	
	private ThreadManager(){
		threadQueue = new Hashtable();
	}
	
	/**
	 * Retrieves the system's ThreadManager instance.
	 * @return ThreadManager instance.
	 */
	public static ThreadManager getInstance() {
		if(instance == null) {
			instance = new ThreadManager();
		}
		return instance;
	}

	/**
	 * Register new ThreadAttendees to the queue.
	 * @param getter ThreadAttendees.
	 */
	public void register(ThreadAttendees getter){
		System.out.println("queueNumber " + queueNumber);
		threadQueue.put(Integer.toString(queueNumber), getter);
		queueNumber++;
	}

	/**
	 * Start the queue.
	 */
	public void start(){
		Enumeration eKeys = threadQueue.keys();
		while (eKeys.hasMoreElements() && queue < MAX_QUEUE) {
			String key = (String) eKeys.nextElement();
			ThreadAttendees getter = (ThreadAttendees) threadQueue.get(key);
			threadQueue.remove(key);
			queue++;
			getter.start(key);
		}
	}

	/**
	 * Notify the ThreadManager instance that the thread with specified key has finished.
	 * @param key
	 */
	public void notify(String key){
		System.out.println("notify " + key);
		queue--;
		threadQueue.remove(key);
	
		start();
	}

	/**
	 * Empty the queue.
	 */
	public void emptyQueue(){
		if(!threadQueue.isEmpty()){
			threadQueue.clear();
			queue = 0;
		}
	}
}