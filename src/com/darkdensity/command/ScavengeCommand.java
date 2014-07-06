package com.darkdensity.command;

import java.awt.Point;
import java.util.UUID;

import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.tile.Building;

/**
 * 
 * @ClassName: ScavengeCommand
 * @Description: scavenge resources in the building, including food, wood and
 *               iron
 * @author Team A1 - Ting Yuen Lam
 */
public class ScavengeCommand extends CommandFactory implements Command {
	
	/* (non-Javadoc) 
	* <p>Title: execute</p> 
	* <p>Description: execute the command</p>  
	* @see com.darkdensity.command.Command#execute() 
	*/ 
	
	@Override
	public void execute() {
		Team team = GameWorld.getTeam((PlayerRole) commandData
				.get("playerRole"));
		team.addFood((Integer) commandData.get("food"));
		team.addWood((Integer) commandData.get("wood"));
		team.addIron((Integer) commandData.get("iron"));
		Building building = tileManager.getBuilding((UUID) commandData
				.get("focusID"));
		building.clearResurce();

		if (Config.IS_SERVER && (Integer) commandData.get("survivor") > 0) {
			for (int i = 0; i < (Integer) commandData.get("survivor"); i++) {
				Point p = gridMap.getClosestPoint(
						building.getX() + building.getTileWidth() / 2,
						building.getY() + building.getTileHeight() / 2);
				tileManager
						.createSprite(
								((PlayerRole) commandData.get("playerRole") == PlayerRole.SURVIVOR) ? "Survivor1"
										: "Zombie1", p.x, p.y);
			}
		}

		// serve will generate building command
		if (Config.IS_SERVER) {

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(Constant.getResourceGenerateTime());
						tileManager.generateBuildingResource((UUID) commandData
								.get("focusID"));
					} catch (InterruptedException e) {
						if (Config.DEBUGMODE) {
							e.printStackTrace();
						}
					}

				}
			};
			Thread t = new Thread(runnable);
			t.start();
		}
	}
}
