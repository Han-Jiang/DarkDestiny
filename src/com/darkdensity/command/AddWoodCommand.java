package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Constant.PlayerRole;

/**
 * @ClassName: AddWoodCommand
 * @Description: GameCommand that implementing the adding wood process when
 *               player entering the cheat for adding wood. According to the
 *               configuration in Cheat.java (SVN#438), the add iron cheat is
 *               "wood please"
 * @author Team A1 - Hei Yin Wong
 */

public class AddWoodCommand extends CommandFactory implements Command {

	/*
	 * (non-Javadoc) <p>Title: execute</p> <p>Description: </p>
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
		// Get player's team (the team who enter cheat for adding wood)
		Team team = GameWorld.getTeam((PlayerRole) commandData
				.get("playerRole"));

		// add 100 wood to player's team
		team.addWood(100);
	}

}
