package com.randi.dyned6.model.persistable;

import java.util.Hashtable;

import com.randi.dyned6.model.Resources;
import com.randi.dyned6.view.AvatarScreen;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.Persistable;

/**
 * Store Avatar videos.
 * @author Randi Waranugraha
 *
 */
public class AvatarManager implements Persistable {

	public static final int BEFORE = 0;
	public static final int AFTER = 1;
	
	private static final long KEY = 0xaaf8386794d079c6L; //com.randi.dyned6.model.persistable.AvatarManager

	private static AvatarManager instance = null;

	private Hashtable units;
	private static PersistentObject avatarObject;
	
	private AvatarManager() {
		units = new Hashtable();
	}

	/**
	 * Retrieves the system's AvatarManager instance.
	 * @return Instance of AvatarManager.
	 */
	public static AvatarManager getInstance() {
		if(instance == null) {
			avatarObject = PersistentStore.getPersistentObject(KEY);
			if(avatarObject.getContents() == null) {
				instance = new AvatarManager();
				synchronized (avatarObject) {
					avatarObject.setContents(instance);
				}
			} else {
				instance = (AvatarManager) avatarObject.getContents();
			}
		}
		return instance;
	}

	/**
	 * Set the system AvatarManager instance.
	 * @param avatarManager
	 */
	public static void set(AvatarManager avatarManager){
		instance = avatarManager;
		avatarObject = PersistentStore.getPersistentObject(KEY);
		synchronized (avatarObject) {
			avatarObject.setContents(instance);
		}
	}
	
	/**
	 * Get a copy of the system AvatarManager instance.
	 * @return A new copy instance of AvatarManager.
	 */
	public static AvatarManager backup(){
		AvatarManager avatarManager = new AvatarManager();
		if(instance != null){
			avatarManager.units = instance.units;			
		}
		return avatarManager;
	}
	
	/**
	 * Get the Avatar video file location from the indicated Unit and Lesson index.
	 * @param unitIndex Index of Unit.
	 * @param lessonIndex Index of Lesson.
	 * @return File location string.
	 */
	public String getFileLocation(int unitIndex, int lessonIndex){
		if(unitIndex == 0 && (lessonIndex == 0 || lessonIndex == 2)){
			return "/video/" + getFileName(unitIndex, lessonIndex);
		}
		return Resources.PATH_AVATAR_EN + getFileName(unitIndex, lessonIndex);
	}
	
	/**
	 * Get the Avatar video file name from the indicated Unit and Lesson index.
	 * @param unitIndex Index of Unit.
	 * @param lessonIndex Index of Lesson.
	 * @return File name string.
	 */
	public String getFileName(int unitIndex, int lessonIndex){
		switch (unitIndex) {
		case 0: // unit 1
			if(lessonIndex == 0){
				return "DynEd1.3gp"; // after lesson A
			}
			if(lessonIndex == 2){
				return "DynEd2.3gp"; // after lesson C
			}
			break;
		case 2: // unit 3
			if(lessonIndex == 0){
				return "DynEd7.3gp"; // before lesson A
			}
			if(lessonIndex == 2){
				return "DynEd3.3gp"; // after lesson C
			}
			break;
		case 5: // unit 6
			if(lessonIndex == 0){
				return "DynEd7.3gp"; // before lesson A
			}
			if(lessonIndex == 3){
				return "DynEd5.3gp"; // after lesson C
			}
			break;
		case 8: // unit 9
			if(lessonIndex == 0){
				return "DynEd6.3gp";
			}
			break;
		case 7: // unit 8
		case 11: // unit 12
			if(lessonIndex == 0){
				return "DynEd7.3gp"; // before lesson A
			}
			break;
		
		case 1: // unit 2
		case 3: // unit 4
		case 6: // unit 7
		case 9: // unit 10
			if(lessonIndex == 0){
				return "DynEd4.3gp"; // before lesson A
			}
			break;
		}
		return null;
	}
	
	/**
	 * Play the Avatar video using file location.
	 * @param fileLocation Avatar video file location.
	 */
	public void playAvatar(String fileLocation){
		synchronized (UiApplication.getEventLock()) {
			UiApplication.getUiApplication().pushScreen(new AvatarScreen(fileLocation));
		}
	}
	
	/**
	 * Set the indicated Avatar video played.
	 * @param unitIndex Index of Unit.
	 * @param lessonIndex Index of Lesson.
	 */
	public void setFilePlayed(int unitIndex, int lessonIndex){
		String fileName = getFileName(unitIndex, lessonIndex);
		AvatarItem item = (AvatarItem)units.get(fileName);
		item.setHasPlay(true);
		units.put(fileName, item);
		avatarObject.commit();
	}
	
	/**
	 * Check if this position should play the Avatar video indicated with Unit and Lesson index.
	 * @param unitIndex Index of Unit.
	 * @param lessonIndex Index of Lesson.
	 * @param position Position to play video.
	 * @return True if position match with the indicated Unit and Lesson index.
	 */
	public boolean checkPlay(int unitIndex, int lessonIndex, int position){
//		if(position == BEFORE){
//			if(unitIndex == 0 || unitIndex == 2 || unitIndex == 5){
//				return false;
//			}
//		} else if(position == AFTER){
//			if(unitIndex == 1 || unitIndex == 3 || unitIndex == 6 || unitIndex == 9
//					|| unitIndex == 2 || unitIndex == 5 || unitIndex == 7 || unitIndex == 11 
//					|| unitIndex == 8) {
//				return false;
//			}
//		}
//
//		if(getFileName(unitIndex, lessonIndex) != null){
//			String fileName = getFileName(unitIndex, lessonIndex);
//			AvatarItem item;
//			if(units.containsKey(fileName)){
//				item = (AvatarItem) units.get(fileName);
//			} else {
//				item = new AvatarItem(fileName);
//				item.setHasPlay(false);
//				item.setFileLocation(getFileLocation(unitIndex, lessonIndex));
//				units.put(fileName, item);
//				avatarObject.commit();
//			}
//
//			if(!item.hasPlay){
//				return true;
//			}
//		}

		return false;
	}
	
	/**
	 * Clear the instance.
	 */
	public static void clear(){
		if(avatarObject != null){
			avatarObject.setContents(null);
			avatarObject.commit();			
		}
		instance = null;
	}
	
	private class AvatarItem implements Persistable {
		private String id;
		private String fileLocation;
		private boolean hasPlay;

		public AvatarItem(String id) {
			this.id = id;
		}

		public String getFileLocation() {
			return fileLocation;
		}

		public String getId() {
			return id;
		}

		public void setHasPlay(boolean hasPlay) {
			this.hasPlay = hasPlay;
		}

		public void setFileLocation(String fileLocation) {
			this.fileLocation = fileLocation;
		}
	}
}