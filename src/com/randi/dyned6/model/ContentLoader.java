package com.randi.dyned6.model;

import net.rim.device.api.ui.UiApplication;

import com.awan.dyned6.general.GeneralFunction;
import com.randi.dyned6.tools.ProgressListener;
import com.randi.dyned6.tools.ThreadManager;

/**
 * A thread class to download Audio and Avatar video files.
 * @author Randi Waranugraha
 *
 */
public class ContentLoader extends Thread implements ContentListener {
	
	private ProgressListener listener;
	private ThreadManager threadManager = ThreadManager.getInstance();
	private AudioDownload audioDownload = AudioDownload.getInstance();
	private AvatarDownload avatarDownload = AvatarDownload.getInstance();
	
	private int totalProgress;
	private int currentProgress;
	
	private static final String[] DIALOGUES_LESSON_ONE = {
		"NDE0101A.mp3", "NDE0101B.mp3", "NDE0101C.mp3"
	};
	
	private static final String[] QUESTIONS_LESSON_ONE = {
		"NDE0101B_Q1.mp3", "NDE0101B_Q2.mp3", "NDE0101B_Q3.mp3"
	};
	
	private static final String[] DIALOGUES = {
		"NDE0102A.mp3", "NDE0102B.mp3", "NDE0102C.mp3",

    	"NDE0103A.mp3", "NDE0103B.mp3", "NDE0103C.mp3", "NDE0103_BONUS.mp3",

    	"NDE0104A.mp3", "NDE0104B.mp3", "NDE0104C.mp3",

    	"NDE0105A.mp3", "NDE0105B.mp3", "NDE0105C.mp3",

    	"NDE0106A.mp3", "NDE0106B.mp3", "NDE0106C.mp3", "NDE0106_BONUS.mp3",

    	"NDE0107A.mp3", "NDE0107B.mp3", "NDE0107C.mp3",

    	"NDE0108A.mp3", "NDE0108B.mp3", "NDE0108C.mp3",

    	"NDE0109A.mp3", "NDE0109B.mp3", "NDE0109C.mp3", "NDE0109_BONUS.mp3",

    	"NDE0110A.mp3", "NDE0110B.mp3", "NDE0110C.mp3",

    	"NDE0111A.mp3", "NDE0111B.mp3", "NDE0111C.mp3", 

    	"NDE0112A.mp3", "NDE0112B.mp3", "NDE0112C.mp3", "NDE0112_BONUS.mp3"
	};
	
	private static final String[] QUESTIONS = {
		"NDE0102B_Q1.mp3",

    	"NDE0102B_Q2.mp3", "NDE0102B_Q3.mp3", "NDE0103B_Q1.mp3", "NDE0103B_Q2.mp3",

    	"NDE0103B_Q3.mp3", "NDE0104B_Q1.mp3", "NDE0104B_Q2.mp3", "NDE0104B_Q3.mp3",

    	"NDE0105B_Q1.mp3", "NDE0105B_Q2.mp3", "NDE0105B_Q3.mp3", "NDE0106B_Q1.mp3",

    	"NDE0106B_Q2.mp3", "NDE0106B_Q3.mp3", "NDE0107B_Q1.mp3", "NDE0107B_Q2.mp3",

    	"NDE0107B_Q3.mp3", "NDE0108B_Q1.mp3", "NDE0108B_Q2.mp3", "NDE0108B_Q3.mp3",

    	"NDE0109B_Q1.mp3", "NDE0109B_Q2.mp3", "NDE0109B_Q3.mp3", "NDE0110B_Q1.mp3",

    	"NDE0110B_Q2.mp3", "NDE0110B_Q3.mp3", "NDE0111B_Q1.mp3", "NDE0111B_Q2.mp3",

    	"NDE0111B_Q3.mp3", "NDE0112B_Q1.mp3", "NDE0112B_Q2.mp3", "NDE0112B_Q3.mp3"
	};

//	private static final String[] AVATARS = {
//		"DynEd1Done.3gp", "DynEd2BDone.3gp", "DynEd3ADone.3gp", "DynEd4Done.3gp", "DynEd5Done.3gp", 
//		"DynEd6ADone.3gp", "DynEd7ADone.3gp", 
//	};
	private static final String[] AVATARS = {
		"DynEd3.3gp", "DynEd4.3gp", "DynEd5.3gp", 
		"DynEd6.3gp", "DynEd7.3gp", 
	};

	/**
	 * Creates new object of ContentLoader
	 */
	public ContentLoader() {
	}

	/**
	 * Sets the ProgressListener to this object.
	 * @param listener 
	 */
	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}
	
	public void run() {
		GeneralFunction.createDirectory(Resources.PATH);
		GeneralFunction.createDirectory(Resources.PATH_AUDIO);
		GeneralFunction.createDirectory(Resources.PATH_AUDIO_LEVEL);
		GeneralFunction.createDirectory(Resources.PATH_AUDIO_DIALOGUES);
		GeneralFunction.createDirectory(Resources.PATH_AUDIO_QUESTIONS);
		GeneralFunction.createDirectory(Resources.PATH_AVATAR);
		GeneralFunction.createDirectory(Resources.PATH_AVATAR_EN);

		if(listener != null) {
			listener.onPost("Preparing content..");
		}
		
		totalProgress = DIALOGUES_LESSON_ONE.length + QUESTIONS_LESSON_ONE.length;

		for(int i = 0; i < DIALOGUES_LESSON_ONE.length; i++){
			String dialog = DIALOGUES_LESSON_ONE[i];
			String downloadUrl = Resources.FILES_AUDIO_DIALOGUES + dialog;
			String saveLocation = Resources.PATH_AUDIO_DIALOGUES + dialog;

			DownloadAndSaveFile downloadAndSaveFile = new DownloadAndSaveFile(saveLocation, downloadUrl);
			downloadAndSaveFile.setContentListener(this);
			downloadAndSaveFile.setLabel(dialog);
			threadManager.register(downloadAndSaveFile);
		}

		for(int i = 0; i < QUESTIONS_LESSON_ONE.length; i++) {
			String question = QUESTIONS_LESSON_ONE[i];
			String downloadUrl = Resources.FILES_AUDIO_QUESTIONS + question;
			String saveLocation = Resources.PATH_AUDIO_QUESTIONS + question;

			DownloadAndSaveFile downloadAndSaveFile = new DownloadAndSaveFile(saveLocation, downloadUrl);
			downloadAndSaveFile.setContentListener(this);
			downloadAndSaveFile.setLabel(question);
			threadManager.register(downloadAndSaveFile);
		}

		for(int i = 0; i < DIALOGUES.length; i++) {
			audioDownload.addDialogs(DIALOGUES[i]);
		}

		for(int i = 0; i < QUESTIONS.length; i++) {
			audioDownload.addQuestions(QUESTIONS[i]);
		}
		
		for(int i = 0; i < AVATARS.length; i++) {
			avatarDownload.addAvatars(AVATARS[i]);
		}
		
		if(listener != null){
			listener.onPost("Get ready to download");
		}
		threadManager.start();
		super.run();
	}

	public void onFinishTask(String label) {
		currentProgress++;
		if(listener != null) {
			listener.onProgress(currentProgress, totalProgress);
		}
		if(currentProgress == totalProgress) {
			synchronized (UiApplication.getEventLock()) {
				UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
				audioDownload.start();
				avatarDownload.start();
			}
		}
	}

	public void onErrorTask(String label) {
		onFinishTask(label);
	}
}