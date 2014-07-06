package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Constant.PlayerRole;

/**
* @ClassName: GameEndCommand
* @Description: command to end game
* @author Team A1 - Ting Yuen Lam
*/

public class GameEndCommand extends CommandFactory implements Command{

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
		gameWorld.gameEnd((PlayerRole) commandData.get("playerRole"));
	}
}
