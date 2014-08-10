package com.randi.dyned6.view;

import com.randi.dyned6.view.manager.ContentManager;

import net.rim.device.api.ui.container.VerticalFieldManager;

public class TestingScreen extends BasicScreen {

	public TestingScreen() {
		super("Testing");
		VerticalFieldManager vfManager = new VerticalFieldManager(VERTICAL_SCROLL | USE_ALL_WIDTH);
		ContentManager contentManager = new ContentManager(vfManager);
		add(contentManager);
	}
}