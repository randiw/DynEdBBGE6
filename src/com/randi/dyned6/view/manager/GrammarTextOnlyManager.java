package com.randi.dyned6.view.manager;

import com.randi.dyned6.model.GrammarItem;
import com.randi.dyned6.model.GrammarManager;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.tools.FontSetting;
import com.randi.dyned6.view.field.ChoiceField;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class GrammarTextOnlyManager extends VerticalFieldManager implements FieldChangeListener {

	private FontSetting fontSetting = FontSetting.getInstance();
	
	private String answer;
	private String question;

	public GrammarTextOnlyManager(GrammarItem item) {
		super(USE_ALL_WIDTH | VERTICAL_SCROLL);
		answer = item.getAnswer();
		question = item.getQuestion();
		
		XYEdges fieldEdges = new XYEdges(20, 20, 20, 20);
		XYEdges boxEdges = new XYEdges(20, 60, 20, 60);
		XYEdges choiceMargin = new XYEdges(10, 0, 0, 0);
		if(Variables.smallScreen()){
			fieldEdges.set(15, 15, 10, 15);
			boxEdges.set(0, 60, 0, 60);
		}
		if(Variables.bigTouchScreen()){
			choiceMargin.set(0, 0, 15, 0);
		}

		fontSetting.setPoint(Font.PLAIN, 1);
		LabelField labelField = new LabelField("", FOCUSABLE);
		labelField.setFont(fontSetting.getFont());
		add(labelField);

		fontSetting.setPoint(Font.PLAIN, 8);
		LabelField questionField = new LabelField(item.getQuestion(), USE_ALL_WIDTH | LabelField.HCENTER | LabelField.VCENTER){
			protected void paint(Graphics g) {
				g.setColor(0x5f5f5d);
				super.paint(g);
			};
		};
		questionField.setFont(fontSetting.getFont());
		questionField.setBackground(BackgroundFactory.createSolidBackground(0xdedddb));
		questionField.setPadding(fieldEdges);
		add(questionField);

		VerticalFieldManager vfManager = new VerticalFieldManager(USE_ALL_WIDTH | NO_VERTICAL_SCROLL);
		vfManager.setPadding(boxEdges);
		add(vfManager);

		String[] options = item.getRandomOptions();
		for(int i = 0; i < options.length; i++){
			ChoiceField choiceField = new ChoiceField(options[i]);
			choiceField.setPadding(5, 10, 5, 10);
			choiceField.setMargin(choiceMargin);
			choiceField.setChangeListener(this);
			vfManager.add(choiceField);
		}
	}
	
	public void fieldChanged(Field field, int context) {
		if(field instanceof ChoiceField){
			GrammarManager manager = GrammarManager.getInstance();
			
			ChoiceField choiceField = (ChoiceField)field;
			String label = choiceField.getText();
			manager.addAnswer(question, label, answer.equals(label));
			manager.nextManager();
		}
	}
}