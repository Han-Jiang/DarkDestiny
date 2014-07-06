package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Constant.PlayerRole;

/**
* @ClassName: AddWoodCommand
* @Description: TODO(What the class do)
* @author Team A1
*/

public class AddWoodCommand extends CommandFactory implements Command{

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
		Team team = GameWorld.getTeam((PlayerRole) commandData
				.get("playerRole"));

		team.addWood(100);		
	}

}
