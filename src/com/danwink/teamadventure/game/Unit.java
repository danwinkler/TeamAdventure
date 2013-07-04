package com.danwink.teamadventure.game;

import javax.vecmath.Vector2f;

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class Unit
{
	public int id;
	public Vector2f pos = new Vector2f();
	
	public Unit()
	{
		id = UniqueInt.get();
	}

	public void render( Graphics g )
	{
		g.setColor( Color.GRAY );
		g.fillCircle( pos.x * Map.tileSize, pos.y * Map.tileSize, 10 );
	}
}
