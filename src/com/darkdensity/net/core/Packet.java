package com.darkdensity.net.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import com.darkdensity.setting.Constant.BarricadeDirection;
/** 
* @author Team A1 - Hei Yin Wong
*/
public class Packet implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<String, Object> commandData;

	public Packet(HashMap<String, Object>commandData){
		this.commandData = (HashMap<String, Object>) commandData.clone();
	}
	

	public String getClassName() {
		return (String) commandData.get("className");
	}

	public String getSenderName() {
		return (String) commandData.get("senderName");
	}

	public HashMap<String, Object> getCommandData(){
		return commandData;
	}
}
