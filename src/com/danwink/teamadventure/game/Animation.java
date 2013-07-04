package com.danwink.teamadventure.game;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import org.mini2Dx.core.graphics.Graphics;

public abstract class Animation
{
	boolean alive = true;
	public abstract void update( float d );
	public abstract void render( Graphics g );
}

class LineToPointAnim extends Animation
{
	Point2f start;
	Vector2f dir;
	
	float time;
	
	public LineToPointAnim( float x1, float y1, int 
	
	public void update( float d )
	{
		
	}
	
	public void render( Graphics g )
	{
		
	}
}
