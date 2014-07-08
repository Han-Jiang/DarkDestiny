package com.darkdensity.net.chat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.darkdensity.setting.Config;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class VoiceChatClient extends VoiceChatManager {

	private Socket socket;
	private VoiceMessageReceiver receiver;

	public VoiceChatClient(String ipAddress) {
		//Create a socket to handling voice chat
		super();
		try {
			System.out.println("Voice Chat Client init");
			this.socket = new Socket();
			this.socket.connect(new InetSocketAddress(ipAddress, 3001), 10000);
			this.receiver = new VoiceMessageReceiver(this, socket, false);
			new Thread(receiver).start();

		} catch (IOException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void sendVoiceMessage() {
		//Send out a voice packet 
		System.out.println("Send message in voice Chat client");
		try {
			// VoicePacket vp = new VoicePacket(this.getCapturedAudio());

			DataOutputStream dataOutputStream = new DataOutputStream(
					socket.getOutputStream());
			byte[] message = this.getCapturedAudio();
			int messageLength = message.length;
			//debug message
			System.out.println("*****Voice Packet ******");
			System.out.println("Length: " + messageLength);
			//
			System.out.println("========================");

			dataOutputStream.writeInt(messageLength);
			dataOutputStream.write(message);
			dataOutputStream.flush();
		} catch (IOException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}

	}

	public void stopReceiveMessage() {
	
		receiver.stopReceiveMessage();
	}
}
