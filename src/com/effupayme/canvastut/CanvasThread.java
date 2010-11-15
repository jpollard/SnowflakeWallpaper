package com.effupayme.canvastut;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class CanvasThread extends Thread {

	private SurfaceHolder _surfaceHolder;
	private Panel _panel;
	private boolean _run = false;
	private ArrayList<Snowflakes> _flakes;
	
	public CanvasThread(SurfaceHolder surfaceHolder,Panel panel, ArrayList<Snowflakes> flakes){
		_surfaceHolder = surfaceHolder;
		_panel = panel;		
		_flakes = flakes;
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
				Thread.sleep(25);
				c = _surfaceHolder.lockCanvas(null);
				for(int i = 0; i < _flakes.size(); i++){
					_flakes.get(i).delta();
					_panel.invalidate();
				}
				synchronized (_surfaceHolder){
					_panel.onDraw(c);
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				if( c != null) {
					_surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
		
	}
}
