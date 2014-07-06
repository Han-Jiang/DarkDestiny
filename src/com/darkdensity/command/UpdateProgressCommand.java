package com.darkdensity.command;

import java.io.IOException;
import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.tile.Tile;

/**
 * 
* @ClassName: UpdateProgressCommand
* @Description: Progress of the tile
* @author Team A1 - Ting Yuen Lam
 */
public class UpdateProgressCommand extends CommandFactory implements Command{

	/* (non-Javadoc) 
	* <p>Title: execute</p> 
	* <p>Description: execute the command </p> 
	* @throws IOException
	* @throws InstantiationException
	* @throws IllegalAccessException
	* @throws ClassNotFoundException 
	* @see com.darkdensity.command.Command#execute() 
	*/ 
	
	@Override
	public void execute() throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Tile tile = tileManager.getTile((UUID) commandData.get("focusID"));
		tile.progressUpdate((Integer) commandData.get("progressUpdate"));
	}
}
