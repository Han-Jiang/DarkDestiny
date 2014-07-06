package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Constant.PlayerRole;

/**
* @ClassName: ConsumeResourceCommand
* @Description: command to consume resource
* @author Team A1 - Ting Yuen Lam
*/

public class ConsumeResourceCommand extends CommandFactory implements Command {
	
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
		Team team = GameWorld.getTeam((PlayerRole) commandData.get("playerRole"));
		team.minusFood((Integer) commandData.get("food"));
		team.minusWood((Integer) commandData.get("wood"));
		team.minusIron((Integer) commandData.get("iron"));
	}
}
