package com.randi.dyned6.view;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.randi.dyned6.model.ComprehensionManager;
import com.randi.dyned6.tools.FrameFieldListener;

public class ComprehensionScreen extends BasicScreen implements FrameFieldListener {

	private Manager currentManager;
	private Manager nextManager;
		
	private ComprehensionManager comprehensionManager = ComprehensionManager.getInstance();
	
	public ComprehensionScreen(int unitIndex, int lessonIndex) {
		super("Comprehension");
		currentManager = new VerticalFieldManager();
		add(currentManager);

		comprehensionManager.reset();
		comprehensionManager.setListener(this);
		comprehensionManager.setCurrentIndex(unitIndex, lessonIndex);
		comprehensionManager.nextManager();			
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
		invalidate();
	}

	public void changeTitle(String title) {
		setTitle(title);
	}

	protected void actionKeyEscape() {
	}
}