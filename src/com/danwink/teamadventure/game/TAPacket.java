package com.danwink.teamadventure.game;

public class TAPacket
{
	public TAPacketType type;
	public Object message;
	
	public TAPacket()
	{
		
	}
	
	public TAPacket( TAPacketType type, Object message )
	{
		this.type = type;
		this.message = message;
	}
}
