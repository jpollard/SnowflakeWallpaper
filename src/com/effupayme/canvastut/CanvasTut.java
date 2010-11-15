package com.effupayme.canvastut;

import android.app.Activity;
import android.os.Bundle;

public class CanvasTut extends Activity {
	private Bundle bundle;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	//onSaveInstanceState(bundle);
    	//this.setActive(false);
    }
    
  //  public void onResume(bundle){
    	
    //}
}