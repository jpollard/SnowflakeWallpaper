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
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws falling snowflakes
 */

public class SnowflakeWallpaper extends WallpaperService {
	// Limit of snowflakes per snowflake type; 4 types * 4 snowflake = 16 total
	// Should keep memory usage at a minimal
	static int SNOWFLAKE_AMOUNT = 4;
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
    	WallpaperManager wManager = WallpaperManager.getInstance(this);
    	drawWall = wManager.getFastDrawable();
    	wallBounds = drawWall.copyBounds();
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
        	
          if(snow.size() < 16){
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
    		/*// Set speed to (current speed) / (array size - current spot in array)
    		// This makes sure that the flakes in the back move slower and that everything
    		// doesnt fall at the same speed.
    		for(int j = 0; j < snow.size(); j++){
    			snow.get(j).setSpeed((snow.get(j).getSpeed() + j)/(snow.size() - j));
    		}*/
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
            
            /*
             * if the snow goes too low or too right, reset;
             */
            for(int i = 0; i < snow.size(); i++){
            	if(snow.get(i).getX() > holder.getSurfaceFrame().width()){
            		snow.get(i).setX(-65);
            	}
            	if(snow.get(i).getY() > holder.getSurfaceFrame().height()){
            		snow.get(i).setY(-69);
            	}
            }
            
            // Test if the array was just create; true - randomly populate snowflakes on screen
            if(snow.get(1).getX() < -70){
            	for(int i = 0; i < snow.size(); i++){
            		snow.get(i).setX((int)(Math.random() * getSurfaceHolder().getSurfaceFrame().width() +1));
            		snow.get(i).setY((int)(Math.random() * getSurfaceHolder().getSurfaceFrame().height() + 1));
            	}
            }
            
            
            // Change snowflake x & y
            for(int i = 0; i < snow.size(); i++){
            	snow.get(i).delta();
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
                mHandler.postDelayed(mDrawSnow, 1000 / 100);
            }
        }

        /*
         * Draw the snowflakes
         */
        void drawSnow(Canvas c) {
            c.save();

            // Draw bg
            //********** add code to pull current bg and draw that instead of black. Maybe set this in config?
            if(drawWall == null){
            	c.drawColor(Color.BLACK);
            }else{
            	drawWall.copyBounds(wallBounds);
            	drawWall.draw(c);
            }
            
            /*
             * draw up the snow
             */
            for(int i = 0; i < snow.size(); i++){
            	c.drawBitmap(snow.get(i).getImage(), snow.get(i).getX(), snow.get(i).getY(), null);
            }
            
            c.restore();
        }
    }
}