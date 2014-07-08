package com.darkdensity.core;

/**
 * @ClassName: GridMapManager
 * @Description: TODO(What the class do)
 * @author Team A1 - Ting Yuen Lam
 * @date 24 Mar 2014 23:01:48
 */


import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.darkdensity.maprender.GridMap;
import com.darkdensity.setting.Constant;

// for recording the sprites on grid, doesn't needs to render them in the picture( for now)
public class GridMapManager {
	public static GridMap gridMap;
	
	public GridMapManager(int mapWidth, int mapHeight,GameWorld gameWorld) throws FileNotFoundException, IOException {
		//System.out.print("gw"+Config.PANEL_WIDTH/TILE_WIDTH+"gl"+ Config.PANEL_HEIGHT/TILE_HEIGHT);
//		this.gridMap = new GridMap("ddmap.json");
		this.gridMap = new GridMap();
	}
	
	/**
	 * 
	* @Title: tileXToPx 
	* @Description: change tile x to pixel x
	* @param @param x
	* @param @return
	* @return int    
	* @throws
	 */
	public static int tileXToPx(int x){
		return Constant.TILE_WIDTH * x;
	}
	
	/**
	 * 
	* @Title: tileYToPy 
	* @Description: change tile y to pixel y
	* @param @param y
	* @param @return
	* @return int    
	* @throws
	 */
	public static int tileYToPy(int y){
		return Constant.TILE_HEIGHT * y;
	}
	
	/**
	 * 
	* @Title: pxToTileX 
	* @Description: change pixel x to tile x
	* @param @param x
	* @param @return
	* @return int    
	* @throws
	 */
	public static int pxToTileX(float x){
		return  Math.round(x) / Constant.TILE_WIDTH;
	}
	
	/**
	 * 
	* @Title: pxToTileY 
	* @Description: change pixel y to tile y
	* @param @param y
	* @param @return
	* @return int    
	* @throws
	 */
	public static int pxToTileY(float y){
		return Math.round(y) / Constant.TILE_HEIGHT;
	}
	
	/**
	 * 
	* @Title: pxToTile 
	* @Description: change pixel point to tile point
	* @param @param x
	* @param @param y
	* @param @return
	* @return Point    
	* @throws
	 */
	public static Point pxToTile(float x, float y) {
		int mapx = Math.round(x) / Constant.TILE_WIDTH;
		int mapy = Math.round(y) / Constant.TILE_HEIGHT;
		return new Point(mapx, mapy);
	}
}
