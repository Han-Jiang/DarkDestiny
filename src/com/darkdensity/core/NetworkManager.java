package com.darkdensity.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.darkdensity.gui.MultiPlayerSettingPanel;
import com.darkdensity.net.chat.VoiceChatClient;
import com.darkdensity.net.chat.VoiceChatManager;
import com.darkdensity.net.chat.VoiceChatServer;
import com.darkdensity.net.core.GameClientIO;
import com.darkdensity.net.core.GameServer;
import com.darkdensity.net.core.NetUtil;
import com.darkdensity.net.core.Packet;
import com.darkdensity.net.lobby.GameLobby;
import com.darkdensity.net.lobby.GameLobbyClient;
import com.darkdensity.net.lobby.GameLobbyServer;
import com.darkdensity.setting.Config;

public class NetworkManager {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ArrayList<String> playerList;
	private int state;
	private MultiPlayerSettingPanel multiPlayerSettingPanel;
	private JFrame jFrame;
	private GameServer gameServer;
	private GameWorld gameWorld;
	private String serverAddress;
	private VoiceChatManager voiceChatManager;
	private GameLobby gameLobby;

	public NetworkManager() throws IOException {
		// TODO Auto-generated constructor stub
		this.playerList = new ArrayList<String>();

		serverSocket = new ServerSocket(3000);

		this.serverAddress = InetAddress.getLocalHost().getHostAddress();

		multiPlayerSettingPanel = new MultiPlayerSettingPanel(GameCore.frame,
				this);

		GameCore.frame.setContentPane(multiPlayerSettingPanel);

		this.addPlayer(Config.PLAYER_NAME);

		this.state = 1;
		gameServer = new GameServer(this);
		new Thread(gameServer).start(); // start server

		voiceChatManager = new VoiceChatServer();
		multiPlayerSettingPanel.setVCM(voiceChatManager);

		gameLobby = new GameLobbyServer(this);

	}

	public NetworkManager(String ipaddress) throws Throwable {
		this.state = 1;
		this.jFrame = GameCore.frame;
		this.clientSocket = new Socket();
		this.serverAddress = ipaddress;
		this.clientSocket
				.connect(new InetSocketAddress(ipaddress, 3000), 50000); // Throw
		// IOException
		// when
		// connection
		// failure
		voiceChatManager = new VoiceChatClient(ipaddress);

		GameClientIO gameClientIO = new GameClientIO(this, this.clientSocket);
		// connect
		gameClientIO.handleConnection();

	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void addPlayer(String playerName) {
		this.playerList.add(playerName);
		multiPlayerSettingPanel.updatePlayerList();
	}

	public ArrayList<String> getPlayerList() {
		return playerList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setPlayerList(ArrayList<String> playerList) {
		System.out.println("Set PlayerList size: " + playerList.size());
		for (String s : playerList) {
			System.out.println("Player List element: " + s);
		}
		this.playerList = playerList;
	}

	public void startGame(JFrame parent, int gameTime) {
		// For game server
		this.gameServer.startGame(parent, gameTime);

	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	public GameServer getGameServer() {
		return gameServer;
	}

	public void sendNetworkCommand(Object packet) {
		try {
			DataOutputStream dataOutputStream = new DataOutputStream(
					this.clientSocket.getOutputStream());
			byte[] gameCommandAry = NetUtil.serialize(packet);
			dataOutputStream.writeInt(gameCommandAry.length);
			dataOutputStream.flush();
			dataOutputStream.write(gameCommandAry);
			dataOutputStream.flush();
			System.out.println("Send Command: " + packet.getClass().getName());
		} catch (SocketException e) {
			System.out.print("host down");
		} catch (Exception e) {
			if(Config.DEBUGMODE){e.printStackTrace();}
		}

	}

	public JFrame getFrame() {
		return jFrame;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public VoiceChatManager getVoiceChatManager() {
		return this.voiceChatManager;
	}

	public MultiPlayerSettingPanel getMultiPlayerSettingPanel() {
		return multiPlayerSettingPanel;
	}

	public GameLobby getGameLobby() {
		return gameLobby;
	}

	public void setGameLobby(GameLobby gameLobby) {
		this.gameLobby = gameLobby;
	}

	public void setMultiPlayerSettingPanel(
			MultiPlayerSettingPanel multiPlayerSettingPanel) {
		this.multiPlayerSettingPanel = multiPlayerSettingPanel;
	}
	

}
