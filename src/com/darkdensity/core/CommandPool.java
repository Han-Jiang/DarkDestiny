package com.darkdensity.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.darkdensity.command.Command;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.net.core.GameServer;
import com.darkdensity.net.core.NetConstant;
import com.darkdensity.net.core.Packet;
import com.darkdensity.setting.Config;

/**
 * 
* @ClassName: CommandPool
* @Description: Class for handling the command
* @author Team A1 - Ting Yuen Lam
 */
public class CommandPool {
	//command to be executed
	private ArrayList<Command> commandPool;
	//commands that is executing
	private ArrayList<Command> executingCommand;
	//commands that has been executed
	private ArrayList<Command> executedCommand;
	
	private GameWorld gameWorld;
	private NetworkManager networkManager;
	private GameServer gameServer;
	private boolean isMultiplayer;
	private boolean isServer;

	/**
	* <p>Title: </p> 
	* <p>Description: </p>
	* @param gameWorld Construct with game world
	 */
	public CommandPool(GameWorld gameWorld) {
		commandPool = new ArrayList<Command>();
		this.gameWorld = gameWorld;
	}

	/**
	 * 
	* @Title: setNetworkManager 
	* @Description: Set the network manger
	* @param @param networkManager
	* @return void    
	* @throws
	 */
	public void setNetworkManager(NetworkManager networkManager) {
		this.networkManager = gameWorld.getNetworkManager();

		isMultiplayer = !(networkManager == null);
		if (isMultiplayer) {
			this.isServer = networkManager.getServerSocket() != null;
		}
	}

	/**
	 * 
	* @Title: getCommandPool 
	* @Description: get the command pool
	* @param @return 
	* @return ArrayList<Command>    
	* @throws
	 */
	public ArrayList<Command> getCommandPool() {
		return commandPool;
	}

	/**
	 * 
	* @Title: setCommandPool 
	* @Description: set the command pool
	 */
	public void setCommandPool(ArrayList<Command> commandPool) {
		this.commandPool = commandPool;
	}

	/**
	* @Title: addCommand 
	* @Description: add command and send it through network
	* @param @param command
	* @return void    
	* @throws
	 */
	public void addCommand(Command command) {
		if (isMultiplayer) {
			Packet packet = ((CommandFactory) command).pack(Config.PLAYER_NAME);
			if (isServer) {
				networkManager.getGameServer().sendNetworkCommand(packet);
			} else {
				networkManager.sendNetworkCommand(packet);
			}
		}

		commandPool.add(command);
	}

	/**
	 * 
	* @Title: addNetworkCommand 
	* @Description: add an networking command in command pool
	* @param packet a packet of commands
	* @return void    
	* @throws
	 */
	public void addNetworkCommand(Packet packet) {
		//if is himself,ignore 
		if (packet.getSenderName().equals(Config.PLAYER_NAME)) {
			return;
		}
		try {
			CommandFactory commandFactory = gameWorld.getCommandFactory();
			Command command = commandFactory.unpack(packet);
			commandPool.add(command);

			this.execute(); //heiyin added at 355
		} catch (Throwable t) {
			t.printStackTrace();
			System.out
					.println("error in adding networking command in command pool");
		}

	}

	/**
	 * 
	* @Title: execute 
	* @Description: Execute the command in the command pool
	* @param @throws IOException
	* @param @throws InstantiationException
	* @param @throws IllegalAccessException
	* @param @throws ClassNotFoundException
	* @return void    
	* @throws
	 */
	public void execute() throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		executingCommand = new ArrayList<Command>(commandPool);
		commandPool.clear();
		executedCommand = new ArrayList<Command>();
		for (Command command : executingCommand) {
		//	System.out.println("executing: "+command.getClass().getName());
			command.execute();
			executedCommand.add(command);
		}
		executingCommand.removeAll(executedCommand);
		executedCommand.clear();
	}
}
