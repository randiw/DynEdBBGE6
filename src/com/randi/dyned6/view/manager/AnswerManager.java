package com.randi.dyned6.view.manager;

import com.randi.dyned6.tools.FontSetting;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;

public class AnswerManager extends Manager {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	private LabelField questionLabel;
	private LabelField answerLabel;
	private BitmapField icon;
	
	private boolean correct;
	
	public AnswerManager(String question, String answer, boolean correct) {
		super(USE_ALL_WIDTH);
		this.correct = correct;
		
		fontSetting.setPoint(Font.PLAIN, 8);

		if(correct){
			icon = new BitmapField(Bitmap.getBitmapResource("true.png"));
		} else {
			icon = new BitmapField(Bitmap.getBitmapResource("false.png"));			
		}
		add(icon);

		questionLabel = new LabelField(question);
		questionLabel.setFont(fontSetting.getFont());
		add(questionLabel);
		
		answerLabel = new LabelField(answer);
		answerLabel.setFont(fontSetting.getFont());
		add(answerLabel);
	}

	protected void sublayout(int width, int height) {
		layoutChild(icon, width, height);
		layoutChild(questionLabel, width - icon.getWidth(), height);
		layoutChild(answerLabel, width - icon.getWidth(), height - questionLabel.getHeight());
		
		setPositionChild(icon, 0, 0);
		setPositionChild(questionLabel, icon.getWidth(), 0);
		setPositionChild(answerLabel, icon.getWidth(), questionLabel.getHeight());
		
		setExtent(width, questionLabel.getHeight() + answerLabel.getHeight());
	}
	
	public boolean isCorrect() {
		return correct;
	}
}