package com.effupayme.snowflakewall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Service;

public class SnowflakeWallpaper extends Service {
	private Bundle bundle;
	private Panel panel;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        panel = new Panel(this);
        setContentView(panel);
        
    }
    
    @Override
    public void onPause(){
    	super.onPause();
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