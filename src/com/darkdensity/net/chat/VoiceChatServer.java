package com.darkdensity.net.chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.darkdensity.net.core.NetUtil;
/**
 * 
* @ClassName: ChatManager
* @Description: Chat manager manage all the fucntions related to chat
* @author Team A1 - Hei Yin Wong
* @date Mar 28, 2014 3:27:21 AM
 */
public class VoiceChatServer extends VoiceChatManager {

	private ServerSocket serverSocket;
	private ArrayList<Socket> clients;
	private ArrayList<VoiceMessageReceiver> receivers;
	

	public VoiceChatServer() {
		super();
		
		try {
			this.serverSocket = new ServerSocket(3001);
			clients = new ArrayList<Socket>();
			receivers = new ArrayList<VoiceMessageReceiver>();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						Socket socket = serverSocket.accept();
						clients.add(socket);
						VoiceMessageReceiver receiver = new VoiceMessageReceiver(VoiceChatServer.this,socket,true);
						new Thread(receiver).start();
						VoiceChatServer.this.receivers.add(receiver);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		};

		new Thread(runnable).start();

	}

	@Override
	public void sendVoiceMessage() {
		// TODO Auto-generated method stub
		System.out.println("Send message in voice Chat server");
		//VoicePacket vp = new VoicePacket(this.getCapturedAudio());
		//sendVoiceMessage(vp);
		this.sendVoiceMessage(this.getCapturedAudio());
	}
	
	//public void sendVoiceMessage(VoicePacket vp){
	public void sendVoiceMessage(byte [] message){
		try {
			
			//byte[] message = NetUtil.serialize(vp);
			//byte [] message = vp.getMessage();
			int messageLenth = message.length;
			System.out.println("*****Voice Packet ******");
			System.out.println("Length: "+ messageLenth);
//			this.playAudio(message);
			System.out.println("========================");
			for (Socket sc : clients) {
				DataOutputStream dataOutputStream = new DataOutputStream(
						sc.getOutputStream());
				dataOutputStream.writeInt(messageLenth);
				dataOutputStream.write(message);
				dataOutputStream.flush();
				
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	public void stopReceiveMessage(){
		for(VoiceMessageReceiver vmr : this.receivers){
			vmr.stopReceiveMessage();
		}
	}

}
