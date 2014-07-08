package com.darkdensity.net.lobby;

import com.darkdensity.setting.Config;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class ChangeRevealRequest extends GameRequest {

	boolean isFullReveal;

	public ChangeRevealRequest(boolean isFullReveal) {
		super(Config.PLAYER_NAME);
		this.isFullReveal = isFullReveal;
	}

	public boolean isFullReveal() {
		return isFullReveal;
	}

	
}
