package com.darkdensity.command;

import java.awt.Point;
import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Resource;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.BarricadeDirection;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.tile.Barricade;
import com.darkdensity.tile.Tile;

/**
 * 
* @ClassName: BarricadeCommand
* @Description: Barricade command , set the location and direction 
* of the barricade and add it to the map
* @author Team A1
* @date 19 Mar 2014 11:45:22
 */
public class BarricadeCommand extends CommandFactory implements Command{
	
	/* (non-Javadoc) 
	* <p>Title: execute</p> 
	* <p>Description: execute the barricade command</p> 
	* @throws InstantiationException
	* @throws IllegalAccessException
	* @throws ClassNotFoundException 
	* @see com.darkdensity.command.Command#execute() 
	*/ 
	
	@Override
	public void execute() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Barricade barricade = (Barricade) Class.forName(Constant.TILE_PACKAGE + commandData.get("tileName")).newInstance();
		barricade.setX((Integer) commandData.get("pointX"));
		barricade.setY((Integer) commandData.get("pointY"));
		barricade.setTileManager(tileManager);
		barricade.setFocusManager(focusManager);
		barricade.setDirection((BarricadeDirection) commandData.get("barricadeDirection"));
		barricade.setUUID((UUID) commandData.get("focusID"));
		tileManager.addTile((Tile) barricade);		
		
		if(Config.IS_SERVER){
			gameWorld.consumeTeamResource(barricade.getRole(), barricade.getConsumeResource());
		}
	}
}
