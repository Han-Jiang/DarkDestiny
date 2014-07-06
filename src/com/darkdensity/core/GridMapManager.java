package com.darkdensity.core;

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
	
	public static int tileXToPx(int x){
		return Constant.TILE_WIDTH * x;
	}
	
	public static int tileYToPy(int y){
		return Constant.TILE_HEIGHT * y;
	}
	
	public static int pxToTileX(float x){
		return  Math.round(x) / Constant.TILE_WIDTH;
	}
	
	public static int pxToTileY(float y){
		return Math.round(y) / Constant.TILE_HEIGHT;
	}
	public static Point pxToTile(float x, float y) {
		int mapx = Math.round(x) / Constant.TILE_WIDTH;
		int mapy = Math.round(y) / Constant.TILE_HEIGHT;
		return new Point(mapx, mapy);
	}
}
