package com.darkdensity.command;

import java.io.IOException;
import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Constant;
import com.darkdensity.tile.Sprite;
import com.darkdensity.tile.Tile;

/**
 * 
* @ClassName: CreateCommand
* @Description: Create a new tile instance(sprite) and add it to the tile manager 
* @author Team A1 - Ting Yuen Lam
 */
public class CreateCommand extends CommandFactory implements Command {
	/* (non-Javadoc) 
	* <p>Title: execute</p> 
	* <p>Description: execute the command</p> 
	* @throws InstantiationException
	* @throws IllegalAccessException
	* @throws ClassNotFoundException
	* @throws IOException 
	* @see com.darkdensity.command.Command#execute() 
	*/ 
	
	@Override
	public void execute() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		Sprite sprite = (Sprite) Class.forName(Constant.TILE_PACKAGE + commandData.get("tileName")).newInstance();
		sprite.setX((Integer) commandData.get("pointX"));
		sprite.setY((Integer) commandData.get("pointY"));
		sprite.setTileManager(tileManager);
		sprite.setUUID((UUID) commandData.get("focusID"));
		tileManager.addTile((Tile) sprite);
	}
}
