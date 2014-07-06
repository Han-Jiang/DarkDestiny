package com.darkdensity.command;

import java.awt.Point;
import java.io.IOException;
import java.util.UUID;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.tile.Building;

/**
* @ClassName: AddFoodCommand
* @Description: command that adds the food
* @author Team A1 - Ting Yuen Lam
*/

public class AddFoodCommand extends CommandFactory implements Command {

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
		Team team = GameWorld.getTeam((PlayerRole) commandData
				.get("playerRole"));

		team.addFood(100);		
	}

}
