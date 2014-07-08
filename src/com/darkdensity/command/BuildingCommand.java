package com.darkdensity.command;

import java.io.IOException;
import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Constant;
import com.darkdensity.tile.Building;
import com.darkdensity.tile.Tile;

/**
 * 
 * @ClassName: BuildingCommand
 * @Description: Building command , set the location and direction of the
 *               buidling and add it to the map
 * @author Team A1 - Ting Yuen Lam
 */
public class BuildingCommand extends CommandFactory implements Command {

	/*
	 * (non-Javadoc) <p>Title: execute</p> <p>Description: execute the building
	 * command</p>
	 * 
	 * @throws InstantiationException
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws ClassNotFoundException
	 * 
	 * @throws IOException
	 * 
	 * @see com.darkdensity.command.Command#execute()
	 */

	@Override
	public void execute() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {
		Building building = (Building) Class.forName(
				Constant.TILE_PACKAGE + commandData.get("tileName"))
				.newInstance();
		building.setX((Integer) commandData.get("pointX"));
		building.setY((Integer) commandData.get("pointY"));
		building.setTileManager(tileManager);
		building.setFocusManager(focusManager);
		building.setUUID((UUID) commandData.get("focusID"));
		building.initBuildingImage((String) commandData.get("buildingName"));
		tileManager.addTile((Tile) building);
	}
}