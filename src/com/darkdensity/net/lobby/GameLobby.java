package com.darkdensity.net.lobby;

import com.darkdensity.core.NetworkManager;
/** 
* @author Team A1 - Hei Yin Wong
*/
public abstract class GameLobby{

	protected NetworkManager networkManager;
	

	public GameLobby(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}


	public abstract void sendRequest(GameRequest request);


	public abstract void stop();


	

}
