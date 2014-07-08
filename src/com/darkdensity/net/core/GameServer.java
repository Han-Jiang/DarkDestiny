package com.darkdensity.net.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import com.darkdensity.command.SettingCommand;
import com.darkdensity.core.GameWorld;
import com.darkdensity.core.NetworkManager;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.GameMode;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class GameServer implements Runnable {

	private NetworkManager networkManager;
	private ServerSocket serverSocket;
	private ArrayList<Socket> clients;

	private int messageLength;
	private byte[] messageAry;
	private String message;
	private int status;

	public GameServer(NetworkManager networkManager) {
		this.networkManager = networkManager;
		this.serverSocket = networkManager.getServerSocket();
		this.clients = new ArrayList<Socket>();
		this.status = 1;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (status == 1) {
			try {
				Socket cs = serverSocket.accept();

				DataInputStream dataInputStream = new DataInputStream(
						cs.getInputStream());

				DataOutputStream dataOutputStream = new DataOutputStream(
						cs.getOutputStream());

				messageLength = dataInputStream.readInt();
				messageAry = new byte[messageLength];
				dataInputStream.read(messageAry);
				message = new String(messageAry);
				this.networkManager.addPlayer(message);

				this.clients.add(cs);

				this.notifyPlayerChange();

				

			} catch (Exception e) {
				if(Config.DEBUGMODE){e.printStackTrace();}
			}

		}

	}

	public void notifyPlayerChange() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					for (Socket c : clients) {
						byte[] playerListByteAry = NetUtil
								.serialize(GameServer.this.networkManager
										.getPlayerList());
						DataOutputStream dataOutputStream = new DataOutputStream(
								c.getOutputStream());
						dataOutputStream.writeInt(playerListByteAry.length);
						dataOutputStream.flush();
						dataOutputStream.write(playerListByteAry);
						dataOutputStream.flush();
						System.out.println("Output done");

					}
				} catch (Exception e) {
					if(Config.DEBUGMODE){e.printStackTrace();}
				}

			}
		};

		new Thread(runnable).start();
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public void startGame(final JFrame parent, final int gameTime) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				status = 0;
				networkManager.setState(0);

				networkManager.getGameLobby().stop();
				// TODO Auto-generated method stub

				try {
					for (Socket c : clients) {
						System.out.println("Start game");
						DataOutputStream dataOutputStream = new DataOutputStream(
								c.getOutputStream());
/*
						SettingCommand settingCommand = new SettingCommand();
						HashMap<String, Object> commandData = new HashMap<String, Object>();
						commandData.put(NetConstant.GAME_TIME_KEY, gameTime);
						settingCommand.setCommandData(commandData);
						sendNetworkCommand(settingCommand
								.pack(Config.PLAYER_NAME));*/

						byte[] startGameCommandAry = NetConstant.STARTGAMECOMMAND
								.getBytes();

						dataOutputStream.writeInt(startGameCommandAry.length);
						dataOutputStream.flush();
						dataOutputStream.write(startGameCommandAry);
						dataOutputStream.flush();
						System.out.println("start game command done");

						// start command receiver

					}
				} catch (Exception e) {
					if(Config.DEBUGMODE){e.printStackTrace();}
				}

				Config.IS_NETWORK_MODE = true;
				GameWorld gameWorld;
				try {
					gameWorld = new GameWorld(parent, GameMode.VERSUS);
					gameWorld.setNetworkManager(networkManager);
					networkManager.setGameWorld(gameWorld);
					gameWorld.initSprite();
					gameWorld.startGame();
					parent.setContentPane(gameWorld);

					for (Socket c : clients) {
						new Thread(new GameServerCommandReceiver(gameWorld,
								networkManager, c)).start();
					}

				} catch (Throwable t) {
					t.printStackTrace();
				}

			}
		};

		new Thread(runnable).start();
	}

	public void sendNetworkCommand(Object packet) {
		// System.out.println("*********PACKET INFO*********");
		// System.out.println(packet.getClassName());
		// System.out.println(packet.getCommandData().get("focusID"));
		// System.out.println("*****************************");
		try {
			for (Socket c : clients) {
				DataOutputStream dataOutputStream = new DataOutputStream(
						c.getOutputStream());
				byte[] gameCommandAry = NetUtil.serialize(packet);
				dataOutputStream.writeInt(gameCommandAry.length);
				dataOutputStream.flush();
				dataOutputStream.write(gameCommandAry);
				dataOutputStream.flush();

			}
		} catch (Exception e) {
			if(Config.DEBUGMODE){e.printStackTrace();}
		}

	}

}
