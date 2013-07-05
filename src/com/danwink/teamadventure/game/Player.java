package com.danwink.teamadventure.game;

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class Player extends Unit
{
	public Player()
	{
		super();
		inventory[0] = new MiniWand();
	}
	
	public void render( Graphics g )
	{
		g.setColor( Color.RED );
		g.fillCircle( pos.x * Map.tileSize, pos.y * Map.tileSize, 10 );
	}
}
