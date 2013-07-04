package com.danwink.teamadventure.game;

import java.util.ArrayList;

import com.danwink.teamadventure.TeamAdventureServer;

public abstract class Item
{
	public abstract void use( Unit u, TeamAdventureServer s );
}

class MiniWand extends Item
{
	public void use( Unit u, TeamAdventureServer s )
	{
		
	}	
}
