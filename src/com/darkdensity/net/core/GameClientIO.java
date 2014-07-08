package com.darkdensity.net.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.darkdensity.core.GameCore;
import com.darkdensity.core.GameWorld;
import com.darkdensity.core.NetworkManager;
import com.darkdensity.gui.MultiPlayerSettingPanel;
import com.darkdensity.net.lobby.GameLobby;
import com.darkdensity.net.lobby.GameLobbyClient;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.GameMode;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class GameClientIO {

	private Socket socket;
	private NetworkManager networkManager;

	private int messageLength;
	private byte[] messageAry;
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private Thread waitToStartGameThread;
	private GameWorld gameWorld;
	private MultiPlayerSettingPanel multiPlayerSettingPanel;

	public GameClientIO(NetworkManager networkManager, Socket socket) {
		this.socket = socket;
		this.networkManager = networkManager;
		try {
			this.dataInputStream = new DataInputStream(socket.getInputStream());
			this.dataOutputStream = new DataOutputStream(
					socket.getOutputStream());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void handleConnection() {
		try {

			System.out.println("---Connected---");

			connectToGameLobby();

			waitToStartGame();

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void connectToGameLobby() throws Throwable {
		int messageLength;
		byte[] messageAry;
		String message;

		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		DataInputStream dataInputStream = new DataInputStream(
				socket.getInputStream());

		messageAry = Config.PLAYER_NAME.getBytes();
		messageLength = messageAry.length;

		dataOutputStream.writeInt(messageLength);
		dataOutputStream.write(messageAry);
		dataOutputStream.flush();

		multiPlayerSettingPanel = new MultiPlayerSettingPanel(GameCore.frame,
				networkManager);
		multiPlayerSettingPanel.setVCM(networkManager.getVoiceChatManager());

		networkManager.setMultiPlayerSettingPanel(multiPlayerSettingPanel);
		
		GameCore.frame.setContentPane(multiPlayerSettingPanel);
		
		
		GameLobby gameLobby = new GameLobbyClient(networkManager, socket.getInetAddress().getHostAddress());
		networkManager.setGameLobby(gameLobby);
		

	}

	public void waitToStartGame() throws Throwable {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub

					int messageLength;
					byte[] messageAry;
					DataInputStream dataInputStream;

					dataInputStream = new DataInputStream(
							socket.getInputStream());

					
					while (networkManager.getState() == 1) {
						while (true) {
							// TODO start game
							try {
								messageLength = dataInputStream.readInt();
								break;
							} catch (Throwable t) {
								continue;
							}
						}

						messageAry = new byte[messageLength];
						dataInputStream.read(messageAry);

						if (new String(messageAry)
								.equals(NetConstant.STARTGAMECOMMAND)) {
							Config.IS_NETWORK_MODE = true;
							networkManager.setState(2);
							System.out
									.println("message ary equal to start game command");
							networkManager.getGameLobby().stop();
							// NetworkManager.this.startGame();
							gameWorld = new GameWorld(
									networkManager.getFrame(),
									GameMode.VERSUS);
							gameWorld.setNetworkManager(networkManager);
							networkManager.getFrame().setContentPane(gameWorld);

							gameWorld.startGame();

							multiPlayerSettingPanel.stopChatPanel();
							new Thread(new GameServerCommandReceiver(gameWorld,
									networkManager, socket)).start();
							System.out
									.println("new game server command receiver is executed");

						} else {
							Object obj = NetUtil.deserialize(messageAry);
							if (obj instanceof ArrayList) {
								System.out.println("---Client list---");
								ArrayList<String> playerList = (ArrayList<String>) obj;
								for (String s : playerList) {
									System.out.println(s);
								}

								networkManager.setPlayerList(playerList);
								multiPlayerSettingPanel.updatePlayerList();
							}
						}

					}
				} catch (Throwable t) {
					t.printStackTrace();
				}

			}
		};
		waitToStartGameThread = new Thread(runnable);
		waitToStartGameThread.start();
	}
}
