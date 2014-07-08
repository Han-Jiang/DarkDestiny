package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;

/**
* @ClassName: ShowAllRevalCommand
* @Description: To set reval to full reval when user input cheat "how do you turn this on"
* @author Team A1 - Hei Yin Wong
*/

public class ShowAllRevalCommand extends CommandFactory implements Command{

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
		GameWorld.changeReval();
	}

	

}
