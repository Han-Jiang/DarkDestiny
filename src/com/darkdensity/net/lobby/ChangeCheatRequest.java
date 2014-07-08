package com.darkdensity.net.lobby;

import com.darkdensity.setting.Config;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class ChangeCheatRequest extends GameRequest {

	boolean isCheat;

	public ChangeCheatRequest(boolean isCheat) {
		super(Config.PLAYER_NAME);
		this.isCheat = isCheat;
	}

	public boolean isCheat() {
		return isCheat;
	}
}
