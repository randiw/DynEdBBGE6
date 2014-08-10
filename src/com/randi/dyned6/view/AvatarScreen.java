package com.randi.dyned6.view;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.VolumeControl;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;

import com.randi.dyned6.model.Resources;
import com.randi.dyned6.model.persistable.AvatarManager;

public class AvatarScreen extends AbstractScreen implements PlayerListener{
	
	private Player player;
	private VideoControl videoControl;
	private VolumeControl volumeControl;
	private Field videoField;
	
	private AbstractScreen nextScreen;
	private int unitIndex;
	private int lessonIndex;
	private boolean hasIndex;
	
	public AvatarScreen(String videoFile) {
		try {
			if(videoFile.startsWith(Resources.PATH)){
				player = Manager.createPlayer(videoFile);				
			} else {
				InputStream is = (InputStream)getClass().getResourceAsStream(videoFile);
				player = Manager.createPlayer(is, "video/3gpp");
			}
			player.prefetch();
			player.realize();
			videoControl = (VideoControl)player.getControl("VideoControl");
			if(videoControl != null) {
				videoField = (Field) videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, "net.rim.device.api.ui.Field");
				videoControl.setDisplayFullScreen(true);
				videoControl.setVisible(true);
				synchronized (UiApplication.getEventLock()) {
					add(videoField);
				}
			}
			player.addPlayerListener(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AvatarScreen(String videoFile, int unitIndex, int lessonIndex) {
		this(videoFile);
		setIndex(unitIndex, lessonIndex);
	}
	
	protected void onUiEngineAttached(boolean attached) {
		if(attached){
			try {
				player.start();
				volumeControl = (VolumeControl) player.getControl("VolumeControl");
			} catch (MediaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onUiEngineAttached(attached);
	}
	
	public void setNextScreen(AbstractScreen screen){
		nextScreen = screen;
	}
	
	public void setIndex(int unitIndex, int lessonIndex){
		this.unitIndex = unitIndex;
		this.lessonIndex = lessonIndex;
		hasIndex = true;
	}
	
	public void setBackground() {
	}
 
	public void fieldChanged(Field field, int context) {
	}

	public void playerUpdate(Player player, String event, Object eventData) {
		if(event.equals(PlayerListener.END_OF_MEDIA)){
			if(hasIndex){
				AvatarManager avatarManager = AvatarManager.getInstance();
				avatarManager.setFilePlayed(unitIndex, lessonIndex);
			}
			synchronized (UiApplication.getEventLock()) {
				UiApplication.getUiApplication().popScreen(this);
				if(nextScreen != null){
					UiApplication.getUiApplication().pushScreen(nextScreen);
				}
			}
		}
	}

	protected boolean keyDown(int keycode, int time) {
		int key = Keypad.key(keycode);
		if(volumeControl != null){
			if(key == Keypad.KEY_VOLUME_UP){
				volumeControl.setLevel(volumeControl.getLevel() + 10);
			} else if(key == Keypad.KEY_VOLUME_DOWN){
				volumeControl.setLevel(volumeControl.getLevel() - 10);				
			}
		}
		return super.keyDown(keycode, time);
	}
}