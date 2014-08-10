package com.randi.dyned6.view;

import net.rim.blackberry.api.messagelist.ApplicationIndicatorRegistry;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.awan.dyned6.general.Constans;
import com.awan.dyned6.general.GeneralFunction;
import com.randi.dyned6.MainApp;
import com.randi.dyned6.model.AudioDownload;
import com.randi.dyned6.model.AvatarDownload;
import com.randi.dyned6.model.Resources;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.model.persistable.NotificationManager;
import com.randi.dyned6.view.field.UnitMenu;
import com.randi.dyned6.view.field.UnitMenuField;
import com.randi.dyned6.view.manager.ContentManager;

public class HomeMenuScreen extends BasicScreen {

	private AudioDownload audioDownload = AudioDownload.getInstance();
	private AvatarDownload avatarDownload = AvatarDownload.getInstance();
	private EducationManager educationManager = EducationManager.getInstance();
	private VerticalFieldManager vfManager;
	
	public HomeMenuScreen() {
		super(Variables.APP_NAME);
		vfManager = new VerticalFieldManager(VERTICAL_SCROLL | USE_ALL_WIDTH);
		for (int i = 0; i < Constans.mainScreenList.length; i++) {
			String unitId = Constans.mainScreenList[i];
			UnitMenu unitMenuField = new UnitMenu(unitId);
			unitMenuField.setChangeListener(this);
			vfManager.add(unitMenuField);
		}
		
		ContentManager contentManager = new ContentManager(vfManager);		
		add(contentManager);
	}

	protected void onUiEngineAttached(boolean attached) {
		if(attached){
			if (!GeneralFunction.checkIsFileExists(Resources.PATH) || !GeneralFunction.checkIsFileExists(Resources.PATH_AUDIO) || !GeneralFunction.checkIsFileExists(Resources.PATH_AUDIO_LEVEL)) {
				UiApplication.getUiApplication().pushScreen(new ProgressScreen());
			} else {
				audioDownload.start();
				avatarDownload.start();

				NotificationManager notificationManager = NotificationManager.getInstance();
				if(notificationManager.isNewItem()){
					try {
						ApplicationIndicatorRegistry indicatorRegistry = ApplicationIndicatorRegistry.getInstance();
						indicatorRegistry.unregister();
					} catch(Exception e){
					}
					notificationManager.setNewItem(false);
				}
			}
		}
		super.onUiEngineAttached(attached);
	}

	protected void sublayout(int width, int height) {
		super.sublayout(width, height);
	}
	
	public void fieldChanged(Field field, int context) {
		if(field instanceof UnitMenuField || field instanceof LabelField){
			int index = vfManager.getFieldWithFocusIndex();
			if(educationManager.contains(Constans.mainScreenList[index])){
				UiApplication.getUiApplication().pushScreen(new LessonMenuScreen(index));				
			}
		}
	}
	
	public void setMenuItems() {
		addMenuItem(new MenuItem("Logout", 0, 10) {
			public void run() {
				MainApp.logout();
			}
		});
		super.setMenuItems();
	}
}