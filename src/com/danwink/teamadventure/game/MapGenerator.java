package com.danwink.teamadventure.game;

import com.danwink.teamadventure.game.Map.Tile;
import com.danwink.teamadventure.game.Map.TileType;

public class MapGenerator
{
	public static Map testMap()
	{
		Map m = new Map( 50, 50 );
		
		for( int y = 0; y < m.getHeight(); y++ )
		{
			for( int x = 0; x < m.getWidth(); x++ )
			{
				m.tiles[x][y] = new Tile( TileType.GRASS );
			}
		}
		
		m.spawn.x = m.getWidth()/2;
		m.spawn.y = m.getHeight()/2;
		
		return m;
	}
}
