package com.effupayme.canvastut;



import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Panel extends SurfaceView implements SurfaceHolder.Callback{
	private static int NUM_OF_FLAKES = 4;
	CanvasThread canvasThrd;
	ArrayList<Snowflakes> snowflakes;
	
	public Panel(Context context, AttributeSet attrs){
		super(context, attrs);
		
		getHolder().addCallback(this);  //getHolder returns SurfaceHolder that provides control and access to SurfaceView
		canvasThrd = new CanvasThread(getHolder(), this);
		setFocusable(true);		
	}
	
	@Override	
	public void surfaceChanged(SurfaceHolder surface, int format, int height, int width){
		// Auto generated with SurfaceHolder
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		// Auto generated with SurfaceHolder
		Boolean retry = true;
		canvasThrd.setRunning(false);
		while(retry){
			try{
				canvasThrd.join();
				retry = false;
			} catch(Exception e) { }
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		// Auto generated with SurfaceHolder
		snowflakes = new ArrayList<Snowflakes>();
		
		//Back snowflakes
		for(int i = 0; i < NUM_OF_FLAKES; i++){
			snowflakes.add(new Snowflakes(BitmapFactory.decodeResource(getResources(), R.drawable.snowflakeback),(int)(Math.random() * holder.getSurfaceFrame().width() +1), (int)(Math.random() * holder.getSurfaceFrame().height() + 1), (int)(Math.random() * 5) + 1));
		}
		
		//MidBack snowflakes
		for(int i = 0; i < NUM_OF_FLAKES; i++){
			snowflakes.add(new Snowflakes(BitmapFactory.decodeResource(getResources(), R.drawable.snowflakemid),(int)(Math.random() * holder.getSurfaceFrame().width() +1), (int)(Math.random() * holder.getSurfaceFrame().height() + 1), (int)(Math.random() * 5) + 1));
		}
		
		// Mid snowflakes
		for(int i = 0; i < NUM_OF_FLAKES; i++){
			snowflakes.add(new Snowflakes(BitmapFactory.decodeResource(getResources(), R.drawable.snowflakemidfront),(int)(Math.random() * holder.getSurfaceFrame().width() +1), (int)(Math.random() * holder.getSurfaceFrame().height() + 1), (int)(Math.random() * 5) + 1));
		}
		// Front snowflakes
		for(int i = 0; i < NUM_OF_FLAKES; i++){
			snowflakes.add(new Snowflakes(BitmapFactory.decodeResource(getResources(), R.drawable.snowflake),(int)(Math.random() * holder.getSurfaceFrame().width() +1), (int)(Math.random() * holder.getSurfaceFrame().height() + 1), (int)(Math.random() * 5) + 1));
		}
		
		
		canvasThrd.setRunning(true);
		canvasThrd.start();
		
	}
	
	public void onDraw(Canvas canvas){
		Paint paint = new Paint();
		
		for(int i = 0; i < snowflakes.size(); i++){
			snowflakes.get(i).setY(snowflakes.get(i).getY() + 1);
			if(snowflakes.get(i).getY() > canvas.getHeight()){
				snowflakes.get(i).setY(-48);
			}
			if(snowflakes.get(i).getX() > canvas.getWidth()){
				snowflakes.get(i).setX(-48);
			}
		}
		
		canvas.drawColor(Color.BLACK);
		
		for(int i = 0; i < snowflakes.size(); i++){
			snowflakes.get(i).delta();
			canvas.drawBitmap(snowflakes.get(i).getImage(), snowflakes.get(i).getX(), snowflakes.get(i).getY(), null);
		}
		
	}
}
