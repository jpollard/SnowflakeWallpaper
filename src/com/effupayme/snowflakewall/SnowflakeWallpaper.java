package com.effupayme.snowflakewall;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SnowflakeWallpaper extends Activity {
	private Bundle bundle;
	private Panel panel;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    }
    
    
    public class panelThread implements Runnable{
    	
    	@Override
    	public void run(){
    		while(true){
    			//SnowflakeWallpaper.this.panelHandler.sendEmptyMessage(0);
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