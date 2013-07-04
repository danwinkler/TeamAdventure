package com.danwink.teamadventure;

import java.util.ArrayList;
import java.util.HashMap;

import com.danwink.teamadventure.game.ClassRegisterHelper;
import com.danwink.teamadventure.game.Map;
import com.danwink.teamadventure.game.MapGenerator;
import com.danwink.teamadventure.game.Player;
import com.danwink.teamadventure.game.TAPacket;
import com.danwink.teamadventure.game.TAPacketType;
import com.danwink.teamadventure.game.Unit;
import com.danwink.teamadventure.game.UnitUpdate;
import com.phyloa.dlib.network.DMessage;
import com.phyloa.dlib.network.DServer;

public class TeamAdventureServer
{
	DServer<TAPacket> server;
	
	Map map;
	ArrayList<Unit> units = new ArrayList<Unit>();
	HashMap<Integer, Unit> unitMap = new HashMap<Integer, Unit>();
	
	public void begin()
	{
		map = MapGenerator.testMap();
		
		server = new DServer<TAPacket>( 512000, 32000 );
		ClassRegisterHelper.register( server );
		server.start( 55678, 55789 );
		
		Thread t = new Thread( new ServerLoop() );
		t.start();
	}
	
	public void update()
	{
		while( server.hasServerMessages() )
		{
			DMessage<TAPacket> m = server.getNextServerMessage();
			
			switch( m.messageType )
			{
				case CONNECTED: 
				{
					//Send basic map outline
					server.sendToClient( m.sender, new TAPacket( TAPacketType.MAPINFO, map.getInfo() ) );
					
					//create a player, add it to unit list/map, send it
					Player p = new Player();
					p.pos.x = map.spawn.x + .5f;
					p.pos.y = map.spawn.y + .5f;
					
					units.add( p );
					unitMap.put( p.id, p );
					
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
					}
					
					break;
				}
			}
		}
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
			while( running )
			{
				try{
				update();
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
}
