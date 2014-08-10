package com.randi.dyned6.view.manager;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.FlowFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.randi.dyned6.model.GrammarItem;
import com.randi.dyned6.model.GrammarManager;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.view.field.GreyLabelField;
import com.randi.dyned6.view.field.SentenceButton;

public class SentenceBuilderManager extends VerticalFieldManager implements FieldChangeListener {

	private FontSetting fontSetting = FontSetting.getInstance();

	private LabelField answerField;
	
	private String answer;
	private String[] options;
	
	public SentenceBuilderManager(GrammarItem item) {
		super(USE_ALL_WIDTH | VERTICAL_SCROLL);
		answer = item.getAnswer();
		options = item.getRandomOptions();		
		initScreen();
	}
	
	private void initScreen(){
		if(getFieldCount()  > 0){
			deleteAll();			
		}
		
		XYEdges fieldEdges = new XYEdges(20, 20, 20, 20);
		XYEdges boxEdges = new XYEdges(20, 30, 0, 30);
		XYEdges buttonEdges = new XYEdges(0, 10, 0, 10);
		XYEdges answerBox = new XYEdges(0, 60, 20, 60);
		if(Variables.smallScreen()){
			fieldEdges.set(15, 15, 10, 15);
			boxEdges.set(10, 10, 0, 10);
			answerBox.set(0, 60, 10, 60);
		}
		
		fontSetting.setPoint(Font.PLAIN, 1);
		LabelField labelField = new LabelField("", FOCUSABLE);
		labelField.setFont(fontSetting.getFont());
		add(labelField);
		
		fontSetting.setPoint(Font.PLAIN, 8);
		answerField = new LabelField("", USE_ALL_WIDTH | LabelField.VCENTER){
			protected void paint(Graphics g) {
				g.setColor(0x5f5f5d);
				super.paint(g);
			};					
		};
		answerField.setFont(fontSetting.getFont());
		answerField.setBackground(BackgroundFactory.createSolidBackground(0xdedddb));
		answerField.setPadding(fieldEdges);
		add(answerField);
				
		fontSetting.setPoint(Font.PLAIN, 7);

		FlowFieldManager flowManager = new FlowFieldManager(USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);
		flowManager.setPadding(boxEdges);
		add(flowManager);
		for(int i = 0; i < options.length; i++){
			SentenceButton button = new SentenceButton(options[i]);
			button.setFont(fontSetting.getFont());
			button.setPadding(buttonEdges);
			button.setMargin(new XYEdges(0, 5, 5, 5));
			button.setChangeListener(this);
			flowManager.add(button);
		}		
		
//		GreyButtonField greyReset = new GreyButtonField("Reset");
		GreyLabelField greyReset = new GreyLabelField("Reset");
		greyReset.setMargin(15, 30, 0, 30);
		greyReset.setChangeListener(this);
		add(greyReset);

		GreyLabelField greyOk = new GreyLabelField("OK");
		greyOk.setMargin(15, 30, 0, 30);
		greyOk.setChangeListener(this);
		add(greyOk);
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof SentenceButton){
			SentenceButton button = (SentenceButton) field;
			String label = button.getLabel();
			answerField.setText(answerField.getText() + " " + label);
			button.setEditable(false);
		} else if(field instanceof GreyLabelField){
			GreyLabelField greyButton = (GreyLabelField) field;
			String label = greyButton.getLabel();
			if("OK".equals(label)){
				String attemptAnswer = answerField.getText().trim();
				GrammarManager grammarManager = GrammarManager.getInstance();
				grammarManager.addAnswer(answer, attemptAnswer, answer.equals(attemptAnswer));
				grammarManager.nextManager();
			} else {
				initScreen();
			}
		}
	}	
}