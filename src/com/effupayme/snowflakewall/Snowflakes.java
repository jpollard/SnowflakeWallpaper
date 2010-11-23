package com.effupayme.snowflakewall;

import android.graphics.Bitmap;

/**
 * 
 * @author jwp
 * Bitmap snowflake:	pass in the bitmap to be controlled
 * float x:				set the initial x postion
 * float y:				set the initial y postion
 * float speed:			set the speed of the change in (x,y) postion
 * 
 * void delta(): 		change x & y
 * 
 */
public class Snowflakes {
	private Bitmap snowflake;
	private float x;
	private float y;
	private float speed;
	
	public Snowflakes(Bitmap _snowflake, float _x, float _y, float _speed){
		snowflake = _snowflake;
		x = _x;
		y = _y;
		speed = _speed;
	}
	
	public void setY(float _y){
		y = _y ;
	}
	
	public void setX(float _x){
		x = _x;
	}

	public float getY(){
		return y;
	}
	
	public float getX(){
		return x;
	}
	
	public Bitmap getImage(){
		return snowflake;
	}
	
	public void delta(){
		y += speed;
		x += speed;
	}
}
