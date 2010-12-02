package com.effupayme.snowflakewall;

import java.util.ArrayList;

import android.app.WallpaperManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws falling snowflakes
 */

public class SnowflakeWallpaper extends WallpaperService {
	// Limit of snowflakes per snowflake type; 4 types * 4 snowflake = 16 total
	// Should keep memory usage at a minimal
	static int SNOWFLAKE_AMOUNT = 4;
	WallpaperManager wManager;
	Drawable drawWall;
	Rect wallBounds;
	
	// Draw all snowflakes off screen due to not knowing size of canvas at creation
	static int SNOW_START = -90;
	
	ArrayList<Snowflakes> snow = new ArrayList<Snowflakes>();
    private final Handler mHandler = new Handler();
    

    @Override
    public void onCreate() {
        super.onCreate();
    	//WallpaperManager to pull current wallpaper
    	wManager = WallpaperManager.getInstance(this);
    	drawWall = wManager.getFastDrawable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new SnowEngine();
    }

    class SnowEngine extends Engine {
    	private float mOffset;
    	private float mCenterX;
    	private float mCenterY;
    	
    	private void initSnow(){
    		
    		//Back snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(), 
    					R.drawable.snowflakeback),
    					SNOW_START,
    					SNOW_START,
    					((float)(Math.random() * 2) + 1)) // Fall speed initial setup, back slowest to front fastest potentially 
    			);
    		}
    		
    		//MidBack snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemid),
    					SNOW_START,
    					SNOW_START,
    					((float)(Math.random() * 4) + 1))
    			);
    		}
    		
    		// Mid snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemidfront),
    					SNOW_START,
    					SNOW_START,
    					((float)(Math.random() * 8) + 1))
    			);
    		}
    		
    		// Front snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(), 
    					R.drawable.snowflake),
    					SNOW_START, 
    					SNOW_START, 
    					((float)(Math.random() * 16) + 1))
    			);
    		}	
    	}

        private final Runnable mDrawSnow = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        
        private boolean mVisible;

        SnowEngine() {  
          if(snow.size() < 16){
        	initSnow();
          }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawSnow);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawSnow);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            mCenterX = width/2.0f;
            mCenterY = width/2.0f;
            
            drawFrame();
            
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawSnow);
        }
        
        @Override
        public void onTouchEvent(MotionEvent event){
        	super.onTouchEvent(event);
        }
        
        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset * 100;
            drawFrame();
        }

        /*
         * Update the screen with a new frame
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            int screenWidth = holder.getSurfaceFrame().width();
            int screenHeight = holder.getSurfaceFrame().height();
            
            /*
             * if the snow goes too low or too right, reset;
             */
            for(Snowflakes mySnow : snow){
            	if(mySnow.getX() > screenWidth + 15){
            		mySnow.setX(-65);
            	}
            	if(mySnow.getY() > screenHeight){
            		mySnow.setY(-69);
            	}
            }
            
            // Test if the array was just create; true - randomly populate snowflakes on screen
            if(snow.get(1).getX() < -70){
            	for(Snowflakes mySnow : snow){
            		mySnow.setX((int)(Math.random() * screenWidth));
            		mySnow.setY((int)(Math.random() * screenHeight));
            	}
            }
            
            
            // Change snowflake x & y
            for(Snowflakes mySnow : snow){
            	mySnow.delta();
            }

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                	
                    // call to draw new snow position
                    drawSnow(c);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawSnow);
            if (mVisible) {
                mHandler.postDelayed(mDrawSnow, 15);
            }
        }

        /*
         * Draw the snowflakes
         */
        void drawSnow(Canvas c) {
            c.save();
            c.translate(-(mCenterX + mOffset), 0);
            
            // Draw bg
            //********** add code to pull current bg and draw that instead of black. Maybe set this in config?
            if(drawWall == null){
            	c.drawColor(Color.BLACK);
            }else{
            	drawWall.draw(c);
            }
            c.translate((mCenterX + mOffset), 0);
            /*
             * draw up the snow
             */
            for(Snowflakes mySnow : snow){
            	c.drawBitmap(mySnow.getImage(), mySnow.getX() - mOffset, mySnow.getY(), null);
            }
            
            c.restore();
        }
    }
}