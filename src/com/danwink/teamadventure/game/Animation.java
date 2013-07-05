package com.danwink.teamadventure.game;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

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
	
	float speed;
	float pos;
	
	public LineToPointAnim()
	{
		
	}
	
	public LineToPointAnim( float x1, float y1, float x2, float y2, float speed )
	{
		start = new Point2f( x1, y1 );
		dir = new Vector2f( x2 - x1, y2 - y1 );
		this.speed = speed;
	}
	
	public void update( float d )
	{
		pos += d * speed;
		if( pos >= 1 )
		{
			alive = false;
		}
	}
	
	public void render( Graphics g )
	{
		if( alive )
		{
			g.setColor( Color.ORANGE );
			g.drawCircle( (start.x + dir.x * pos) * Map.tileSize, (start.y + dir.y * pos) * Map.tileSize, 3 );
		}
	}
}
