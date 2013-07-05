package com.danwink.teamadventure.game;

import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;

import com.phyloa.dlib.network.ClassRegister;

public class ClassRegisterHelper
{
	public static void register( ClassRegister c )
	{
		c.register( Animation.class );
		
		c.register( Map.class );
		c.register( MapInfo.class );
		
		c.register( Map.Tile.class );
		c.register( Map.Tile[].class );
		c.register( Map.Tile[][].class );
		c.register( Map.TileType.class );
		
		c.register( TAPacket.class );
		c.register( TAPacketType.class );
		
		c.register( Unit.class );
		c.register( UnitUpdate.class );
		
		c.register( Player.class );
		
		c.register( Item.class );
		c.register( Item[].class );
		c.register( MiniWand.class );
		
		c.register( int[].class );
		c.register( Object[].class );
		c.register( float[].class );
		
		c.register( Point2f.class );
		c.register( Vector2f.class );
		
		c.register( Animation.class );
		c.register( LineToPointAnim.class );
	}
}
