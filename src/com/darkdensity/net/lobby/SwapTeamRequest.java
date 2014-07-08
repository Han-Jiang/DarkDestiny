package com.darkdensity.net.lobby;

import com.darkdensity.setting.Constant.PlayerRole;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class SwapTeamRequest extends GameRequest{

	
	private PlayerRole role;

	public SwapTeamRequest(String playerName, PlayerRole role) {
		super(playerName);
		this.role = role;
	}

	public PlayerRole getTeam() {
		return role;
	}

}
