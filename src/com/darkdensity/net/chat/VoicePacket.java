package com.darkdensity.net.chat;

import java.io.Serializable;

import com.darkdensity.setting.Config;
/** 
* @author Team A1 - Hei Yin Wong
* @description A instance that used to store voice message and sender name,
*  it will be used in sending voice message 
*/
public class VoicePacket implements Serializable{

	private byte[] message;
	private String sender;
	
	public VoicePacket(byte [] message){
		this.sender = Config.PLAYER_NAME;
		this.message = message;
	}
	
	public static VoicePacket pack(byte [] message){
		return new VoicePacket(message);
	}

	public byte[] getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}
	
	

}
