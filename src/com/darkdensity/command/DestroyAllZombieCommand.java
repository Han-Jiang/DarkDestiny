package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.tile.Tile;

/**
* @ClassName: DestroyAllZombieCommand
* @Description: command to destroy all zombie
* @author Team A1 - Ting Yuen Lam
*/

public class DestroyAllZombieCommand extends CommandFactory implements Command {
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
		for (Tile t : tileManager.getZombies()) {
			tileManager.removeTile(t);
		}
	}
}
