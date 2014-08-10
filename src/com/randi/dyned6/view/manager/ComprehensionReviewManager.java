package com.randi.dyned6.view.manager;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;

import com.randi.dyned6.model.ComprehensionManager;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.view.GrammarScreen;
import com.randi.dyned6.view.field.CustomImageButtonField;
import com.randi.dyned6.view.field.GreyLabelField;

public class ComprehensionReviewManager extends ReviewManager {
	
	private ComprehensionManager manager = ComprehensionManager.getInstance();
	private EducationManager educationManager = EducationManager.getInstance();
	private CustomImageButtonField grammarButton;

	public ComprehensionReviewManager(Vector vector) {
		super(vector);
		
		String unitId = educationManager.getUnitId(manager.getUnitIndex());
		String lessonId = Integer.toString(manager.getLessonIndex());
		educationManager.setComprehensionPoint(unitId, lessonId, getCount(), getCorrectCount());

		FontSetting fontSetting = FontSetting.getInstance();
		fontSetting.setPoint(Font.PLAIN, 1);
		
		LabelField labelField = new LabelField("", FOCUSABLE);
		labelField.setFont(fontSetting.getFont());
		add(labelField);
		
		if(Variables.bigTouchScreen()){
			if(isDone()){
				GreyLabelField greyGrammarButton = new GreyLabelField("Grammar");
//				GreyButtonField greyGrammarButton = new GreyButtonField("Grammar");
				greyGrammarButton.setMargin(10, 30, 0, 30);
				greyGrammarButton.setChangeListener(this);
				add(greyGrammarButton);
			}
			
			GreyLabelField greyListenButton = new GreyLabelField("Try Again");
//			GreyButtonField greyListenButton = new GreyButtonField("Try Again");
			greyListenButton.setMargin(15, 30, 0, 30);
			greyListenButton.setChangeListener(this);
			add(greyListenButton);
		} else {
			if(isDone()){
				grammarButton = new CustomImageButtonField("grammar.png", "grammar_btn.png", FIELD_HCENTER);
				grammarButton.setChangeListener(this);
				add(grammarButton);
			}
			
			XYEdges edges = new XYEdges(10, 0, 0, 0);
			if(Variables.smallScreen()){
				edges.set(5, 0, 0, 0);
			}
			CustomImageButtonField listenButton = new CustomImageButtonField("listening_slctd.png", "listening.png", FIELD_HCENTER);
			listenButton.setPadding(edges);
			listenButton.setChangeListener(this);
			add(listenButton);			
		}
	}
	
	public void fieldChanged(Field field, int context) {
		if(field instanceof CustomImageButtonField){
			synchronized (UiApplication.getEventLock()) {
				if(field == grammarButton){
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
					UiApplication.getUiApplication().pushScreen(new GrammarScreen(manager.getUnitIndex(), manager.getLessonIndex()));
					ComprehensionManager.close();
				} else {
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
				}
			}
		}
		if(field instanceof GreyLabelField) {
			synchronized (UiApplication.getEventLock()) {
				GreyLabelField greyButton = (GreyLabelField)field;
				String label = greyButton.getLabel();
				if("Grammar".equals(label)){
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
					UiApplication.getUiApplication().pushScreen(new GrammarScreen(manager.getUnitIndex(), manager.getLessonIndex()));
					ComprehensionManager.close();					
				} else {
					UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
				}
			}
		}
	}
}
