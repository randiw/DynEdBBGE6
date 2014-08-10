package com.randi.dyned6.model;

import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

import com.randi.dyned6.tools.ThreadManager;

/**
 * Manage and keep records of downloaded Audio for dialogs and questions.
 * 
 * @author Randi Waranugraha
 *
 */
public class AudioDownload implements Persistable, ContentListener {

	private static final long KEY = 0xdcbf46180f0f1c5dL; // com.randi.dyned6.model.AudioDownload

	private static AudioDownload instance = null;

	private Vector dialogs;
	private Vector questions;
	private static PersistentObject audioObject;
		
	private AudioDownload() {
		dialogs = new Vector();
		questions = new Vector();
	}

	/**
	 * Retrieves the system's AudioDownload instance.
	 * 
	 * @return instance of AudioDownload
	 */
	public static AudioDownload getInstance() {
		if(instance == null) {
			audioObject = PersistentStore.getPersistentObject(KEY);
			if(audioObject.getContents() == null) {
				instance = new AudioDownload();
				synchronized (audioObject) {
					audioObject.setContents(instance);
				}
			} else {
				instance = (AudioDownload) audioObject.getContents();
			}
		}
		return instance;
	}
	
	/**
	 * Adds dialog to download.
	 * 
	 * @param fileName Dialog file name.
	 */
	public void addDialogs(String fileName){
		if(!dialogs.contains(fileName)){			
			dialogs.addElement(fileName);
		}
	}
	
	/**
	 * Adds question to download.
	 * 
	 * @param fileName Question file name.
	 */
	public void addQuestions(String fileName){
		if(!questions.contains(fileName)){
			questions.addElement(fileName);			
		}
	}

	/**
	 * Start download remaining dialogs file.
	 */
	public void startDialog(){
		if(!dialogs.isEmpty()){
			String dialog = (String) dialogs.firstElement();			
			String downloadUrl = Resources.FILES_AUDIO_DIALOGUES + dialog;
			String saveLocation = Resources.PATH_AUDIO_DIALOGUES + dialog;
			
			DownloadAndSaveFile downloadAndSaveFile = new DownloadAndSaveFile(saveLocation, downloadUrl);
			downloadAndSaveFile.setContentListener(this);
			downloadAndSaveFile.setLabel(dialog);
			
			ThreadManager threadManager = ThreadManager.getInstance();
			threadManager.register(downloadAndSaveFile);
			threadManager.start();
		}
	}
	
	/**
	 * Start download remaining questions file.
	 */
	public void startQuestion(){
		if(!questions.isEmpty()){
			String question = (String) questions.firstElement();
			String downloadUrl = Resources.FILES_AUDIO_QUESTIONS + question;
			String saveLocation = Resources.PATH_AUDIO_QUESTIONS + question;
			
			DownloadAndSaveFile downloadAndSaveFile = new DownloadAndSaveFile(saveLocation, downloadUrl);
			downloadAndSaveFile.setContentListener(this);
			downloadAndSaveFile.setLabel(question);

			ThreadManager threadManager = ThreadManager.getInstance();
			threadManager.register(downloadAndSaveFile);			
			threadManager.start();
		}
	}
	
	/**
	 * Call startDialog() and startQuestion() to start download.
	 */
	public void start(){
		startDialog();
		startQuestion();
	}

	public void onFinishTask(String label) {
		if(dialogs.contains(label)){
			dialogs.removeElementAt(0);
			startDialog();
		} else if(questions.contains(label)){
			questions.removeElementAt(0);
			startQuestion();
		}
		audioObject.commit();			
	}

	public void onErrorTask(String label) {
		if(dialogs.contains(label)){
			dialogs.removeElementAt(0);
			dialogs.insertElementAt(label, 1);
			startDialog();
		} else if(questions.contains(label)){
			questions.removeElementAt(0);
			questions.insertElementAt(label, 1);
			startQuestion();
		}
	}
}