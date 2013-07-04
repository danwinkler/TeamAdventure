package com.danwink.teamadventure.game;

import javax.vecmath.Point2i;

import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class Map
{
	public static final int tileSize = 40;
	
	public Tile[][] tiles;
	public Point2i spawn = new Point2i();
	
	public Map( int width, int height )
	{
		tiles = new Tile[width][height];
	}
	
	public Map( MapInfo mi )
	{
		this( mi.width, mi.height );
	}

	public int getWidth()
	{
		return tiles.length;
	}
	
	public int getHeight()
	{
		return tiles[0].length;
	}
	
	public static class Tile
	{
		TileType type;
		
		public Tile()
		{
			
		}
		
		public Tile( TileType t )
		{
			this.type = t;
		}
	}
	
	public enum TileType
	{
		GRASS,
		WAITING;
	}
	
	public void render( Graphics g )
	{
		g.setColor( Color.GRAY );
		g.drawRect( 0, 0, getWidth()*tileSize, getHeight()*tileSize );
		for( int y = 0; y < getHeight(); y++ )
		{
			for( int x = 0; x < getWidth(); x++ )
			{
				Tile t = tiles[x][y];
				if( t != null )
				{
					switch( t.type )
					{
					case GRASS:
						g.setColor( Color.GREEN );
						g.fillRect( x*tileSize, y*tileSize, tileSize, tileSize );
						break;
					case WAITING:
						g.setColor( Color.WHITE );
						g.drawRect( x*tileSize, y*tileSize, tileSize, tileSize );
					}
				}
			}
		}
	}

	public MapInfo getInfo()
	{
		return new MapInfo( getWidth(), getHeight() );
	}
}
