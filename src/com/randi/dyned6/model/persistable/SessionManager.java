package com.randi.dyned6.model.persistable;

import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

/**
 * Keep record for current user session.
 * @author Randi Waranugraha
 *
 */
public class SessionManager implements Persistable {
	
	private static final long KEY = 0xb52b34824ef71193L; //com.randi.dyned6.model.persistable.SessionManager
	
	private static SessionManager instance = null;
	
	private String username;
	private String id;
	private String app_key;
	private String role;
	
	private Hashtable tablePofile;
	
	private static PersistentObject sessionObject;
	
	private SessionManager() {
	}
	
	/**
	 * Retrieves the system's SessionManager instance.
	 * @return Instance of SessionManager.
	 */
	public static SessionManager getInstance() {
		if(instance == null) {
			sessionObject = PersistentStore.getPersistentObject(KEY);
			if(sessionObject.getContents() == null){
				instance = new SessionManager(); 
				synchronized (sessionObject) {
					sessionObject.setContents(instance);
				}
			} else {
				instance = (SessionManager)sessionObject.getContents();
			}
		}
		return instance;
	}

	/**
	 * Clear this system's SessionManager instance.
	 */
	public static void clear(){
		sessionObject.setContents(null);
		sessionObject.commit();
		instance = null;
	}

	/**
	 * Set the system SessionManager instance.
	 * @param sessionManager
	 */
	public static void set(SessionManager sessionManager){
		instance = sessionManager;
		sessionObject = PersistentStore.getPersistentObject(KEY);
		synchronized (sessionObject) {
			sessionObject.setContents(instance);
		}
	}

	/**
	 * Get a copy of the system SessionManager instance.
	 * @return A new copy instance of SessionManager.
	 */
	public static SessionManager backup(){
		SessionManager sessionManager = new SessionManager();
		if(instance != null){
			sessionManager.setUsername(instance.username);
			sessionManager.setId(instance.id);
			sessionManager.setApp_key(instance.app_key);
			sessionManager.setRole(instance.role);
			sessionManager.setTablePofile(instance.tablePofile);
		}
		return sessionManager;
	}

	/**
	 * Set the current session username.
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Get the current session username.
	 * @return String of username.
	 */
	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApp_key() {
		return app_key;
	}

	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Hashtable getTablePofile() {
		return tablePofile;
	}

	public void setTablePofile(Hashtable tablePofile) {
		this.tablePofile = tablePofile;
	}
	
	public String getStatus(){
		return (String)tablePofile.get("status");
	}
}