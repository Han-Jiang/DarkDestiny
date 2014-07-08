package com.darkdensity.command;

import java.io.IOException;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.net.core.NetConstant;
import com.darkdensity.net.core.NetUtil;
import com.darkdensity.setting.Config;

/**
 * 
* @ClassName: SettingCommand
* @Description: Change setting of the game 
* @author Team A1 - Hei Yin Wong
 */
public class SettingCommand extends CommandFactory implements Command{

	
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
		System.out.println("GAME_TIME_KEY: "+ commandData.get(NetConstant.GAME_TIME_KEY));
		Config.GAME_WINNING_TIME = (Long) commandData.get(NetConstant.GAME_TIME_KEY);
	}
	

}
