package com.randi.dyned6.model;

import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

/**
 * Manage and keep records of downloaded Avatar video files.
 * 
 * @author Randi Waranugraha
 *
 */
public class AvatarDownload implements Persistable, ContentListener {
	
	private static final long KEY = 0x905ccd7dc8619a93L; // com.randi.dyned6.model.AvatarDownload

	private static AvatarDownload instance = null;
	
	private Vector avatars;
	private static PersistentObject avatarObject;
	
	private AvatarDownload() {
		avatars = new Vector();
	}

	/**
	 * Retrieves the system's AvatarDownload instance.
	 * 
	 * @return instance of AvatarDownload
	 */
	public static AvatarDownload getInstance() {
		if(instance == null) {
			avatarObject = PersistentStore.getPersistentObject(KEY);
			if(avatarObject.getContents() == null) {
				instance = new AvatarDownload();
				synchronized (avatarObject) {
					avatarObject.setContents(instance);
				}
			} else {
				instance = (AvatarDownload) avatarObject.getContents();
			}
		}
		return instance;
	}
	
	/**
	 * Adds avatar video to download.
	 * 
	 * @param fileName Avatar video file name.
	 */
	public void addAvatars(String fileName){
		if(!avatars.contains(fileName)){
			avatars.addElement(fileName);
		}
	}
	
	/**
	 * Start download remaining avatar videos file.
	 */
	public void start(){
//		if(!avatars.isEmpty()){
//			String avatar = (String) avatars.firstElement();
//			String downloadUrl = Resources.FILES_AVATAR_EN + avatar;
//			String saveLocation = Resources.PATH_AVATAR_EN + avatar;
//			
//			DownloadAndSaveFile downloadAndSaveFile = new DownloadAndSaveFile(saveLocation, downloadUrl);
//			downloadAndSaveFile.setContentListener(this);
//			downloadAndSaveFile.setLabel(avatar);
//			
//			ThreadManager threadManager = ThreadManager.getInstance();
//			threadManager.register(downloadAndSaveFile);
//			threadManager.start();
//		}
	}
	
	public void onFinishTask(String label) {
		if(avatars.contains(label)){
			avatars.removeElementAt(0);
			start();
			avatarObject.commit();
		}
	}

	public void onErrorTask(String label) {
		if(avatars.contains(label)){
			avatars.removeElementAt(0);
			if(avatars.size() > 0){
				avatars.insertElementAt(label, 1);				
			} else {
				avatars.insertElementAt(label, 0);
			}
			start();
			avatarObject.commit();
		}
	}
}