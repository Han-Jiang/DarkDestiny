package com.darkdensity.net.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.darkdensity.core.NetworkManager;
import com.darkdensity.setting.Config;

public class GameServerIS implements Runnable {

	private InputStream is;
	private GameServer gameServer;
	private NetworkManager networkManager;

	public GameServerIS(GameServer gameServer, InputStream is) {
		this.gameServer = gameServer;
		this.is = is;
		this.networkManager = gameServer.getNetworkManager();
	}

	@Override
	public void run() {
		String playerName;

		BufferedReader in = new BufferedReader(new InputStreamReader(is));

		try {
			System.out.println("GameServerIS");
			playerName = in.readLine();
			System.out.println(playerName + " is Connected");
			networkManager.addPlayer(playerName);

			Thread.sleep(10);

			gameServer.notifyPlayerChange();

			String inline;

			/*
			 * while(true){ inline = in.readLine(); if(inline!=null){
			 * System.out.println("Line: "+ inline); } }
			 */

		} catch (IOException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}

	}

}
