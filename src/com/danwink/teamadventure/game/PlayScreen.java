package com.danwink.teamadventure.game;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Point2f;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.danwink.teamadventure.game.Map.Tile;
import com.danwink.teamadventure.game.Map.TileType;
import com.phyloa.dlib.network.DClient;
import com.phyloa.dlib.network.DMessage;

public class PlayScreen implements GameScreen
{
	DClient<TAPacket> client;
	
	Map map;
	
	Player player;
	
	ArrayList<Unit> units = new ArrayList<Unit>();
	HashMap<Integer, Unit> unitMap = new HashMap<Integer, Unit>();
	
	ArrayList<Animation> anims = new ArrayList<Animation>();
	
	public void initialise( GameContainer gc )
	{
		try
		{
			client = new DClient<TAPacket>( 128000, 32000 );
			ClassRegisterHelper.register( client );
			client.start( InetAddress.getLocalHost().getHostAddress(), 5000, 55678, 55789 );
		} catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void render( GameContainer gc, Graphics g )
	{
		if( player != null )
		{
			g.translate( (int)(player.pos.x*Map.tileSize - gc.getWidth()/2), (int)(player.pos.y*Map.tileSize - gc.getHeight()/2) );
			map.render( g );
			
			for( int i = 0; i < units.size(); i++ )
			{
				Unit u = units.get( i );
				u.render( g );
			}
			
			for( int i = 0; i < anims.size(); i++ )
			{
				anims.get( i ).render( g );
			}
			
			g.setColor( Color.RED );
			g.drawCircle( Gdx.input.getX() + (int)(player.pos.x*Map.tileSize - gc.getWidth()/2), Gdx.input.getY() + (int)(player.pos.y*Map.tileSize - gc.getHeight()/2), 10 );
		}
	}

	public void update( GameContainer gc, ScreenManager sm, float d )
	{
		while( client.hasClientMessages() )
		{
			DMessage<TAPacket> message = client.getNextClientMessage();
			
			if( message.message != null )
			{
				TAPacket packet = message.message;
				switch( packet.type )
				{
					case MAPINFO:
					{
						map = new Map( (MapInfo)packet.message );
						break;
					}
					case PLAYERINFO:
					{
						player = (Player)packet.message;
						
						units.add( player );
						unitMap.put( player.id, player );
						break;
					}
					case TILEUPDATE:
					{
						Object[] oa = (Object[])packet.message;
						Tile t = (Tile)oa[0];
						int[] pos = (int[])oa[1];
						map.tiles[pos[0]][pos[1]] = t;
						break;
					}
					case UNITUPDATE:
					{
						UnitUpdate uu = (UnitUpdate)packet.message;
						if( uu.id == player.id ) break;
						Unit u = unitMap.get( uu.id );
						if( u == null )
						{
							u = new Unit();
							u.id = uu.id;
							unitMap.put( uu.id, u );
							units.add( u );
						}
						
						u.pos.x = uu.x;
						u.pos.y = uu.y;
						break;
					}
					case PLAYANIM:
					{
						Animation a = (Animation)packet.message;
						anims.add( a );
						break;
					}
				}
			}
		}
		
		if( player != null )
		{
			
			searchForNullTiles:
			for( int y = Math.max( (int)(player.pos.y - 6), 0 ); y < Math.min( (int)(player.pos.y + 6), map.getHeight() ); y++ )
			{
				for( int x = Math.max( (int)(player.pos.x - 6), 0 ); x <  Math.min( (int)(player.pos.x + 6), map.getWidth() ); x++ )
				{
					if( map.tiles[x][y] == null )
					{
						float dx = (x+.5f)-player.pos.x;
						float dy = (y+.5f)-player.pos.y;
						if( dx*dx+dy*dy < 5*5 )
						{
							map.tiles[x][y] = new Tile( TileType.WAITING );
							client.sendToServer( new TAPacket( TAPacketType.TILEUPDATE, new int[] { x, y } ) );
							client.manualUpdate( 5 ); 
							break searchForNullTiles;
						}
					}
				}
			}
		
			for( int i = 0; i < anims.size(); i++ )
			{
				Animation a = anims.get( i );
				a.update( d );
				if( !a.alive )
				{
					anims.remove( a );
					i--;
				}
			}
			
			//Player Control
			boolean moved = false;
			if( Gdx.input.isKeyPressed( Keys.W ) )
			{
				player.pos.y -= 3 * d;
				moved = true;
			}
			if( Gdx.input.isKeyPressed( Keys.S ) )
			{
				player.pos.y += 3 * d;
				moved = true;
			}
			if( Gdx.input.isKeyPressed( Keys.A ) )
			{
				player.pos.x -= 3 * d;
				moved = true;
			}
			if( Gdx.input.isKeyPressed( Keys.D ) )
			{
				player.pos.x += 3 * d;
				moved = true;
			}
			
			if( moved )
			{
				client.sendToServer( new TAPacket( TAPacketType.PLAYERINFO, new UnitUpdate( player.id, player.pos.x, player.pos.y ) ) );
			}
			
			if( Gdx.input.isButtonPressed( Input.Buttons.LEFT ) )
			{
				Point2f mouseInWorld = screenToPos( Gdx.input.getX(), Gdx.input.getY(), gc );
				client.sendToServer( new TAPacket( TAPacketType.CLICK, new float[] { Input.Buttons.LEFT, mouseInWorld.x, mouseInWorld.y } ) );
				
			}
		}
	}
	
	public int getId()
	{
		return this.getClass().getName().hashCode();
	}
	
	public Point2f screenToPos( float x, float y, GameContainer gc )
	{
		Point2f p = new Point2f( x, y );
		p.x += player.pos.x*Map.tileSize;
		p.y += player.pos.y*Map.tileSize;
		
		p.x -= gc.getWidth()/2;
		p.y -= gc.getHeight()/2;
		
		p.x /= Map.tileSize;
		p.y /= Map.tileSize;
		return p;
	}
}
