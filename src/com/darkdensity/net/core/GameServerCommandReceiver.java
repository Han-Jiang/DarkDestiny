package com.darkdensity.net.core;

import java.io.DataInputStream;
import java.net.Socket;

import com.darkdensity.core.CommandPool;
import com.darkdensity.core.GameWorld;
import com.darkdensity.core.NetworkManager;
import com.darkdensity.setting.Config;

public class GameServerCommandReceiver implements Runnable {

	private Socket socket;
	private NetworkManager networkManager;
	private CommandPool commandPool;

	public GameServerCommandReceiver(GameWorld gameWorld,
			NetworkManager networkManager, Socket socket) {

		this.socket = socket;
		this.networkManager = networkManager;
		this.commandPool = gameWorld.getCommandPool();
	}

	@Override
	public void run() {
		try {
			int messageLength = 0;
			byte[] message;

			while (true) {

				DataInputStream dataInputStream = new DataInputStream(
						socket.getInputStream());
				while (true) {
					try {
						messageLength = dataInputStream.readInt();
						break;
					} catch (Throwable t) {
						continue;
					}
				}

				message = new byte[messageLength];

				dataInputStream.read(message);
				Packet commandPacket = (Packet) NetUtil.deserialize(message);

				// System.out.println("Command Received: "+
				// commandPacket.getClassName());
				this.commandPool.addNetworkCommand(commandPacket);
				GameServer gServer = this.networkManager.getGameServer();
				if (gServer != null) {
					gServer.sendNetworkCommand(commandPacket);
					// System.out.println("Command Sent: "+
					// commandPacket.getClassName());
				}

			}
		} catch (Throwable e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}

	}
}
