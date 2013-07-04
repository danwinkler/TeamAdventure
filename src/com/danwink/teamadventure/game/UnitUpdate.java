package com.danwink.teamadventure.game;

public class UnitUpdate
{
	public int id;
	public float x, y;
	
	public UnitUpdate()
	{
		
	}
	
	public UnitUpdate( int id, float x, float y )
	{
		this.id = id;
		this.x = x;
		this.y = y;
	}
}
