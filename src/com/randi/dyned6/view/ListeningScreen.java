package com.randi.dyned6.view;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.awan.dyned6.general.Constans;
import com.awan.dyned6.general.GeneralFunction;
import com.randi.dyned6.model.ContentListener;
import com.randi.dyned6.model.DownloadAndSaveFile;
import com.randi.dyned6.model.Resources;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.tools.ImageUtils;
import com.randi.dyned6.view.field.CustomImageButtonField;
import com.randi.dyned6.view.field.GreyLabelField;
import com.randi.dyned6.view.manager.PlayAudioListener;
import com.randi.dyned6.view.manager.PlayAudioManager;
import com.randi.dyned6.view.manager.ViewScriptManager;

public class ListeningScreen extends BasicScreen implements PlayAudioListener, ContentListener {

	private EducationManager educationManager = EducationManager.getInstance();
	private FontSetting fontSetting = FontSetting.getInstance();

	private VerticalFieldManager vfManager;
	private PlayAudioManager playAudioManager;
	private ViewScriptManager viewScriptManager;
	private LabelField labelField;

	private int unitIndex;
	private int lessonIndex;
	private long listeningTime;

	private boolean playOnce;
	private boolean onPause;
	private boolean isDownload;

	private String audioFile;
	private String audioLocation;

	public ListeningScreen(int unitIndex, int lessonIndex) {
		super("Listening");
		this.unitIndex = unitIndex;
		this.lessonIndex = lessonIndex;

		educationManager.openLesson(educationManager.getUnitId(unitIndex), Integer.toString(lessonIndex));

		vfManager = new VerticalFieldManager(USE_ALL_WIDTH | VERTICAL_SCROLL);

		audioFile = Constans.audioList[unitIndex][lessonIndex];

		audioLocation = Resources.PATH_AUDIO_DIALOGUES + audioFile;
		String viewScript = Constans.scriptList[unitIndex][lessonIndex];

		if (GeneralFunction.checkIsFileExists(audioLocation)) {
			playAudioManager = new PlayAudioManager(audioLocation);
			playAudioManager.setPlayAudioListener(this);
			vfManager.add(playAudioManager);
		} else {
			isDownload = true;
			fontSetting.setPoint(Font.PLAIN, 7);
			labelField = new LabelField("Please wait.. downloading audio material", USE_ALL_WIDTH | LabelField.VCENTER);
			labelField.setPadding(new XYEdges(10, 10, 10, 10));
			labelField.setFont(fontSetting.getFont());
			labelField.setBackground(BackgroundFactory.createSolidBackground(Color.LIGHTGRAY));
			vfManager.add(labelField);
		}

		String imageName = Constans.imageList[unitIndex][lessonIndex];
		Bitmap bitmap = Bitmap.getBitmapResource(imageName);

		if(Variables.bigTouchScreen()){
			int imageWidth = Display.getWidth() - 60;
			int imageHeight = imageWidth * bitmap.getHeight() / bitmap.getWidth();
			bitmap = ImageUtils.resizeBitmap(bitmap, imageWidth, imageHeight);
		
			QuestionBitmap bitmapField = new QuestionBitmap(bitmap);
			bitmapField.setPadding(15, 30, 10, 30);
			vfManager.add(bitmapField);

			GreyLabelField greyButton = new GreyLabelField("Questions");
			greyButton.setMargin(0, 30, 10, 30);
			greyButton.setChangeListener(this);
			vfManager.add(greyButton);
			
			viewScriptManager = new ViewScriptManager(viewScript);
			vfManager.add(viewScriptManager);			
		} else {
			viewScriptManager = new ViewScriptManager(viewScript);
			vfManager.add(viewScriptManager);

			HorizontalFieldManager hfManager = new HorizontalFieldManager(USE_ALL_WIDTH);

			int imageWidth = Display.getWidth() / 2;
			int imageHeight = (imageWidth * 2) / 3;
			bitmap = ImageUtils.resizeBitmap(bitmap, imageWidth, imageHeight);

			QuestionBitmap bitmapField = new QuestionBitmap(bitmap);
			bitmapField.setPadding(5, 10, 5, 10);
			hfManager.add(bitmapField);

			CustomImageButtonField customeImageButton = new CustomImageButtonField("question_btn_detoff.png", "question_btn_det.png", FIELD_RIGHT | FIELD_VCENTER);
			customeImageButton.setChangeListener(this);
			hfManager.add(customeImageButton);
			vfManager.add(hfManager);
		}
		add(vfManager);			
	}

	public void fieldChanged(Field field, int context) {
		if (field instanceof CustomImageButtonField || field instanceof GreyLabelField) {
			if (playAudioManager != null && playOnce && !playAudioManager.isPlay()) {
				if (onPause) {
					listeningTime += playAudioManager.getCurrentTime();
				}

				String unitId = educationManager.getUnitId(unitIndex);
				String lessonId = Integer.toString(lessonIndex);
				int time = (int) (listeningTime / 1000000);
				educationManager.addLessonListeningTime(unitId, lessonId, time);
				UiApplication.getUiApplication().pushScreen(new ComprehensionScreen(unitIndex, lessonIndex));
			}
		}
	}

	protected void onUiEngineAttached(boolean attached) {
		if (attached) {
			if (isDownload) {
				viewScriptManager.setPlaying(true);
				String downloadUrl = Resources.FILES_AUDIO_DIALOGUES + audioFile;
				DownloadAndSaveFile download = new DownloadAndSaveFile(audioLocation, downloadUrl);
				download.setContentListener(this);
				download.start();					
			} else {
				playAudioManager.start();
			}
		}
		super.onUiEngineAttached(attached);
	}

	public void onStartAudio() {
		if(!playOnce){
			viewScriptManager.setPlaying(true);			
		}
		if (onPause) {
			onPause = false;
		}
	}

	public void onPauseAudio() {
		onPause = true;
	}

	public void onFinishAudio() {
		playOnce = true;
		viewScriptManager.setPlaying(false);
		listeningTime += playAudioManager.getDuration();
	}

	protected void actionKeyEscape() {
		if(playAudioManager == null){
			super.actionKeyEscape();			
		} else {
			if (!playAudioManager.isPlay()) {
				super.actionKeyEscape();
			}			
		}
	}

	public void onFinishTask(String label) {
		System.out.println("[ListeningScreen] finishTask");
		isDownload = false;
		synchronized (UiApplication.getEventLock()) {
			playAudioManager = new PlayAudioManager(audioLocation);
			playAudioManager.setPlayAudioListener(this);
			vfManager.replace(labelField, playAudioManager);
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					playAudioManager.start();
				}
			});
		}
	}

	private class QuestionBitmap extends BitmapField {

		private Bitmap bitmap;
		
		public QuestionBitmap(Bitmap bitmap) {
			super(bitmap);
			this.bitmap = bitmap;
		}
		
		public int getPreferredWidth() {
			return bitmap.getWidth();
		}
		
		public int getPreferredHeight() {
			return bitmap.getHeight();
		}
		
		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
		
		protected void paint(Graphics g) {
			g.drawBitmap(0, 0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);
		}
	}

	public void onErrorTask(String label) {
		System.out.println("[ListeningScreen]error");
	}
}