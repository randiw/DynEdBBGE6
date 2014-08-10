package com.randi.dyned6.view.manager;

import java.util.Vector;

import com.randi.dyned6.model.Variables;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.container.VerticalFieldManager;

public abstract class ReviewManager extends VerticalFieldManager implements FieldChangeListener {
	
	private int correctCount;
	private int count;
	private boolean done;
	
	public ReviewManager(Vector vector) {
		super(USE_ALL_WIDTH | VERTICAL_SCROLL);
		XYEdges managerEdges = new XYEdges(10, 10, 10, 10);
		if(Variables.smallScreen()){
			managerEdges.set(5, 10, 5, 10);
		}
		VerticalFieldManager vfManager = new VerticalFieldManager(USE_ALL_WIDTH | FIELD_HCENTER);
		vfManager.setPadding(managerEdges);
		
		count = vector.size();
		correctCount = 0;
		for(int i = 0; i < vector.size(); i++){
			AnswerManager answer = (AnswerManager)vector.elementAt(i);
			vfManager.add(answer);
			if(answer.isCorrect()){
				correctCount++;
			}
		}
		done = correctCount == count;
		
		add(vfManager);
	}

	public int getCorrectCount() {
		return correctCount;
	}
	
	public int getCount(){
		return count;
	}
	
	public boolean isDone() {
		return done;
	}
	
	abstract public void fieldChanged(Field field, int context);	
}