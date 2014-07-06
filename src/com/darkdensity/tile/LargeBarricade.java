package com.darkdensity.tile;

import java.io.IOException;

import com.darkdensity.core.TileManager;
import com.darkdensity.player.Resource;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.PlayerRole;

public class LargeBarricade extends Barricade {
	public LargeBarricade() throws IOException{
		super();
		this.tileWidth = 120;
		this.tileHeight = 120;
		this.setSize(tileWidth, tileHeight);
		this.imageOffset =  2 * Constant.BARRICADE_MAX_LEVEL;
		this.bufferedImages = Constant.getFrames(Config.BARRICADE_PATH + "largeBarricade.png", Config.BARRICADE_PATH + "largeBarricade" + highlight, 2, Constant.BARRICADE_MAX_LEVEL);
		this.blockingMap = Constant.LB_BLOCKINGAREA;
		this.maxHealth = Config.LB_MAX_HEALTH;
		this.consumeResource = Config.LB_RESOURCE;
	}
	
	public LargeBarricade(int x, int y, TileManager spriteManager)
			throws IOException {
		super(x, y, spriteManager);
		this.tileWidth = 120;
		this.tileHeight = 120;
		this.setSize(tileWidth, tileHeight);
		this.imageOffset =  2 * Constant.BARRICADE_MAX_LEVEL;
		this.bufferedImages = Constant.getFrames(Config.BARRICADE_PATH + "largeBarricade.png", Config.BARRICADE_PATH + "largeBarricade" + highlight , 2, Constant.BARRICADE_MAX_LEVEL);
		this.consumeResource = Config.LB_RESOURCE;
		//		blockingArea = Constant.SB_BLOCKINGAREA;
	}
}
