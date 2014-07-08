package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Constant.PlayerRole;

/**
 * @ClassName: AddIronCommand
 * @Description: GameCommand that implementing the adding iron process when
 *               player entering the cheat for adding iron. According to the
 *               configuration in Cheat.java (SVN#438), the add iron cheat is
 *               "iron please"
 * @author Team A1 - Hei Yin Wong
 */

public class AddIronCommand extends CommandFactory implements Command {

	/*
	 * (non-Javadoc) <p>Title: execute</p> <p>Description: excute the command
	 * </p>
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
		// Get player's team (the team who enter add iron cheat)
		Team team = GameWorld.getTeam((PlayerRole) commandData
				.get("playerRole"));
		// add 100 Iron to player's team
		team.addIron(100);
	}

}
