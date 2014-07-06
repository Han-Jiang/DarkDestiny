package com.darkdensity.command;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import com.darkdensity.core.TileManager;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.maprender.GridMap;
import com.darkdensity.tile.Sprite;
import com.darkdensity.tile.Tile;


/**
 * 
* @ClassName: MoveCommand
* @Description: move the survivor to destination
* @author Team A1 - Ting Yuen Lam
 */
public class MoveCommand extends CommandFactory implements Command {
	
	/* (non-Javadoc) 
	* <p>Title: execute</p> 
	* <p>Description: execute the command</p> 
	* @throws IOException 
	* @see com.darkdensity.command.Command#execute() 
	*/ 
	
	@Override
	public void execute() throws IOException {
		Sprite t = tileManager.getSurivor((UUID) commandData.get("focusID"));
		if(t == null){
			t = tileManager.getZombie((UUID) commandData.get("focusID"));
		}
		if(t != null){
			t.setDestination((Integer) commandData.get("pointX"), (Integer) commandData.get("pointY"));
		}
	}
}