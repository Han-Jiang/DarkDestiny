package com.darkdensity.net.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import com.darkdensity.setting.Constant.BarricadeDirection;

public class Packet implements Serializable {
	private static final long serialVersionUID = 1L;
//	private String senderName;
//	private String className;
//	private UUID focusID, targetID;
//	private int pointX, pointY;
//	private String tileName;
//	private BarricadeDirection barricadeDirection;
//	private String buildingName;
	private HashMap<String, Object> commandData;

	public Packet(HashMap<String, Object>commandData){
		this.commandData = (HashMap<String, Object>) commandData.clone();
	}
	
//	public Packet(String senderName, String className, UUID focusID,
//			UUID targetID, int pointX, int pointY, String tileName, BarricadeDirection barricadeDirection, String buildingName,HashMap<String, Object>commandData) {
//		super();
//		this.senderName = senderName;
//		this.className = className;
//		this.focusID = focusID;
//		this.targetID = targetID;
//		this.pointX = pointX;
//		this.pointY = pointY;
//		this.tileName = tileName;
//		this.barricadeDirection = barricadeDirection;
//		this.buildingName = buildingName;
//		this.commandData = commandData;
//	}

	public String getClassName() {
		return (String) commandData.get("className");
	}
//
//	public UUID getFocusID() {
//		return (UUID) commandData.get("focusID");
//	}
//
//	public UUID getTargetID() {
//		return (UUID) commandData.get("targetID");
//	}
//
//	public int getPointX() {
//		return (Integer) commandData.get("pointX");
//	}
//
//	public int getPointY() {
//		return (Integer) commandData.get("pointY");
//	}
//
//	public String getTileName() {
//		return (String) commandData.get("tileName");
//	}
//
//	public BarricadeDirection getBarricadeDirection() {
//		return (BarricadeDirection) commandData.get("barricadeDirection");
//	}
//
	public String getSenderName() {
		return (String) commandData.get("senderName");
	}
//
//	public String getBuildingName() {
//		return (String) commandData.get("buildingName");
//	}
//	
	public HashMap<String, Object> getCommandData(){
		return commandData;
	}
}
