package com.darkdensity.net.lobby;

import com.darkdensity.setting.Config;

public class ChangeGameTimeRequest extends GameRequest {

	int gameTime;
	public ChangeGameTimeRequest(int gameTime) {
		super(Config.PLAYER_NAME);
		this.gameTime = gameTime;
	}
	public int getGameTime() {
		return gameTime;
	}
	

}
