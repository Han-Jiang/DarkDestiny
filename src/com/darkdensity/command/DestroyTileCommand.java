package com.darkdensity.command;

import java.io.IOException;
import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.tile.Tile;

/**
* @ClassName: DestroyTileCommand
* @Description: command to destroy tile
* @author Team A1 - Ting Yuen Lam
* @date 25 Mar 2014 04:12:48
*/

public class DestroyTileCommand extends CommandFactory implements Command {
	
	/* (non-Javadoc) 
	* <p>Title: execute</p> 
	* <p>Description: execute the command</p> 
	* @throws IOException
	* @throws InstantiationException
	* @throws IllegalAccessException
	* @throws ClassNotFoundException 
	* @see com.darkdensity.command.Command#execute() 
	*/ 
	
	@Override
	public void execute() throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Tile t = tileManager.getTile((UUID) commandData.get("focusID"));
		tileManager.removeTile(t);	
	}
}
