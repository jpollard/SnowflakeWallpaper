package com.effupayme.snowflakewall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.wallpaper.WallpaperService;

public class SnowflakeWallpaper extends WallpaperService {
	private Bundle bundle;
	private Panel panel;
	
	/** onCreateEngine autocreated from extending WallpaperService */
	@Override
	public Engine onCreateEngine(){
		return new snowEngine();
	}
	
	public class snowEngine extends Engine{
		
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate() {
    	
    }    
    
    public class panelThread implements Runnable{
    	
    	@Override
    	public void run(){
    		while(true){
    			SnowflakeWallpaper.this.panelHandler.sendEmptyMessage(0);
    		}
    	}
    }
    
    public Handler panelHandler = new Handler(){
    	/** Gets called with every message */
    	public void handleMessage(Message msg){
    		panel.update();
    		panel.invalidate();
    		super.handleMessage(msg);
    	}
    	
    };
}