package com.danwink.teamadventure.game;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

public class Particle 
{
	Point2f p;
	Vector2f delta;
	float damage;
	float time;
	float endTime;
	boolean alive = true;
	Unit owner;
	
	public Particle( Unit owner, float x, float y, float dx, float dy, float endTime, float damage )
	{
		p = new Point2f( x, y );
		delta = new Vector2f( dx, dy );
		this.endTime = endTime;
		this.damage = damage;
		this.owner = owner;
	}
	
	public void update( TeamAdventureServer ts, float d )
	{
		p.x += delta.x * d;
		p.y += delta.y * d;
		time += d;
		if( time >= endTime )
		{
			alive = false;
		}
		
		for( int i = 0; i < ts.units.size(); i++ )
		{
			Unit u = ts.units.get( i );
			if( u != owner && p.distanceSquared( u.pos ) < 10*10 )
			{
				u.dealDamage( damage );
				alive = false;
				break;
			}
		}
	}
}
