package com.effupayme.snowflakewall;

import java.util.ArrayList;
import android.R;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

        private final Paint mPaint = new Paint();
        private float mOffset;
        private long mStartTime;
        private float mCenterX;
        private float mCenterY;
        private ArrayList<Snowflakes> snow;

        private final Runnable mDrawSnow = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;

        SnowEngine() {
        	
            // Create the back snowflakes
            for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
            	snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(), 
    					R.drawable.snowflakeback),
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().width() +1),
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().height() + 1),
    					(int)(Math.random() * 5) + 1)
    			);
            }
            //MidBack snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemid),
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().width() +1),
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().height() + 1),
    					(int)(Math.random() * 5) + 1)
    			);
    		}
    		
    		// Mid snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(),
    					R.drawable.snowflakemidfront),
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().width() +1),
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().height() + 1),
    					(int)(Math.random() * 5) + 1)
    			);
    		}
    		// Front snowflakes
    		for(int i = 0; i < SNOWFLAKE_AMOUNT; i++){
    			snow.add(new Snowflakes(
    					BitmapFactory.decodeResource(getResources(), 
    					R.drawable.snowflake),
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().width() +1), 
    					(int)(Math.random() * getSurfaceHolder().getSurfaceFrame().height() + 1), 
    					(int)(Math.random() * 5) + 1)
    			);
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
            for(int i = 0; i < snow.size(); i++){
            	c.drawBitmap(snow.get(i).getImage(), snow.get(i).getX(), snow.get(i).getY(), null);
            }
            c.restore();
        }
    }
}