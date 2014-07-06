package com.darkdensity.net.lobby;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.darkdensity.core.NetworkManager;
import com.darkdensity.net.core.NetConstant;
import com.darkdensity.net.core.NetUtil;
import com.darkdensity.setting.Config;

public class GameLobbyServer extends GameLobby {
	private ServerSocket serverSocket;
	private ArrayList<Socket> clients;
	private Thread gameLoobyThread;

	public GameLobbyServer(NetworkManager networkManager) throws IOException {
		super(networkManager);
		this.serverSocket = new ServerSocket(NetConstant.LOBBY_PORT);
		this.clients = new ArrayList<Socket>();
		gameLoobyThread = new Thread(gameLobbyRunnable);
		gameLoobyThread.start();
	}

	@Override
	public void sendRequest(GameRequest request) {
		System.out.println("Send request: " + request.getClass());
		byte[] message;
		for (Socket sc : clients) {
			try {
				DataOutputStream dataOutputStream = new DataOutputStream(
						sc.getOutputStream());
				message = NetUtil.serialize(request);
				dataOutputStream.writeInt(message.length);
				dataOutputStream.flush();
				dataOutputStream.write(message);
				dataOutputStream.flush();

			} catch (IOException e) {
				if (Config.DEBUGMODE) {
					e.printStackTrace();
				}
			}

		}
	}

	private Runnable gameLobbyRunnable = new Runnable() {
		DataInputStream dataInputStream;

		@Override
		public void run() {
			try {
				while (networkManager.getState() == 1) {
					Socket socket = serverSocket.accept();
					clients.add(socket);
					GameLobbyRequestReceiver gameLobbyRequestReceiver = new GameLobbyRequestReceiver(
							networkManager, socket);
					new Thread(gameLobbyRequestReceiver).start();
				}

			} catch (IOException e) {
				if (Config.DEBUGMODE) {
					e.printStackTrace();
				}
			}
		}
	};

	public void stop() {
		try {
			System.out.println("Game Lobby stopping...");
			this.serverSocket.close();
		} catch (IOException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
	}
}
