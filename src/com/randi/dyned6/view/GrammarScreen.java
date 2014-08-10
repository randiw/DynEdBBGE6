package com.randi.dyned6.view;

import com.randi.dyned6.model.GrammarManager;
import com.randi.dyned6.tools.FrameFieldListener;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class GrammarScreen extends BasicScreen implements FrameFieldListener {

	private Manager currentManager;
	private Manager nextManager;

	private GrammarManager grammarManager = GrammarManager.getInstance();

	public GrammarScreen(int unitIndex, int lessonIndex) {
		super("Grammar");
		currentManager = new VerticalFieldManager();
		add(currentManager);

		grammarManager.reset();
		grammarManager.setListener(this);
		grammarManager.setCurrentIndex(unitIndex, lessonIndex);
		grammarManager.nextManager();
	}

	public void onFrameChanged(Manager manager) {
		if(nextManager == null){
			nextManager = manager;
			replace(currentManager, nextManager);
			currentManager = null;
		} else {
			currentManager = manager;
			replace(nextManager, currentManager);
			nextManager = null;
		}
	}
	
	public void changeTitle(String title) {
		setTitle(title);
	}
}