package com.effupayme.canvastut;

import android.graphics.Bitmap;

/**
 * 
 * @author jwp
 * Bitmap snowflake:	pass in the bitmap to be controlled
 * int x:				set the initial x postion
 * int y:				set the initial y postion
 * int speed:			set the speed of the change in (x,y) postion
 * 
 */
public class Snowflakes {
	private Bitmap snowflake;
	private int x;
	private int y;
	private int speed;
	
	public Snowflakes(Bitmap _snowflake, int _x, int _y, int _speed){
		snowflake = _snowflake;
		x = _x;
		y = _y;
		speed = _speed;
	}
	
	public void setY(int _y){
		y = _y ;
	}
	
	public void setX(int _x){
		x = _x;
	}

	public int getY(){
		return y;
	}
	
	public int getX(){
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
