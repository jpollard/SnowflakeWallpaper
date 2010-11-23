package com.effupayme.snowflakewall;

import java.util.ArrayList;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws falling snowflakes
 */

public class SnowflakeWallpaper extends WallpaperService {
	static int SNOWFLAKE_AMOUNT = 4;
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
        private long mStartTime;

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
    					-64,
    					-64,
    					((float)(Math.random() * 5) + 1) / snow.size())
    			);
    		}
    		
    		//MidBack snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemid),
    					-64,
    					-64,
    					((float)(Math.random() * 5) + 1) / snow.size())
    			);
    		}
    		
    		// Mid snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemidfront),
    					-64,
    					-64,
    					((float)(Math.random() * 5) + 1) / snow.size())
    			);
    		}
    		// Front snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(), 
    					R.drawable.snowflake),
    					-64, 
    					-64, 
    					((float)(Math.random() * 5) + 1) / snow.size())
    			);
    		}
        	}
            mStartTime = SystemClock.elapsedRealtime();
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
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
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
                mHandler.postDelayed(mDrawSnow, 1000 / 25);
            }
        }

        /*
         * Draw the snowflakes
         */
        void drawSnow(Canvas c) {
            c.save();
            if(snow.get(1).getX() < -48){
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
            		snow.get(i).setX(-48);
            	}
            	if(snow.get(i).getY() > c.getHeight()){
            		snow.get(i).setY(-48);
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