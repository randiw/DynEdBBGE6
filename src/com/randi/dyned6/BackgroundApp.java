package com.randi.dyned6;

import net.rim.device.api.system.Application;

import com.randi.dyned6.model.BackgroundRealtimeClock;

public class BackgroundApp extends Application {
	
	BackgroundRealtimeClock backgroundRealtimeClock;
	
	public BackgroundApp() {
		addClockListener();			
	}
	
	public void addClockListener(){
		backgroundRealtimeClock = new BackgroundRealtimeClock();
		addRealtimeClockListener(backgroundRealtimeClock);		
	}
	
	public void removeClockListener(){
		if(backgroundRealtimeClock != null) {
			removeRealtimeClockListener(backgroundRealtimeClock);		
			backgroundRealtimeClock = null;
		}
	}
}