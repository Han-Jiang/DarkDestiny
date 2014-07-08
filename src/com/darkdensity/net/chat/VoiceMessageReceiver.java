package com.darkdensity.net.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.darkdensity.net.core.NetUtil;
import com.darkdensity.setting.Config;
/** 
* @author Team A1 - Hei Yin Wong
* @Decription handler for receiving voice message
*/
public class VoiceMessageReceiver implements Runnable {

	DataInputStream dataInputStream;
	private boolean isReceiveVoiceMessage;
	private boolean isServer;
	private VoiceChatManager cm;
	private Socket socket;

	

	//init resource
	public VoiceMessageReceiver(VoiceChatManager cm, Socket socket,
			boolean isServer) {
		// TODO Auto-generated constructor stub
		try {
			dataInputStream = new DataInputStream(socket.getInputStream());
			isReceiveVoiceMessage = true;
			this.isServer = isServer;
			this.cm = cm;
			this.socket = socket;
		} catch (IOException e) {
			if(Config.DEBUGMODE){e.printStackTrace();}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("receive message is running");
		
		//Keep listen on port 3001, and trigger play audio process when received a message
		while (isReceiveVoiceMessage) {
			try{
			int messageLength = 0;
			while (true) {
				try {
					messageLength = dataInputStream.readInt();
					break;
				} catch (Throwable t) {
					continue;
				}
			}
			byte[] message = new byte[messageLength];
			dataInputStream.readFully(message);
			// VoicePacket vp = (VoicePacket) NetUtil.deserialize(message);
			
			
			
			System.out.println("Voice mail received");
			if (isServer) {
				((VoiceChatServer) cm).sendVoiceMessage(message);
				System.out.println("Voice message is forward");
			}

			/*
			 * if (vp.getSender().equals(Config.PLAYER_NAME)) { return; }
			 */

			System.out.println("received message length: " + message.length);
			// cm.playAudio(vp.getMessage());
			cm.playAudio(message);
			System.out.println("play message done");
			

			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}

	public void stopReceiveMessage() {
		this.isReceiveVoiceMessage = false;
	}

}
