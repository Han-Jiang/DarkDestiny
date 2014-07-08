package com.darkdensity.net.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import java.util.*;

import com.darkdensity.command.Command;
import com.darkdensity.core.CommandPool;
import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Cheat;
import com.darkdensity.setting.Config;

/**
 * 
* @ClassName: ChatManager
* @Description: Chat manager manage all the fucntions related to chat
* @author Team A1 - Hei Yin Wong
* @date Mar 28, 2014 3:27:21 AM
 */
public class ChatManager {

	private MulticastSocket mcastSocket;
	private int ttl;
	private Cheat cheat;
	private CommandPool commandPool;
	private CommandFactory commandFactory;

	public ChatManager() {
		//Create a UDP multicast object
		cheat = new Cheat();
		try {
			mcastSocket = new MulticastSocket(Config.CHAT_PORT);
			ttl = mcastSocket.getTimeToLive();
			mcastSocket.joinGroup(InetAddress.getByName(Config.CHAT_GROUP));
			commandFactory = new CommandFactory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
	}

	public String receiveMessage() throws IOException {
		//Handle receiving message process
		byte buf[] = new byte[1024];
		DatagramPacket datapack = new DatagramPacket(buf, buf.length);
		mcastSocket.receive(datapack);
		String packStr = new String(datapack.getData(), 0, datapack.getLength());
		return packStr;
	}

	public void sendMessage(String message) throws Exception {
		//Send message
		message = Config.PLAYER_NAME + ": " + message;
		byte[] buf = message.getBytes();
		DatagramPacket pack = new DatagramPacket(buf, buf.length,
				InetAddress.getByName(Config.CHAT_GROUP), Config.CHAT_PORT);
		mcastSocket.send(pack);

	}

	public void closeSocket() {
		this.mcastSocket.close();
	}

	public void setCommandPool(GameWorld gameworld) {
		//Command is required for handling cheat
		// TODO Auto-generated method stub
		this.commandPool = gameworld.getCommandPool();
		this.commandFactory.setCommandPool(gameworld.getCommandPool());
		this.commandFactory.setGameWorld(gameworld);

	}
}
