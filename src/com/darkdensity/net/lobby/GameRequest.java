package com.darkdensity.net.lobby;

import java.io.Serializable;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class GameRequest implements Serializable{

	private String playerName;

	public GameRequest(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
	
	

}
