package com.darkdensity.command;

import java.io.IOException;
import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Resource;
import com.darkdensity.tile.Building;

/**
 * 
* @ClassName: GenernateResourceCommand
* @Description: Generate resource in the building every certain period of time 
* @author Team A1 - Ting Yuen Lam
 */
public class GenernateResourceCommand extends CommandFactory implements Command {
	/* (non-Javadoc) 
	* <p>Title: execute</p> 
	* <p>Description: </p> 
	* @throws IOException
	* @throws InstantiationException
	* @throws IllegalAccessException
	* @throws ClassNotFoundException 
	* @see com.darkdensity.command.Command#execute() 
	*/ 
	
	@Override
	public void execute() throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Building t = tileManager.getBuilding((UUID) commandData.get("focusID"));
		Resource resource = new Resource((Integer) commandData.get("food"), (Integer) commandData.get("iron"), (Integer) commandData.get("wood"), (Integer) commandData.get("survivor"));
		t.setResource(resource);
	}
}
