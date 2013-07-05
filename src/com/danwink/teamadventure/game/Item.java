package com.danwink.teamadventure.game;

import java.util.ArrayList;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;


public abstract class Item
{
	public abstract void use( Unit u, TeamAdventureServer s, Point2f target );
}

class MiniWand extends Item
{
	public void use( Unit u, TeamAdventureServer s, Point2f target )
	{
		Vector2f vec = new Vector2f( target );
		vec.sub( u.pos );
		vec.normalize();
		vec.scale( 5 );
		
		s.addParticle( new Particle( u, u.pos.x, u.pos.y, vec.x * .3f, vec.y * .3f, 1.f/.3f, 10 ) );
		
		s.playAnimation( new LineToPointAnim( u.pos.x, u.pos.y, u.pos.x + vec.x, u.pos.y + vec.y, .3f ) );
	}	
}
