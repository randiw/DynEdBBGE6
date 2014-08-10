package com.randi.dyned6.view;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.awan.dyned6.general.Constans;
import com.awan.dyned6.general.GeneralFunction;
import com.randi.dyned6.model.LessonItem;
import com.randi.dyned6.model.Resources;
import com.randi.dyned6.model.persistable.AvatarManager;
import com.randi.dyned6.model.persistable.EducationManager;
import com.randi.dyned6.view.field.LessonListField;

public class LessonMenuScreen extends BasicScreen {

	private int unitIndex;	
	private VerticalFieldManager vfManager;
	
	public LessonMenuScreen(int index) {
		super(Constans.mainScreenList[index]);
		unitIndex = index;
		
		EducationManager educationManager = EducationManager.getInstance();
		String unitId = educationManager.getUnitId(index);
		
		String[] lessonList = Constans.subCategory[index];
		if(educationManager.justStartedUnit(unitId)){
			for(int i = 0; i < lessonList.length; i++){
				LessonItem lessonItem = educationManager.getLesson(unitId, Integer.toString(i));
				if(i == 0){
					lessonItem.unlock();
				}
				educationManager.commitLesson(unitId, lessonItem.getId(), lessonItem);
			}
		}
		
		vfManager = new VerticalFieldManager(USE_ALL_WIDTH | VERTICAL_SCROLL);
		for(int i = 0; i < lessonList.length; i++) {
			String imageFile = Constans.imageList[index][i];
			if(imageFile != null) {
				String title = lessonList[i];
				LessonListField lessonField = new LessonListField(title, imageFile);
				LessonItem lessonItem = educationManager.getLesson(unitId, Integer.toString(i));
				lessonField.setLocked(!lessonItem.isUnlock());
				lessonField.setChangeListener(this);
				vfManager.add(lessonField);				
			}
		}
		add(vfManager);
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof LessonListField) {
			LessonListField lessonField = (LessonListField)field;
			if(!lessonField.isLocked()){
				int selectedIndex = vfManager.getFieldWithFocusIndex();

				AvatarManager avatarManager = AvatarManager.getInstance();
				if(avatarManager.checkPlay(unitIndex, selectedIndex, AvatarManager.BEFORE)){
					String avatarLocation = avatarManager.getFileLocation(unitIndex, selectedIndex);
					if(GeneralFunction.checkIsFileExists(avatarLocation)){
						AvatarScreen avatarScreen = new AvatarScreen(avatarLocation, unitIndex, selectedIndex);
						ListeningScreen listeningScreen = new ListeningScreen(unitIndex, selectedIndex);
						avatarScreen.setNextScreen(listeningScreen);
						UiApplication.getUiApplication().pushScreen(avatarScreen);						
					} else {
						gotoListen(selectedIndex);
					}
				} else {
					gotoListen(selectedIndex);				
				}				
			}
		}
	}
	
	private void gotoListen(int lessonIndex){
		String audioFile = Constans.audioList[unitIndex][lessonIndex];
		String audioLocation = Resources.PATH_AUDIO_DIALOGUES + audioFile;
		if(GeneralFunction.checkIsFileExists(audioLocation)){
			ListeningScreen listeningScreen = new ListeningScreen(unitIndex, lessonIndex);
			UiApplication.getUiApplication().pushScreen(listeningScreen);						
		} else {
			if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_NONE)){
				UiApplication.getUiApplication().invokeLater(new Runnable() {				
					public void run() {
						Dialog.alert("Please enable internet connection to download audio material");
					}
				});
			} else {
				ListeningScreen listeningScreen = new ListeningScreen(unitIndex, lessonIndex);
				UiApplication.getUiApplication().pushScreen(listeningScreen);			
			}
		}
	}
}