package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.PlayerRole;

/**
* @ClassName: ConsumeFoodCommand
* @Description: TODO(What the class do)
* @author Team A1 - Hei Yin Wong
*/

public class ConsumeFoodCommand extends CommandFactory implements Command{

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
		
		Team team = GameWorld.getTeam(PlayerRole.SURVIVOR);

		team.addFood(0-team.getNumOfSprite() * Config.SURVIVOR_CONSUMPTION);
		
		if(team.getFood() <= 0)
		{
			//It doesn't make sense for survivor have negative number of food 
			//so if survivor do not have enough food, set it to 0 
			team.setFood(0);
		}
	}
	

}
