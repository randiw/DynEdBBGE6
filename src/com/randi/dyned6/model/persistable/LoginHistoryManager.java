package com.randi.dyned6.model.persistable;

import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

/**
 * Keep record of every user that has login.
 * @author Randi Waranugraha
 *
 */
public class LoginHistoryManager implements Persistable {

	private static final long KEY = 0xb43d52de55e4b9f1L; // com.randi.dyned6.model.persistable.LoginHistoryManager

	private static LoginHistoryManager instance = null;

	private Hashtable logins;

	private static PersistentObject historyObject;

	private LoginHistoryManager() {
		logins = new Hashtable();
	}

	/**
	 * Retrieves the system's LoginHistoryManager instance.
	 * @return Instance of LoginHistoryManager.
	 */
	public static LoginHistoryManager getInstance() {
		if (instance == null) {
			historyObject = PersistentStore.getPersistentObject(KEY);
			if (historyObject.getContents() == null) {
				instance = new LoginHistoryManager();
				synchronized (historyObject) {
					historyObject.setContents(instance);
				}
			} else {
				instance = (LoginHistoryManager) historyObject.getContents();
			}
		}
		return instance;
	}

	/**
	 * Determines if user exist in system memory and has login before.
	 * @param userid Username
	 * @param password Password
	 * @return True if user has login before.
	 */
	public boolean checkLogin(String userid, String password){
		if(logins.containsKey(userid)){
			UserLogin userLogin = (UserLogin)logins.get(userid);
			String userPass = userLogin.getPassword();
			if(password.equals(userPass)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines if user exist in system memory.
	 * @param username Username.
	 * @return True if user exist.
	 */
	public boolean isUser(String username){
		if(logins.containsKey(username)){
			return true;
		}
		return false;
	}

	/**
	 * Restore user SessionManager instance.
	 * @param username Username.
	 * @return SessionManager instance.
	 */
	public SessionManager restoreSession(String username){
		if(logins.containsKey(username)){
			UserLogin userLogin = (UserLogin)logins.get(username);
			return userLogin.getSessionManager();
		}
		return null;
	}
	
	/**
	 * Restore user AvatarManager instance.
	 * @param username Username.
	 * @return AvatarManager instance.
	 */
	public AvatarManager restoreAvatar(String username){
		if(logins.containsKey(username)){
			UserLogin userLogin = (UserLogin)logins.get(username);
			return userLogin.getAvatarManager();
		}
		return null;
	}
	
	/**
	 * Restore user EducationManager instance.
	 * @param username Username
	 * @return EducationManager instance.
	 */
	public EducationManager restoreEducation(String username){
		if(logins.containsKey(username)){
			UserLogin userLogin = (UserLogin)logins.get(username);
			return userLogin.getEducationManager();
		}
		return null;
	}
	
	/**
	 * Backup every instance this user have, including SessionManager, AvatarManager and EducationManager.
	 * @param userid Username.
	 * @param session SessionManager instance.
	 * @param avatar AvatarManager instance.
	 * @param education EducationManager instance.
	 */
	public void backup(String userid, SessionManager session, AvatarManager avatar, EducationManager education){
		if(userid != null){
			if(logins.containsKey(userid)){
				UserLogin userLogin = (UserLogin)logins.get(userid);
				userLogin.setSessionManager(session);
				userLogin.setAvatarManager(avatar);
				userLogin.setEducationManager(education);

				logins.put(userid, userLogin);
				historyObject.commit();
			}			
		}
	}

	/**
	 * Adds new user that has successfully login to history. 
	 * @param userid Username.
	 * @param password Password.
	 */
	public void addNewUser(String userid, String password){
		UserLogin userLogin = new UserLogin();
		userLogin.setPassword(password);

		logins.put(userid, userLogin);
		historyObject.commit();
	}
	
	private class UserLogin implements Persistable {

		private String password;

		private AvatarManager avatarManager;
		private EducationManager educationManager;
		private SessionManager sessionManager;
		
		public UserLogin() {
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
		public void setAvatarManager(AvatarManager avatarManager) {
			this.avatarManager = avatarManager;
		}
		
		public void setEducationManager(EducationManager educationManager) {
			this.educationManager = educationManager;
		}
		
		public void setSessionManager(SessionManager sessionManager) {
			this.sessionManager = sessionManager;
		}

		public String getPassword() {
			return password;
		}

		public AvatarManager getAvatarManager() {
			return avatarManager;
		}

		public EducationManager getEducationManager() {
			return educationManager;
		}

		public SessionManager getSessionManager() {
			return sessionManager;
		}		
	}
}