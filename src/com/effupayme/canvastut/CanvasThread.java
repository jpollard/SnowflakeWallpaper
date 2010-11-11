package com.effupayme.canvastut;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

public class CanvasThread extends Thread {

	private SurfaceHolder _surfaceHolder;
	private Panel _panel;
	private boolean _run = false;
	
	public CanvasThread(SurfaceHolder surfaceHolder,Panel panel){
		_surfaceHolder = surfaceHolder;
		_panel = panel;		
	}
	
	public void setRunning(boolean run){
		_run = run;
	}
	
	@Override
	public void run(){
		Canvas c;
		while(_run){
			c = null;
			try{
				c = _surfaceHolder.lockCanvas(null);
				synchronized (_surfaceHolder){
					_panel.onDraw(c);
				}
			} finally {
				if( c != null) {
					_surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
		
	}
}
