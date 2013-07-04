package com.danwink.teamadventure;

import org.mini2Dx.core.game.Mini2DxGame;
import org.mini2Dx.core.game.ScreenBasedGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.danwink.teamadventure.game.PlayScreen;

public class TeamAdventureClient extends ScreenBasedGame
{
	public void initialise()
	{
		this.addScreen( new PlayScreen() );
	}

	public int getInitialScreenId()
	{
		return PlayScreen.class.getName().hashCode();
	}
	
	public static void main( String[] args )
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TeamAdventureClient";
		cfg.useGL20 = true;
		cfg.width = 800;
		cfg.height = 600;
		cfg.useCPUSynch = false;
		cfg.vSyncEnabled = true;
		new LwjglApplication( new Mini2DxGame( new TeamAdventureClient() ), cfg );
	}
}
