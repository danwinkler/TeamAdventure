package com.danwink.teamadventure.game;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Point2f;

import com.badlogic.gdx.Input;
import com.phyloa.dlib.network.DMessage;
import com.phyloa.dlib.network.DServer;

public class TeamAdventureServer
{
	DServer<TAPacket> server;
	
	Map map;
	ArrayList<Unit> units = new ArrayList<Unit>();
	HashMap<Integer, Unit> unitMap = new HashMap<Integer, Unit>();
	HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	
	ArrayList<Particle> particles = new ArrayList<Particle>();
	
	public void begin()
	{
		map = MapGenerator.testMap();
		
		server = new DServer<TAPacket>( 512000, 32000 );
		ClassRegisterHelper.register( server );
		server.start( 55678, 55789 );
		
		Thread t = new Thread( new ServerLoop() );
		t.start();
	}
	
	public void update( float d )
	{
		for( int i = 0; i < particles.size(); i++ )
		{
			Particle p = particles.get( i );
			p.update( this, d );
			if( !p.alive )
			{
				particles.remove( i );
				i--;
			}
		}
		
		while( server.hasServerMessages() )
		{
			DMessage<TAPacket> m = server.getNextServerMessage();
			
			switch( m.messageType )
			{
				case CONNECTED: 
				{
					//Send map info
					server.sendToClient( m.sender, new TAPacket( TAPacketType.MAPINFO, map.getInfo() ) );
					
					//create a player, add it to unit list/map, send it
					Player p = new Player();
					p.pos.x = map.spawn.x + .5f;
					p.pos.y = map.spawn.y + .5f;
					
					units.add( p );
					unitMap.put( p.id, p );
					
					players.put( m.sender, p );
					
					server.sendToClient( m.sender, new TAPacket( TAPacketType.PLAYERINFO, p ) );
					break;
				}
				case DISCONNECTED:
					
					break;
					
				case DATA:
				{
					TAPacket p = m.message;
					
					switch( p.type )
					{
						case TILEUPDATE:
						{
							int[] pos = (int[])p.message;
							server.sendToClient( m.sender, new TAPacket( TAPacketType.TILEUPDATE, new Object[] { map.tiles[pos[0]][pos[1]], new int[] { pos[0], pos[1] } } ) );
							break;
						}
						case PLAYERINFO:
						{
							UnitUpdate uu = (UnitUpdate)p.message;
							Unit u = unitMap.get( uu.id );
							u.pos.x = uu.x;
							u.pos.y = uu.y;
							server.sendToAllClients( new TAPacket( TAPacketType.UNITUPDATE, uu ) );
							break;
						}
						case CLICK:
						{
							float[] vals = (float[])p.message;
							int selectedItem = 0; //@TODO  Have client send selected item
							if( vals[0] == Input.Buttons.LEFT )
							{
								Player player = players.get( m.sender );
								Item item = player.inventory[selectedItem];
								if( item != null )
								{
									item.use( player, this, new Point2f( vals[1], vals[2] ) );
								}
							}
							break;
						}
					}
					
					break;
				}
			}
		}
	}
	
	public void playAnimation( Animation animation ) 
	{
		server.sendToAllClients( new TAPacket( TAPacketType.PLAYANIM, animation ) );
	}
	
	public class ServerLoop implements Runnable 
	{
		long lastTime;
		long frameTime = (1000 / 30);
		long timeDiff;
		public boolean running = true;
		public ServerLoop()
		{
			
		}

		public void run() 
		{
			lastTime = System.currentTimeMillis();
			long lastWholeFrame = lastTime;
			while( running )
			{
				try{
				long timeDiff = System.currentTimeMillis() - lastWholeFrame;
				lastWholeFrame = System.currentTimeMillis();
				update( 1000.f / timeDiff );
				} catch( Exception ex )
				{
					ex.printStackTrace();
				}
				long time = System.currentTimeMillis();
				timeDiff = (lastTime + frameTime) - time;
				if( timeDiff > 0 )
				{
					try {
						Thread.sleep( timeDiff );
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				lastTime = System.currentTimeMillis();
			}
			server.stop();
		}	
	}
	
	public static void main( String[] args )
	{
		TeamAdventureServer tas = new TeamAdventureServer();
		tas.begin();
	}

	public void addParticle( Particle particle )
	{
		particles.add( particle );
	}
}
