package com.effupayme.snowflakewall;

import java.util.ArrayList;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws falling snowflakes
 */

public class SnowflakeWallpaper extends WallpaperService {
	static int SNOWFLAKE_AMOUNT = 4;
	static int SNOW_START = -90;
	ArrayList<Snowflakes> snow = new ArrayList<Snowflakes>();
    private final Handler mHandler = new Handler();
    

    @Override
    public void onCreate() {
        super.onCreate();
      
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

        private final Runnable mDrawSnow = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        
        private boolean mVisible;

        SnowEngine() {
        	//SurfaceHolder holder = getSurfaceHolder();
        	if(snow.size() < 20){
        	//Back snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(), 
    					R.drawable.snowflakeback),
    					SNOW_START,
    					SNOW_START,
    					((float)(Math.random() * 2) + 1))
    			);
    		}
    		
    		//MidBack snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemid),
    					SNOW_START,
    					SNOW_START,
    					((float)(Math.random() * 4) + 1)
    			));
    		}
    		
    		// Mid snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemidfront),
    					SNOW_START,
    					SNOW_START,
    					((float)(Math.random() * 6) + 1))
    			);
    		}
    		// Front snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(), 
    					R.drawable.snowflake),
    					SNOW_START, 
    					SNOW_START, 
    					((float)(Math.random() * 10) + 1))
    			);
    		}
    		for(int i = 0; i < snow.size(); i++){
    			snow.get(i).setSpeed(snow.get(i).getSpeed()/(snow.size() - i));
    		}
        	}
            //mStartTime = SystemClock.elapsedRealtime();
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

        /*
         * Update the screen with a new frame
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            for(int i = 0; i < snow.size(); i++){
            	snow.get(i).delta();
            }

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    // draw something
                    drawSnow(c);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawSnow);
            if (mVisible) {
                mHandler.postDelayed(mDrawSnow, 1000 / 75);
            }
        }

        /*
         * Draw the snowflakes
         */
        void drawSnow(Canvas c) {
            c.save();
            if(snow.get(1).getX() < -70){
            	for(int i = 0; i < snow.size(); i++){
            		snow.get(i).setX((int)(Math.random() * getSurfaceHolder().getSurfaceFrame().width() +1));
            		snow.get(i).setY((int)(Math.random() * getSurfaceHolder().getSurfaceFrame().height() + 1));
            	}
            }
            /*
             * if the snow goes too low or too right, reset;
             */
            for(int i = 0; i < snow.size(); i++){
            	if(snow.get(i).getX() > c.getWidth()){
            		snow.get(i).setX(-65);
            	}
            	if(snow.get(i).getY() > c.getHeight()){
            		snow.get(i).setY(-69);
            	}
            }
            
            /*
             * draw up the snow
             */
            c.drawColor(Color.BLACK);
            for(int i = 0; i < snow.size(); i++){
            	c.drawBitmap(snow.get(i).getImage(), snow.get(i).getX(), snow.get(i).getY(), null);
            	//snow.get(i).delta();
            }
            
            c.restore();
        }
    }
}