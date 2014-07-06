package com.darkdensity.tile;

import java.awt.Graphics;
import java.io.IOException;

import com.darkdensity.core.TileManager;
import com.darkdensity.player.Resource;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;

public class SmallBarricade extends Barricade {
	public SmallBarricade() throws IOException{
		super();
		this.tileWidth = 60;
		this.tileHeight = 60;
		this.setSize(tileWidth, tileHeight);
		this.imageOffset =  2 * Constant.BARRICADE_MAX_LEVEL;
		this.bufferedImages = Constant.getFrames(Config.BARRICADE_PATH + "smallBarricade.png",  Config.BARRICADE_PATH + "smallBarricade" + highlight , 2, Constant.BARRICADE_MAX_LEVEL);
		this.blockingMap = Constant.SB_BLOCKINGAREA;
		this.maxHealth = Config.SB_MAX_HEALTH;
		this.consumeResource = Config.SB_RESOURCE;
//		this.tileWidth = this.getImage().getWidth();
//		this.tileHeight = this.getImage().getHeight();
//		this.setSize(tileWidth, tileHeight);
	}
	
	public SmallBarricade(int x, int y, TileManager spriteManager)
			throws IOException {
		super(x, y, spriteManager);
		this.tileWidth = 60;
		this.tileHeight = 60;
		this.setSize(tileWidth, tileHeight);
		this.imageOffset =  2 * Constant.BARRICADE_MAX_LEVEL;
		this.bufferedImages = Constant.getFrames(Config.BARRICADE_PATH + "smallBarricade.png",  Config.BARRICADE_PATH + "smallBarricade" + highlight, 2, Constant.BARRICADE_MAX_LEVEL);
		this.consumeResource = Config.SB_RESOURCE;
		//		blockingArea = Constant.SB_BLOCKINGAREA;
	}
	
	@Override
	public void paintComponents(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponents(g);
	}
}
