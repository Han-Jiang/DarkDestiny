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
import com.darkdensity.net.core.NetConstant;
import com.darkdensity.net.core.NetUtil;
import com.darkdensity.net.core.Packet;
import com.darkdensity.net.lobby.GameLobby;
import com.darkdensity.net.lobby.GameLobbyClient;
import com.darkdensity.net.lobby.GameLobbyServer;
import com.darkdensity.setting.Config;
/**
 * 
* @ClassName: NetworkManager
* @Description: Handles all networking activity in multiplayer mode
* @author Team A1 - Hei Yin Wong
* @date Mar 27, 2014 10:48:04 PM
 */
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
		//Handles the network activity for game host
		// TODO Auto-generated constructor stub
		this.playerList = new ArrayList<String>();

		//Create a server socket on correct port (this is configurable in NetConst.java)
		serverSocket = new ServerSocket(NetConstant.GAME_WORLD_PORT);

		//Store local ip address that will be displayed in game lobby
		this.serverAddress = InetAddress.getLocalHost().getHostAddress();

		//Create UI for game lobby
		multiPlayerSettingPanel = new MultiPlayerSettingPanel(GameCore.frame,
				this);

		GameCore.frame.setContentPane(multiPlayerSettingPanel);

		//Init and update player list
		this.addPlayer(Config.PLAYER_NAME);

		//State = 1 means the game is not started yet and inside game lobby
		//It will be used in different thread to control if it is alive
		this.state = 1;
		
		//Create GameServer Runnable object to handles server I/O and it's logic 
		gameServer = new GameServer(this);
		new Thread(gameServer).start(); // start server

		
		//Create a Voice Chat Server 
		voiceChatManager = new VoiceChatServer();
		multiPlayerSettingPanel.setVCM(voiceChatManager);

		//Create the GameLobbyServer object, this one will listen on GameLobbyRequest like swap team request
		gameLobby = new GameLobbyServer(this);

	}

	public NetworkManager(String ipaddress) throws Throwable {
		//Handles client's I/O and logic in multiplayer's mode
		this.state = 1;
		this.jFrame = GameCore.frame;
		this.clientSocket = new Socket();
		this.serverAddress = ipaddress;
		//Build the connection to server
		this.clientSocket
				.connect(new InetSocketAddress(ipaddress, NetConstant.GAME_WORLD_PORT), 50000); // Throw
		voiceChatManager = new VoiceChatClient(ipaddress);

		
		//Handls I/O for client
		GameClientIO gameClientIO = new GameClientIO(this, this.clientSocket);

		gameClientIO.handleConnection();

	}

	/**
	 * 
	* @Title: getServerSocket 
	* @Description: get the socket instance
	* @param @return
	* @return ServerSocket    
	* @throws
	 */
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * 
	* @Title: addPlayer 
	* @Description: add one player
	* @param @param playerName
	* @return void    
	* @throws
	 */
	public void addPlayer(String playerName) {
		this.playerList.add(playerName);
		multiPlayerSettingPanel.updatePlayerList();
	}

	/**
	 * 
	* @Title: getPlayerList 
	* @Description: get player lsit
	* @param @return
	* @return ArrayList<String>    
	* @throws
	 */
	public ArrayList<String> getPlayerList() {
		return playerList;
	}

	/**
	 * 
	* @Title: getState 
	* @Description: get state
	* @param @return
	* @return int    
	* @throws
	 */
	public int getState() {
		return state;
	}

	/**
	 * 
	* @Title: setState 
	* @Description: set state
	* @param @param state
	* @return void    
	* @throws
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 
	* @Title: setPlayerList 
	* @Description: set player list
	* @return void    
	* @throws
	 */
	//Update player list
	public void setPlayerList(ArrayList<String> playerList) {
		System.out.println("Set PlayerList size: " + playerList.size());
		for (String s : playerList) {
			System.out.println("Player List element: " + s);
		}
		this.playerList = playerList;
	}

	/**
	 * 
	* @Title: startGame 
	* @Description: start game command
	* @param @param parent
	* @param @param gameTime
	* @return void    
	* @throws
	 */
	
	//Trigger start game process for a game server, only be used if player is host computer
	public void startGame(JFrame parent, int gameTime) {
		// For game server
		this.gameServer.startGame(parent, gameTime);

	}

	/**
	 * 
	* @Title: getGameWorld 
	* @Description: get the game world instance
	* @param @return
	* @return GameWorld    
	* @throws
	 */
	public GameWorld getGameWorld() {
		return gameWorld;
	}

	/**
	 * 
	* @Title: setGameWorld 
	* @Description: set the game world
	* @param @param gameWorld
	* @return void    
	* @throws
	 */
	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	/**
	 * 
	* @Title: getGameServer 
	* @Description: get the game server
	* @param @return
	* @return GameServer    
	* @throws
	 */
	public GameServer getGameServer() {
		return gameServer;
	}

	//The method that accept "Package", serialize the object and sent it through socket
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
