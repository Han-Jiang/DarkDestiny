package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Constant.TileManagerState;

/**
* @ClassName: ReadyCommand
* @Description: command to get the tile manger state ready
* @author Team A1 - Ting Yuen Lam
*/

public class ReadyCommand extends CommandFactory implements Command{

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
		tileManager.setState(TileManagerState.READY);
	}
}
