package com.darkdensity.maprender;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.darkdensity.core.GridMapManager;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.tile.Barricade;
import com.darkdensity.tile.Sprite;
import com.darkdensity.tile.Tile;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * 
* @ClassName: GridMap
* @Description: GridMap is the matrix of the grid, 
* it contains the information of the map
* @author Team A1
* @date 19 Mar 2014 15:52:07
 */

public class GridMap {
	private static HashMap<Point, Grid> map;

	private static boolean completed = false;

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> Construct a new grid map from json file 
	* @throws FileNotFoundException
	* @throws IOException
	 */
	public GridMap() throws FileNotFoundException, IOException {
		map = new HashMap<Point, Grid>();
		readMapJSON("road.json");
		completed = true;
	}

	/**
	 * 
	* @Title: getGrid 
	* @Description: get the grid 
	* @param @param tileX
	* @param @param tileY
	* @param @return
	* @return Grid    
	* @throws
	 */
	public static Grid getGrid(int tileX, int tileY) {
		Point p = new Point(tileX, tileY);
		if (map.containsKey(p))
			return map.get(p);
		return null;
	}

	/**
	 * 
	* @Title: getGrids 
	* @Description: get grids form tile x to tile y
	* @param @param tileX  start x of tile
	* @param @param tileY  end x of tile
	* @param @return
	* @return ArrayList<Tile>    
	* @throws
	 */
	public ArrayList<Tile> getGrids(int tileX, int tileY) {
		ArrayList<Tile> surroundingList = new ArrayList<Tile>();
		for (int x = tileX - 50; x <= tileX + 50; x++) {
			for (int y = tileY - 50; y <= tileY + 50; y++) {
				Grid suroundGrid = getGrid(x, y);
				if (suroundGrid != null) {
					surroundingList.addAll(suroundGrid);
				}
			}
		}
		return surroundingList;

	}

	/**
	 * 
	* @Title: getRandomPoint 
	* @Description: Get random point on the map
	* @param @return
	* @return Point    
	* @throws
	 */
	public Point getRandomPoint() {
		Random random = new Random();
		Object[] pArr = (Object[]) map.keySet().toArray();
		int randInt = random.nextInt(pArr.length);
		return (Point) pArr[randInt];

	}
	/**
	 * 
	* @Title: getRandomGrid 
	* @Description:Get random grid at the grid map
	* @param @return
	* @return Grid    
	* @throws
	 */
	public Grid getRandomGrid() {
		Random random = new Random();
		Grid[] gridArrGrids = (Grid[]) map.values().toArray();
		int randInt = random.nextInt(gridArrGrids.length);
		return gridArrGrids[randInt];

	}
	/**
	 * 
	* @Title: add 
	* @Description: add a tile to the grid map
	* @param @param tile
	* @return void    
	* @throws
	 */
	static public synchronized void add(Tile tile) {
		if (tile instanceof Barricade) {
			ArrayList<Point> blockingArea = ((Barricade) tile)
					.getBlockingArea();
			for (Point point : blockingArea) {
				Point tilePoint = GridMapManager.pxToTile(point.x, point.y);
				Grid grid = map.get(tilePoint);
				grid.add(tile);
				grid.block();
			}
		} else {
			Point tilePoint = GridMapManager.pxToTile(tile.getX(), tile.getY());
			map.get(tilePoint).add(tile);
		}
	}
	
	/**
	 * 
	* @Title: remove 
	* @Description: remove the tile form the grid map
	* @param @param tile
	* @return void    
	* @throws
	 */
	static public synchronized void remove(Tile tile) {
		if (tile instanceof Barricade) {
			ArrayList<Point> blockingArea = ((Barricade) tile)
					.getBlockingArea();
			for (Point point : blockingArea) {
				Point tilePoint = GridMapManager.pxToTile(point.x, point.y);
				Grid grid = map.get(tilePoint);
				grid.remove(tile);
				grid.release();
			}
		} else {
			Point tilePoint = GridMapManager.pxToTile(tile.getX(), tile.getY());
			map.get(tilePoint).remove(tile);
		}
	}


	/**
	 * 
	* @Title: updateGrid 
	* @Description: update the sprite form previous position point 
	* to current position point 
	* @param @param s
	* @param @param prevPosPoint
	* @return void    
	* @throws
	 */
	public void updateGrid(Sprite s, Point prevPosPoint) {
		Grid prevGrid = getGrid(GridMapManager.pxToTileX(prevPosPoint.x),
				GridMapManager.pxToTileY(prevPosPoint.y));
		Grid curGrid = getGrid(GridMapManager.pxToTileX(s.getX()),
				GridMapManager.pxToTileY(s.getY()));
		if (prevGrid == curGrid)
			return;
		if (prevGrid != null && curGrid != null) {
			prevGrid.remove(s);
			curGrid.add(s);
		}
	}
	
	/**
	 * 
	* @Title: readMapJSON 
	* @Description: read the json file and initialize the grid map
	* @param @param mapPath
	* @param @throws FileNotFoundException
	* @param @throws IOException
	* @return void    
	* @throws
	 */
	public void readMapJSON(String mapPath) throws FileNotFoundException, IOException {
		JsonObject jsonObject = JsonObject.readFrom(new FileReader(
				Config.JSON_PATH + mapPath));
		JsonArray roads = jsonObject.get("road").asArray();
	
		for (JsonValue point : roads) {
			map.put(new Point(point.asObject().get("x").asInt(),
					point.asObject().get("y").asInt()), new Grid());
		}
	}

	/**
	 * 
	* @Title: isCompleted 
	* @Description: tell whether the grid map has finished loading
	* @param @return
	* @return boolean    
	* @throws
	 */
	public static boolean isCompleted() {
		return completed;
	}

	/**
	 * 
	* @Title: getMap 
	* @Description: get the grip map
	* @param @return
	* @return HashMap<Point,Grid>    
	* @throws
	 */
	public static HashMap<Point, Grid> getMap() {
		return map;
	}
	
	/**
	 * 
	* @Title: getClosestPoint 
	* @Description: get the closest point
	* @param @param x
	* @param @param y
	* @param @return
	* @return Point    
	* @throws
	 */
	public Point getClosestPoint(int x, int y){
		Point point = GridMapManager.pxToTile(x, y);
		for(Point p = point; ; ++p.x, ++p.y){
			if(map.containsKey(point)){
				return new Point(point.x * Constant.TILE_WIDTH, point.y * Constant.TILE_HEIGHT);
			}
		}
	}
	/**
	 * 
	* @Title: blockInBetween 
	* @Description: see whether it is walkable between two ponit
	* @param @param bPoint  beigin point
	* @param @param ePoint	end point
	* @param @param size	
	* @param @return
	* @return boolean    
	* @throws
	 */
	public boolean blockInBetween(Point bPoint, Point ePoint, int size) {

		if (size == 1 || size == 0)
			return false;
		if (size == 2) {
			int middlePointX = (bPoint.x / 16 + ePoint.x / 16) / 2;
			int middlePointY = (bPoint.y / 16 + ePoint.y / 16) / 2;
			
			return (getGrid(middlePointX, middlePointY) == null || getGrid(middlePointX, middlePointY).isBlocked());

		}
		double Vx = (double)(ePoint.x - bPoint.x) / size;
		double Vy = (double)(ePoint.y - bPoint.y) / size;

		for (int i = 0; i < size - 1; i++) {
			int tmpgridX = (int) (bPoint.x + Vx*i)/16;
			int tmpgridY = (int) (bPoint.y + Vy*i)/16;
			if (getGrid(tmpgridX, tmpgridY) == null
					|| getGrid(tmpgridX, tmpgridY).isBlocked())
				return true;
		}
		return false;
	}
	
	public Point getZombieBasePoint() {
		Object[] pArr = (Object[]) map.keySet().toArray();
		return (Point) pArr[pArr.length-1];

	}
	
}
