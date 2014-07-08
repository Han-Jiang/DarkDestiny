package com.darkdensity.tile;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.darkdensity.core.FocusManager;
import com.darkdensity.core.TileManager;
import com.darkdensity.player.Resource;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.BarricadeDirection;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.util.ImageLoader;

public class Barricade extends Tile {
	protected BarricadeDirection direction;
	protected BufferedImage[] bufferedImages;
	protected HashMap<BarricadeDirection, ArrayList<Point>> blockingMap;
	protected int level;
	// protected int defense;
	protected int imageOffset;
	protected FocusManager focusManager;
	protected boolean destroyed = false;
	protected String highlight = "_highlight_"
			+ ((Config.PLAYER_ROLE == PlayerRole.SURVIVOR) ? "green" : "red")
			+ ".png";
	
	protected Resource consumeResource;

	public Barricade() {
		this.setLocation(x, y);
		this.level = 0;
		this.direction = BarricadeDirection.LEFT;
		this.health = 1;
		this.role = PlayerRole.SURVIVOR;
	}

	public Barricade(int x, int y, TileManager tileManager) throws IOException {
		this.x = x;
		this.y = y;
		this.tileManager = tileManager;
		this.setLocation(x, y);
		this.level = 0;
		this.direction = BarricadeDirection.LEFT;
		focusManager = tileManager.getGameWorld().getFocusManager();
	}

	/**
	 * whether the barricade has been destroy
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/**
	 * 
	* @Title: upgrade 
	* @Description: upgrade the barricade
	* @param 
	* @return void    
	* @throws
	 */
	public void upgrade() {
		if (this.level < Constant.BARRICADE_MAX_LEVEL - 1) {
			this.level++;
		}
	}

	/**
	 * get the bufferedImage for drawing 
	 */
	public BufferedImage getImage() {
		Image i = bufferedImages[((focusing || entered) ? imageOffset : 0)
				+ direction.ordinal() * Constant.BARRICADE_MAX_LEVEL + level];
		return (BufferedImage) ImageLoader.resize(i, tileWidth, tileHeight);
	}

	public void setDirection(BarricadeDirection direction) {
		this.direction = direction;
	}

	/**
	 * 
	* @Title: getBlockingArea 
	* @Description: get the blocking area of the barricade
	* @param @return
	* @return ArrayList<Point>    
	* @throws
	 */
	public ArrayList<Point> getBlockingArea() {
		int centerX = x + tileWidth / 2;
		int centerY = y + tileHeight / 2;
		ArrayList<Point> blockingArea = new ArrayList<Point>();
		for (Point p : blockingMap.get(direction)) {
			blockingArea.add(new Point(centerX + p.x * Constant.TILE_WIDTH,
					centerY + p.y * Constant.TILE_HEIGHT));
		}
		return blockingArea;
	}

	/**
	 * update the progress of the barricade
	 */
	@Override
	public void progressUpdate(int n) {
		if (health != maxHealth && level != Constant.BARRICADE_MAX_LEVEL
				&& !isDestroyed()) {
			this.updateHealth(n / 5);
			if (health > maxHealth / (Constant.BARRICADE_MAX_LEVEL - level)) {
				this.upgrade();
			}
		}
	}

	/**
	 * 
	* @Title: getConsumeResource 
	* @Description: get consume resource of the barricade
	* @param @return
	* @return Resource    
	* @throws
	 */
	public Resource getConsumeResource() {
		return consumeResource;
	}
}
