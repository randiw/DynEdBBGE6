package com.randi.dyned6.view.manager;

import java.io.IOException;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

import com.randi.dyned6.model.Variables;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class PlayAudioManager extends Manager implements FieldChangeListener, PlayerListener {

	private Player player;
	private VolumeControl volumeControl;
	
	private PlayPauseButton button;
	private GaugeField gaugeField;
	
	private long duration;
	private long currentTime;
	private boolean hasEnd;

	private PlayAudioListener myListener;
	
	public PlayAudioManager(String fileAudio) {
		super(USE_ALL_WIDTH);

		XYEdges buttonEdges = new XYEdges(5, 10, 5, 5);
		if(Variables.smallScreen()){
			buttonEdges.set(3, 7, 3, 3);
		}
		
		button = new PlayPauseButton();
		button.setChangeListener(this);
		button.setPadding(buttonEdges);
		add(button);
		
		gaugeField = new GaugeField("", 1, 100, 1, GaugeField.NO_TEXT);
		add(gaugeField);
		
		try {
			player = javax.microedition.media.Manager.createPlayer(fileAudio);
			player.addPlayerListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MediaException e) {
			e.printStackTrace();
		}
		
		setPadding(2, 5, 2, 5);
		setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
	}

	public void setPlayAudioListener(PlayAudioListener myListener){
		this.myListener = myListener;
	}
	
	public void start(){
		try {
			if(hasEnd){
				gaugeField.setValue(1);
				hasEnd = false;
			}
			player.start();
			button.setPlay(true);
			duration = player.getDuration();
			volumeControl = (VolumeControl) player.getControl("VolumeControl");
			if(myListener != null) {
				myListener.onStartAudio();
			}
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long getDuration() {
		return duration;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void pause(){
		try {
			button.setPlay(false);
			player.stop();
			myListener.onPauseAudio();
		} catch (MediaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onUndisplay() {
		if(player != null){
			if(isPlay()){
				pause();
			}
			player.close();
		}
		player = null;
		super.onUndisplay();
	}
	
	protected void sublayout(int width, int height) {
		layoutChild(button, width, height);
		layoutChild(gaugeField, width - button.getWidth(), height);
		
		int maxHeight = button.getHeight() > gaugeField.getHeight() ? button.getHeight() : gaugeField.getHeight();
		setPositionChild(button, 0, (maxHeight - button.getHeight())/2);
		setPositionChild(gaugeField, button.getWidth(), (maxHeight - gaugeField.getHeight())/2);

		setExtent(width, maxHeight);
	}

	public boolean isPlay(){
		return button.isPlay;
	}
	
	private class PlayPauseButton extends Field {

		private Bitmap play;
		private Bitmap playFocus;
		private Bitmap pause;
		private Bitmap pauseFocus;
		
		private boolean isPlay;
		
		public PlayPauseButton() {
			super(FOCUSABLE);
			if(Variables.bigTouchScreen()){
				play = Bitmap.getBitmapResource("button_play_normal_large.png");
				playFocus = Bitmap.getBitmapResource("button_play_focus_large.png");
				pause = Bitmap.getBitmapResource("button_pause_normal_large.png");
				pauseFocus = Bitmap.getBitmapResource("button_pause_focus_large.png");								
			} else {
				play = Bitmap.getBitmapResource("button_play_normal.png");
				playFocus = Bitmap.getBitmapResource("button_play_focus.png");
				pause = Bitmap.getBitmapResource("button_pause_normal.png");
				pauseFocus = Bitmap.getBitmapResource("button_pause_focus.png");				
			}
		}

		protected void layout(int width, int height) {
			setExtent(play.getWidth(), play.getHeight());
		}

		protected void paint(Graphics g) {
			boolean focus  = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
			if(isPlay){
				if(focus){
					g.drawBitmap(0, 0, pauseFocus.getWidth(), pauseFocus.getHeight(), pauseFocus, 0, 0);

				} else {
					g.drawBitmap(0, 0, pause.getWidth(), pause.getHeight(), pause, 0, 0);					
				}				
			} else {
				if(focus){
					g.drawBitmap(0, 0, playFocus.getWidth(), playFocus.getHeight(), playFocus, 0, 0);
				} else {
					g.drawBitmap(0, 0, play.getWidth(), play.getHeight(), play, 0, 0);					
				}
			}
		}
				
		public void setPlay(boolean isPlay) {
			this.isPlay = isPlay;
		}
		
		public void clickButton() {
			fieldChangeNotify(0);
		}

		protected boolean invokeAction(int action) {
			switch (action) {
			case ACTION_INVOKE: {
				clickButton();
				return true;
			}
			}
			return super.invokeAction(action);
		}

		protected boolean keyChar(char character, int status, int time) {
			if (character == Characters.ENTER) {
				clickButton();
				return true;
			}
			return super.keyChar(character, status, time);
		}

		protected boolean navigationClick(int status, int time) {
			clickButton();
			return true;
		}

		protected boolean trackwheelClick(int status, int time) {
			clickButton();
			return true;
		}
		
		public void setDirty(boolean dirty) {
		}

		public void setMuddy(boolean muddy) {
		}
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof PlayPauseButton){
			if(button.isPlay){
				pause();
			} else {
				start();
			}
			invalidate();
		}
	}

	public void playerUpdate(Player player, String event, Object eventData) {
		if(event.equals(PlayerListener.END_OF_MEDIA)){
			button.setPlay(false);
			gaugeField.setValue(100);
			hasEnd = true;
			if(myListener != null) {
				myListener.onFinishAudio();
			}
			invalidate();
		} else if(event.equals("com.rim.timeUpdate")){
			currentTime = player.getMediaTime();
			int now = (int) (currentTime * 100 / duration);
			gaugeField.setValue(now);
			invalidate();
		}
	}
	
	protected boolean keyDown(int keycode, int time) {
		int key = Keypad.key(keycode);		
		if (volumeControl != null) {
			if (key == Keypad.KEY_VOLUME_UP){
				volumeControl.setLevel(volumeControl.getLevel() + 10);					
			} else if (key == Keypad.KEY_VOLUME_DOWN){
				volumeControl.setLevel(volumeControl.getLevel() - 10);						
			}	
		}
		return super.keyDown(keycode, time);		
	}
}