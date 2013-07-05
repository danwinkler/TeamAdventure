package com.danwink.teamadventure.game;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class Unit
{
	public int id;
	public Point2f pos = new Point2f();
	Item[] inventory = new Item[10];
	
	public boolean alive = true;
	public float health = 100;
	
	public Unit()
	{
		id = UniqueInt.get();
	}
	
	public void update( float d )
	{
		if( health <= 0 )
		{
			alive = false;
		}
	}

	public void render( Graphics g )
	{
		g.setColor( Color.GRAY );
		g.fillCircle( pos.x * Map.tileSize, pos.y * Map.tileSize, 10 );
	}

	public void dealDamage( float damage )
	{
		health -= damage;
	}
}
