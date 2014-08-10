package com.randi.dyned6.view;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.randi.dyned6.model.ConfigurationFactory;
import com.randi.dyned6.model.ConfigurationListener;
import com.randi.dyned6.model.Variables;
import com.randi.dyned6.model.connector.HistoryConnector;
import com.randi.dyned6.model.persistable.SessionManager;
import com.randi.dyned6.tools.FontSetting;

public class SplashScreen extends AbstractScreen implements ConfigurationListener {

	private Timer waitingTime;
	private FontSetting fontSetting = FontSetting.getInstance();
	
	public SplashScreen() {
		super(USE_ALL_HEIGHT | USE_ALL_WIDTH);
	}

	public void setBackground() {
		Background bg = BackgroundFactory.createBitmapBackground(
				Bitmap.getBitmapResource("bg_small.png"),
				Background.POSITION_X_LEFT, Background.POSITION_Y_TOP,
				Background.REPEAT_BOTH);
		getMainManager().setBackground(bg);
	}

	protected void paint(Graphics g) {
		super.paint(g);
		fontSetting.setPoint(Font.PLAIN, 11);
		int conLength = fontSetting.getFont().getAdvance(Variables.SPLASH_TITLE);
		g.setFont(fontSetting.getFont());
		g.setColor(0x10498c);
		g.drawText(Variables.SPLASH_TITLE, (getWidth() - conLength)/2, getHeight() / 2 - (10 + fontSetting.getFont().getHeight()));
		
		fontSetting.setPoint(Font.PLAIN, 8);
		int genLength = fontSetting.getFont().getAdvance(Variables.SPLASH_NAME);
		g.setFont(fontSetting.getFont());
		g.setColor(0xadc752);
		g.drawText(Variables.SPLASH_NAME, (getWidth() - genLength)/2, getHeight() / 2);
		
		fontSetting.setPoint(Font.PLAIN, 5);
		int urlLength = fontSetting.getFont().getAdvance(Variables.SPLASH_SUB);
		g.setFont(fontSetting.getFont());
		g.setColor(Color.GRAY);
		g.drawText(Variables.SPLASH_SUB, (getWidth() - urlLength)/2, getHeight() - 20 - fontSetting.getFont().getHeight());
	}
	
	protected void onDisplay() {
		if (waitingTime == null) {
			waitingTime = new Timer();
			waitingTime.schedule(new CountDown(), 300);
		}
	}

	public void fieldChanged(Field field, int context) {
	}

	public boolean keyChar(char key, int status, int time) {
		if (key == Characters.ESCAPE) {
			System.exit(0);
		}
		return super.keyChar(key, status, time);
	}

	public void onError(String message) {
		System.out.println("=======> config load failed" + message);
		setStatus(new LabelField(" = config load failed = ", FIELD_HCENTER));
	}

	public void onDone() {
		synchronized (UiApplication.getEventLock()) {
			SessionManager sessionManager = SessionManager.getInstance();
						
			if(sessionManager.getId() == null) {
				System.out.println("no id");
				UiApplication.getUiApplication().popScreen(this);			
				UiApplication.getUiApplication().pushScreen(new LoginScreen());
			} else {
				System.out.println("Session ID: " + sessionManager.getId());
				System.out.println("Session AppKey: " + sessionManager.getApp_key());
				
				URLEncodedPostData postData = new URLEncodedPostData(null, false);
				postData.append("conversation", Variables.CONVERSATION_CODE);
				postData.append("app_key", sessionManager.getApp_key());
				
				HistoryConnector historyConnector = new HistoryConnector(postData);
				historyConnector.start();
				UiApplication.getUiApplication().pushScreen(new LoadingScreen(historyConnector, "Fetching history.."));
			}
		}			
	}

	private class CountDown extends TimerTask {
		public void run() {
			DismissThread dThread = new DismissThread();
			UiApplication.getUiApplication().invokeLater(dThread);
		}
	}

	private class DismissThread implements Runnable {
		public void run() {
			dismiss();
		}
	}

	private void dismiss() {
		new Thread(new Runnable() {

			public void run() {
				ConfigurationFactory cfFactory = new ConfigurationFactory(SplashScreen.this);
				cfFactory.parseXML();
			}
		}).start();
	}
}