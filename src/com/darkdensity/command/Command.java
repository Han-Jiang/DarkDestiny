package com.darkdensity.command;

import java.io.IOException;

/**
 * 
* @ClassName: Command Interface
* @Description: Command Interface for different kind of command to be executed by themselves
* @author Team A1 - Ting Yuen Lam
 */
public interface Command {
	public void execute() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException;
}
	