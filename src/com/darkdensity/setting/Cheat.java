package com.darkdensity.setting;

import java.util.HashMap;

/**
 * 
* @ClassName: Cheat
* @Description: add cheat fucntion of the game
* @author Team A1
* @date Mar 28, 2014 6:32:55 AM
 */
public class Cheat extends HashMap<String, String>{
	public Cheat(){
		this.put("how do you turn this on", "ShowAllRevalCommand");
		this.put("food please", "AddFoodCommand");
		this.put("wood please", "AddWoodCommand");
		this.put("iron please", "AddIronCommand");
		this.put("never gonna give you up", "DestroyAllZombieCommand");
	}
}
