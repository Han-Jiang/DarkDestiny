package com.darkdensity.factory;

import java.util.HashMap;
import java.util.UUID;

import com.darkdensity.command.Command;
import com.darkdensity.core.CommandPool;
import com.darkdensity.core.FocusManager;
import com.darkdensity.core.GameWorld;
import com.darkdensity.core.TileManager;
import com.darkdensity.maprender.GridMap;
import com.darkdensity.net.core.Packet;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.BarricadeDirection;
import com.darkdensity.setting.Constant.PlayerRole;

/**
 * 
* @ClassName: CommandFactory
* @Description: command factory manage all the command
* @author Team A1 - Ting Yuen Lam
* @date Mar 27, 2014 11:15:32 PM
 */
public class CommandFactory {
	protected GameWorld gameWorld;
	protected TileManager tileManager;
	protected FocusManager focusManager;
	protected GridMap gridMap;
	private CommandPool commandPool;
	protected HashMap<String, Object> commandData;

	public CommandFactory() {
		commandData = new HashMap<String, Object>();
	}

	@SuppressWarnings("unchecked")
	public void setCommandData(HashMap<String, Object> commandData) {
		this.commandData = (HashMap<String, Object>) commandData.clone();
	}

	public void setBarricadeDirection(BarricadeDirection barricadeDirection) {
		commandData.put("barricadeDirection", barricadeDirection);
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.focusManager = gameWorld.getFocusManager();
		this.tileManager = gameWorld.getTileManager();
	}

	public void setPlayerRole(PlayerRole playerRole) {
		commandData.put("playerRole", playerRole);
	}

	public void setTileName(String tileName) {
		commandData.put("tileName", tileName);
	}

	public void setCommandPool(CommandPool commandPool) {
		this.commandPool = commandPool;
	}

	public void setGridMap(GridMap gridMap) {
		this.gridMap = gridMap;
	}

	public void setFocusID(UUID focusID) {
		commandData.put("focusID", focusID);
	}

	public void setTargetID(UUID targetID) {
		commandData.put("targetID", targetID);
	}

	public void setPointX(int pointX) {
		commandData.put("pointX", pointX);
	}

	public void setPointY(int pointY) {
		commandData.put("pointY", pointY);
	}

	public void setClassName(String className) {
		commandData.put("className", className);
	}

	public void setBuildingName(String buildingName) {
		commandData.put("buildingName", buildingName);
	}

	public void setProgressUpdate(int n) {
		commandData.put("progressUpdate", n);
	}

	public void setResourse(int food, int wood, int iron, int survivor) {
		commandData.put("food", food);
		commandData.put("wood", wood);
		commandData.put("iron", iron);
		commandData.put("survivor", survivor);
	}

	public Packet pack(String senderName) {
		commandData.put("className", Constant.COMMAND_PACKAGE
				+ this.getClass().getSimpleName());
		commandData.put("senderName", senderName);
		Packet packet = new Packet(commandData);
		return packet;
	}

	public Command unpack(Packet packet) {

		CommandFactory newCommand = null;
		try {
			newCommand = (CommandFactory) Class.forName(packet.getClassName())
					.newInstance();
			newCommand.setGameWorld(gameWorld);
			newCommand.setGridMap(gridMap);
			newCommand.setCommandData(packet.getCommandData());
		} catch (InstantiationException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
		this.clear();
		return (Command) newCommand;
	}

	public void createCommand(String className) {
		CommandFactory newCommand = null;
		try {
			newCommand = (CommandFactory) Class.forName(
					Constant.COMMAND_PACKAGE + className).newInstance();
			newCommand.setCommandData(commandData);
			newCommand.setGameWorld(gameWorld);
			newCommand.setGridMap(gridMap);
		} catch (InstantiationException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
		this.clear();
		commandPool.addCommand((Command) newCommand);
	}

	private void clear() {
		commandData.clear();
	}
}
