package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Constant.TileManagerState;

/**
* @ClassName: ReadyCommand
* @Description: command that set the tile manger state ready, it is used by server to notify client to start game
* @author Team A1 - Hei Yin Wong
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
