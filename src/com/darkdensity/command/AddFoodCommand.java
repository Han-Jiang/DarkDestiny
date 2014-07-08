package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Constant.PlayerRole;

/**
 * @ClassName: AddFoodCommand
 * @Description: GameCommand that implementing the adding food process when
 *               player entering cheat for adding food. According to the
 *               configuration in Cheat.java (SVN#438), the add food cheat is
 *               "food please"
 * @author Team A1 - Hei Yin Wong
 */

public class AddFoodCommand extends CommandFactory implements Command {

	/*
	 * (non-Javadoc) <p>Title: execute</p> <p>Description: execute the
	 * command</p>
	 * 
	 * @throws IOException
	 * 
	 * @throws InstantiationException
	 * 
	 * @throws IllegalAccessException
	 * 
	 * @throws ClassNotFoundException
	 * 
	 * @see com.darkdensity.command.Command#execute()
	 */

	@Override
	public void execute() throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		// Get player's team (the team who enter cheat for adding food)
		Team team = GameWorld.getTeam((PlayerRole) commandData
				.get("playerRole"));
		// Add 100 food to player's team
		team.addFood(100);
	}

}
