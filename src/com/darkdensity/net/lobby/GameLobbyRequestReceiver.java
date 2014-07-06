package com.darkdensity.net.lobby;

import java.io.DataInputStream;
import java.net.Socket;

import com.darkdensity.core.NetworkManager;
import com.darkdensity.gui.MultiPlayerSettingPanel;
import com.darkdensity.net.core.GameServer;
import com.darkdensity.net.core.NetUtil;
import com.darkdensity.setting.Config;

public class GameLobbyRequestReceiver implements Runnable {

	private NetworkManager networkManager;
	private Socket socket;
	private MultiPlayerSettingPanel multiPlayerSettingPanel;

	public GameLobbyRequestReceiver(NetworkManager networkManager, Socket socket) {
		this.networkManager = networkManager;
		this.socket = socket;
		this.multiPlayerSettingPanel = networkManager.getMultiPlayerSettingPanel();
	}

	@Override
	public void run() {
		System.out.println("Gamelobby request receiver is running");
		// TODO Auto-generated method stub
		try {
			int messageLength = 0;
			byte[] message;

			
			while (networkManager.getState() == 1) {
				DataInputStream dataInputStream = new DataInputStream(
						socket.getInputStream());
				while (networkManager.getState() == 1) {
					try {
						messageLength = dataInputStream.readInt();
						break;
					} catch (Throwable t) {
						continue;
					}
				}
				
				if(networkManager.getState() != 1){
					break;
				}

				message = new byte[messageLength];

				dataInputStream.read(message);
				Object request = NetUtil.deserialize(message);
				
				GameServer gServer = this.networkManager.getGameServer();
				if (gServer != null) {
					networkManager.getGameLobby().sendRequest((GameRequest) request);
				}
				
				if(request instanceof SwapTeamRequest){
					this.multiPlayerSettingPanel.swapTeam((SwapTeamRequest)request);
				}else if(request instanceof ChangeGameTimeRequest){
					int gameTime = ((ChangeGameTimeRequest)request).getGameTime();
					this.multiPlayerSettingPanel.getSettingPanel().setGameTimeText(gameTime + "");
					Config.GAME_WINNING_TIME = gameTime * 60000;
					System.out.println("GAMETIME: "+ Config.GAME_WINNING_TIME);
				}else if(request instanceof ChangeRevealRequest){
					boolean isFullReveal = ((ChangeRevealRequest)request).isFullReveal();
					Config.IS_FULL_REVEAL = isFullReveal;
					this.multiPlayerSettingPanel.getSettingPanel().setRevealState();
					System.out.println("Set Reveal");
				}else if(request instanceof ChangeCheatRequest){
					boolean isCheatAllowed = ((ChangeCheatRequest)request).isCheat();
					Config.IS_CHEAT_ALLOWED = isCheatAllowed;
					this.multiPlayerSettingPanel.getSettingPanel().setCheatState();
					System.out.println("Set Cheat");
				}
					
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
