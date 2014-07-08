package com.darkdensity.net.lobby;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.darkdensity.core.NetworkManager;
import com.darkdensity.net.core.NetConstant;
import com.darkdensity.net.core.NetUtil;
import com.darkdensity.setting.Config;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class GameLobbyClient extends GameLobby {

	private Socket socket;
	private Thread receiverThread;

	public GameLobbyClient(NetworkManager networkManager, String ipAddress)
			throws IOException {
		super(networkManager);
		this.socket = new Socket();
		socket.connect(
				new InetSocketAddress(ipAddress, NetConstant.LOBBY_PORT),
				NetConstant.TIMEOUT);
		GameLobbyRequestReceiver receiverRunnable = new GameLobbyRequestReceiver(
				networkManager, socket);
		receiverThread = new Thread(receiverRunnable);
		receiverThread.start();
	}

	@Override
	public void sendRequest(GameRequest request) {
		byte[] message;
		try {
			DataOutputStream dataOutputStream = new DataOutputStream(
					socket.getOutputStream());
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

	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
			 if(Config.DEBUGMODE){e.printStackTrace();}
		}
	}

}
