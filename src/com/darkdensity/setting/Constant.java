package com.darkdensity.setting;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.server.LoaderHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;

import com.darkdensity.tile.Animation;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * 
* @ClassName: Constant
* @Description: define all the constant and unchange resource of the game
* @author Team A1
* @date Mar 28, 2014 6:33:42 AM
 */
public class Constant {	
	public enum GameState{
		INIT, READY, RUNNING, END
	}
	
	public enum TileManagerState{
		INIT_SPRITE_BUILDING, INIT_BUILDING, READY
	}
	
	public enum GameMode{
		SOLO, COOP, VERSUS
	}
	
	public enum SurvivorClass{
		Survivor1, Survivor2
	}
	
	public enum ZombieClass{
		Zombie1
	}
	
	public enum BarricadeClass {
		LargerBarricade, SmallBarricade
	};
	
	public enum SpriteState {
		STAND, MOVE, ATTACK, DEAD
	};

	//direction for sprite
	public enum Direction {
		SOUTH, WEST, EAST, NORTH, SOUTH_WEST, NORTH_WEST, SOUTH_EAST, NORTH_EAST
	};
	
	public enum BarricadeDirection {
		LEFT, RIGHT
	};
	
	public enum PlayerRole {
		SURVIVOR, ZOMBIE, NEUTRAL
	};
	
	public static final int TILE_WIDTH = 16, TILE_HEIGHT = 16;

	public static final int BARRICADE_MAX_LEVEL = 5;

	public static final int LEFT_MOUSE = 1;
	public static final int MID_MOUSE = 2;
	public static final int RIGHT_MOUSE = 3;

	public static final int FOUR_DIR = 1;
	public static final int EIGHT_DIR = 2;
	
	public static final int BEGIN_POINT = 1;
	public static final int END_POINT = 2;	
	
	public static final int DAY_PERIOD = 60 * 1000;
	public static final int LOADING_TIME = 2 * 1000;
	
	public static final String COMMAND_PACKAGE = "com.darkdensity.command.";
	public static final String TILE_PACKAGE = "com.darkdensity.tile.";
	
	// for thread pool
	public static final int NTHREDS = 10;
	
	public static final double POINT_INTERVAL = 16.0;
	
	//HashMap point set for small and large barricade
	public static HashMap<BarricadeDirection, ArrayList<Point>> SB_BLOCKINGAREA;
	public static HashMap<BarricadeDirection, ArrayList<Point>> LB_BLOCKINGAREA;
	
	public static Hashtable<Point, HashSet<Point>> ASTAR_NODES;

	private static HashMap<String, BufferedImage[]> animationFrames = new HashMap<String, BufferedImage[]>();

	public static BufferedImage[] getFrames(String filename, String highlightFilename, int rows,
			int cols) throws IOException {
		if (!animationFrames.containsKey(filename))
			animationFrames.put(filename, initFrames(filename, highlightFilename, rows, cols));
		return animationFrames.get(filename);
	}

	private static BufferedImage[] initFrames(String filename, String highlightFilename, int rows,
			int cols) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename));
		BufferedImage highlightImage = ImageIO.read(new File(highlightFilename));
		int width = image.getWidth() / cols;
		int height = image.getHeight() / rows;
		int offset = rows * cols;

		BufferedImage[] frames = new BufferedImage[2 * rows * cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[(i * cols) + j] = image.getSubimage(j * width, i
						* height, width, height);
				frames[offset + (i * cols) + j] = highlightImage.getSubimage(j * width, i
						* height, width, height);
			}
		}
		return frames;
	}

	public static void initBarricadeConfig() throws FileNotFoundException,
			IOException {
		LB_BLOCKINGAREA = loadBarricadeConfig("largeBarricade.json");
		SB_BLOCKINGAREA = loadBarricadeConfig("smallBarricade.json");
	}

	private static HashMap<BarricadeDirection, ArrayList<Point>> loadBarricadeConfig(String filename)
			throws FileNotFoundException, IOException {
		HashMap<BarricadeDirection, ArrayList<Point>> blockingArea = new HashMap<BarricadeDirection, ArrayList<Point>>();
		ArrayList<Point> left = new ArrayList<Point>();
		ArrayList<Point> right = new ArrayList<Point>();
		
		//read the json information about the buildings
		JsonObject jsonObject = JsonObject.readFrom(new FileReader(
				Config.JSON_PATH + filename));
		JsonArray blockingAreas = jsonObject.get("blockingAreas").asArray();
		
		for (JsonValue point : blockingAreas) {
			left.add(new Point(point.asObject().get("x").asInt(), point.asObject().get("y").asInt()));
			right.add(new Point(-(point.asObject().get("x").asInt()+1), point.asObject().get("y").asInt()));	
		}
		blockingArea.put(BarricadeDirection.LEFT, left);
		blockingArea.put(BarricadeDirection.RIGHT, right);
			
		return blockingArea;
	}
	
	/**
	 * 
	* @Title: getResourceGenerateTime 
	* @Description: get the resource generate time
	* @param @return
	* @return Long    
	* @throws
	 */
	public static Long getResourceGenerateTime(){
		Long sleep = (long) ((Config.RESOURCE_DELAY + Math.random() * 30) * 1000);
		return sleep;
	}
	
	public static void initAStarNode() throws FileNotFoundException, IOException{	
		ASTAR_NODES = new Hashtable<Point,HashSet<Point>>();
		JsonArray nodes = JsonArray.readFrom(new FileReader(Config.JSON_PATH + "biDirNodes.json"));
		for (JsonValue node : nodes) {
			JsonObject position = node.asObject().get("position").asObject();
			JsonArray neighbours = node.asObject().get("neighbours").asArray();
			HashSet<Point> neighboursList = new HashSet<Point>();
			for (JsonValue point : neighbours) {
				neighboursList.add(new Point(point.asObject().get("x").asInt(), point.asObject().get("y").asInt()));
			}
			ASTAR_NODES.put(new Point(position.get("x").asInt(), position.get("y").asInt()), neighboursList);
		}
	}
	
}
