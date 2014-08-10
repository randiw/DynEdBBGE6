package com.randi.dyned6.view.manager;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.awan.dyned6.general.GeneralFunction;
import com.randi.dyned6.model.ComprehensionItem;
import com.randi.dyned6.model.ComprehensionManager;
import com.randi.dyned6.model.ContentListener;
import com.randi.dyned6.model.DownloadAndSaveFile;
import com.randi.dyned6.model.Resources;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.tools.StringHelpers;
import com.randi.dyned6.view.field.ChoiceField;

public class AudioAndTextManager extends VerticalFieldManager implements FieldChangeListener, ContentListener {

	private PlayAudioManager playAudioManager;
	private FontSetting fontSetting = FontSetting.getInstance();

	private LabelField labelField;

	private String question;
	private String answer;
	private String audioLocation;
	private String audioFile;
	
	private boolean isDownload;

	public AudioAndTextManager(ComprehensionItem item) {
		super(USE_ALL_WIDTH | NO_VERTICAL_SCROLL);
		String[] questions = StringHelpers.explode('|', item.getQuestion());
		audioFile = questions[0];
		audioLocation = Resources.PATH_AUDIO_QUESTIONS + audioFile;
		question = questions[1];
		answer = item.getAnswer();
		
		if(GeneralFunction.checkIsFileExists(audioLocation)){
			playAudioManager = new PlayAudioManager(audioLocation);
			add(playAudioManager);			
		} else {
			isDownload = true;
			fontSetting.setPoint(Font.PLAIN, 7);
			labelField = new LabelField("Please wait.. downloading audio material", USE_ALL_WIDTH | LabelField.VCENTER);
			labelField.setPadding(new XYEdges(10, 10, 10, 10));
			labelField.setFont(fontSetting.getFont());
			labelField.setBackground(BackgroundFactory.createSolidBackground(Color.LIGHTGRAY));
			add(labelField);
		}
		
		
		VerticalFieldManager vfManager = new VerticalFieldManager(USE_ALL_WIDTH | VERTICAL_SCROLL);
		vfManager.setPadding(20, 60, 20, 60);
		add(vfManager);
		
		String[] options = item.getRandomOptions();
		for(int i = 0; i < options.length; i++){
			ChoiceField choiceField = new ChoiceField(options[i]);
			choiceField.setPadding(5, 10, 5, 10);
			choiceField.setMargin(0, 0, 10, 0);
			choiceField.setChangeListener(this);
			vfManager.add(choiceField);
		}
	}

	protected void onDisplay() {
		if(isDownload){
			if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_NONE)){
				System.out.println("no coverage");
				synchronized (UiApplication.getEventLock()) {
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							Dialog.alert("Please enable internet connection to download audio material");
						}
					});
				}
			} else {
				String downloadUrl = Resources.FILES_AUDIO_QUESTIONS + audioFile;
				DownloadAndSaveFile downloadAndSaveFile = new DownloadAndSaveFile(audioLocation, downloadUrl);
				downloadAndSaveFile.setContentListener(this);
				downloadAndSaveFile.start();				
			}
		} else {			
			playAudioManager.start();
		}
		super.onDisplay();
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof ChoiceField){
			if(!isDownload){
				if(!playAudioManager.isPlay()){
					ComprehensionManager manager = ComprehensionManager.getInstance();
					
					ChoiceField choiceField = (ChoiceField)field;
					String label = choiceField.getText();
					manager.addAnswer(question, label, answer.equals(label));
					manager.nextManager();				
				}				
			}
		}
	}

	public void onFinishTask(String label) {
		isDownload = false;
		synchronized (UiApplication.getEventLock()) {
			playAudioManager = new PlayAudioManager(audioLocation);
			replace(labelField, playAudioManager);
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					playAudioManager.start();
				}
			});
		}
	}

	public void onErrorTask(String label) {
		System.out.println("[AudioAndTextManager]error");
	}
}