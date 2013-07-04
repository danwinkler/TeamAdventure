package com.danwink.teamadventure.game;

public class UniqueInt
{
	private static int value = 0;
	
	public static int get()
	{
		value++;
		return value;
	}
}
