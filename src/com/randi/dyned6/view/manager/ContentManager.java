package com.randi.dyned6.view.manager;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.randi.dyned6.model.ContentListener;
import com.randi.dyned6.model.persistable.NotificationManager;

public class ContentManager extends Manager implements ContentListener {
	
	private Manager content;
	private Manager currentManager;
	private CountDownManager countDown;
	
	public ContentManager(Manager manager) {
		super(USE_ALL_WIDTH);
		content = manager;
		add(content);

		currentManager = new VerticalFieldManager(USE_ALL_WIDTH);

		NotificationManager notificationManager = NotificationManager.getInstance();
		if(notificationManager.isCounting()){
			countDown = new CountDownManager();
			countDown.setListener(this);			
			currentManager.add(countDown);
		}

		add(currentManager);
	}

	protected void sublayout(int width, int height) {
		layoutChild(currentManager, width, height);
		layoutChild(content, width, height - currentManager.getHeight());
		
		setPositionChild(currentManager, 0, height - currentManager.getHeight());
		setPositionChild(content, 0, 0);
		
		setExtent(width, height);
	}

	public void onFinishTask(String label) {
		if(countDown != null){
			currentManager.delete(countDown);
		}
		invalidate();
	}

	public void onErrorTask(String label) {
		// TODO Auto-generated method stub
		
	}	
}