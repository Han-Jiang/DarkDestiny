package com.darkdensity.command;

import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.BarricadeDirection;
import com.darkdensity.tile.Barricade;
import com.darkdensity.tile.Tile;

/**
 * 
 * @ClassName: BarricadeCommand
 * @Description: Command class that implements adding barricade into map. For
 *               detail, please see in-line comment
 * @author Team A1 - Ting Yuen Lam
 * @date 19 Mar 2014 11:45:22
 */
public class BarricadeCommand extends CommandFactory implements Command {

	/*
	 * (non-Javadoc) <p>Title: execute</p> <p>Description: execute the barricade
	 * command</p>
	 * 
	 * @throws InstantiationException
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws ClassNotFoundException
	 * 
	 * @see com.darkdensity.command.Command#execute()
	 */

	@Override
	public void execute() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		
		// Create a Barricade class based on tileName, which define what
		// barricade should be added, in this stage (SVN#438), there are 2
		// different type of barricade
		Barricade barricade = (Barricade) Class.forName(
				Constant.TILE_PACKAGE + commandData.get("tileName"))
				.newInstance();
		//Set the barricade location
		barricade.setX((Integer) commandData.get("pointX"));
		barricade.setY((Integer) commandData.get("pointY"));
		
		//Define the handlers for handling game process later
		barricade.setTileManager(tileManager);
		barricade.setFocusManager(focusManager);
		
		//Define barricade location
		barricade.setDirection((BarricadeDirection) commandData
				.get("barricadeDirection"));
		
		//Give a unique id to barricade for hand
		barricade.setUUID((UUID) commandData.get("focusID"));
		tileManager.addTile((Tile) barricade);

		//If player is game host, consume the resource for target team
		if (Config.IS_SERVER) {
			gameWorld.consumeTeamResource(barricade.getRole(),
					barricade.getConsumeResource());
		}
	}
}
