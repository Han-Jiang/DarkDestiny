package com.darkdensity.core;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.UUID;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.gui.TilePanel;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.setting.Constant.TileManagerState;
import com.darkdensity.tile.Barricade;
import com.darkdensity.tile.Building;
import com.darkdensity.tile.Sprite;
import com.darkdensity.tile.Survivor;
import com.darkdensity.tile.Tile;
import com.darkdensity.tile.Zombie;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
/**
 * @ClassName: TileManager
 * @Description: TODO(What the class do)
 * @author Team A1 - Ting Yuen Lam, Hei Yin Wong and Ying Jing Feng 
 * @date 24 Mar 2014 23:01:48
 */

/**
 * @ClassName: GameWorld
 * @Description: A class that implements all game logic and hold the game loop.
 *               It will use different "Manager" to complete work
 * @author Team A1
 */

/* ****************************************
 * Class: SpriteManager
 * ****************************************
 * Attributes: 	
 ArrayList<Sprite> zombies;
 ArrayList<Sprite> survivors;
 tilePanel tilePanel; // to manage the tilePanel
 * ****************************************
 * Methods:
 * public SpriteManager(JFrame frame) throws IOException;
 * public Sprite getNearestHuman(Sprite zombie); // get nearest human, if there is no, return null
 * public int getsurvivorsNum();
 * public int getZombiesNum();
 * public Sprite getHuman(int spriteID);
 * public Sprite getZombie(int spriteID);
 * public ArrayList<Sprite> getAllTheSprites();
 * public tilePanel gettilePanel();
 * */
public class TileManager extends Observable {
	private HashMap<UUID, Barricade> barricades;
	private HashMap<UUID, Zombie> zombies;
	private HashMap<UUID, Survivor> survivors;
	private HashMap<UUID, Building> buildings;

	private TilePanel tilePanel;
	private GameWorld gameWorld;
	private FocusManager focusManager;
	private CommandFactory commandFactory;
	private TileManagerState state;

	/**
	 * 
	* <p>Title: </p> Tile manager, manage all th tiles(sprites and buildings) in the maps
	* <p>Description: </p>
	* @param gameWorld
	* @throws IOException
	 */
	public TileManager(GameWorld gameWorld) throws IOException {
		this.gameWorld = gameWorld;
		this.tilePanel = new TilePanel(this);
		this.survivors = new HashMap<UUID, Survivor>();
		this.zombies = new HashMap<UUID, Zombie>();
		this.barricades = new HashMap<UUID, Barricade>();
		this.buildings = new HashMap<UUID, Building>();
		this.state = TileManagerState.INIT_SPRITE_BUILDING;
	}

	/**
	 * 
	* @Title: init 
	* @Description:intial the tile manager
	* @param @throws IOException
	* @return void    
	* @throws
	 */
	public void init() throws IOException {
		if (state == TileManagerState.INIT_SPRITE_BUILDING) {
			initSprite();
			initBuilding();
		} else if (state == TileManagerState.INIT_BUILDING) {
			initBuildingResource();
		}
		nextState();
	}

	/**
	 * 
	* @Title: initSprite 
	* @Description: initial and create the sprite
	* @param @throws IOException
	* @return void    
	* @throws
	 */
	private void initSprite() throws IOException {
		System.out.println("=====init sprite=====");
		createSprite("Survivor1", 218 * 16, 101 * 16);

		createSprite("Survivor2", 220 * 16, 106 * 16);

		for (int i = 0; i < 10; i++) {
			Point tmpPoint = GridMapManager.gridMap.getRandomPoint();
			createSprite("Zombie1", tmpPoint.x * 16, tmpPoint.y * 16);
		}

	}

	/**
	 * 
	* @Title: getNearestHuman 
	* @Description: get the nearest human around a zombie
	* @param @param zombie
	* @param @return
	* @return Sprite a human instance   
	* @throws
	 */
	public Sprite getNearestHuman(Sprite zombie) {
		Sprite nearestSuvivor = null;
		double distance;
		int nearestDistanceSqrt = zombie.getReveal()/2;
		for (Sprite survivor : survivors.values()) {
			if (survivor.isDestroyed())
				continue;
			int distanceX = survivor.getX() - zombie.getX();
			int distanceY = survivor.getY() - zombie.getY();
			int distanceSqrt = distanceX * distanceX + distanceY * distanceY;
			if (Math.sqrt(distanceSqrt) < nearestDistanceSqrt) {
				nearestSuvivor = survivor;
			}
		}
		return nearestSuvivor;
	}

	/**
	 * 
	* @Title: getSurvivorsNum 
	* @Description:get the current survior number
	* @param @return
	* @return int    
	* @throws
	 */
	public int getSurvivorsNum() {
		return survivors.size();
	}

	/**
	 * 
	* @Title: getZombiesNum 
	* @Description: get the zombie number
	* @param @return
	* @return int    
	* @throws
	 */
	public int getZombiesNum() {
		return zombies.size();
	}

	/**
	 * 
	* @Title: getSurvivor 
	* @Description:get a survivor's ID 
	* @param @param tileID
	* @param @return
	* @return Sprite    
	* @throws
	 */
	public Sprite getSurvivor(int tileID) {
		return survivors.get(tileID);
	}
	
	/**
	 * 
	* @Title: getZombie 
	* @Description: get a zombie's ID 
	* @param @param tileID
	* @param @return
	* @return Sprite    
	* @throws
	 */
	public Sprite getZombie(int tileID) {
		return zombies.get(tileID);
	}

	/**
	 * 
	* @Title: getBarricad 
	* @Description:get the barricade's ID
	* @param @param tileID
	* @param @return
	* @return Barricade    
	* @throws
	 */
	public Barricade getBarricad(int tileID) {
		return barricades.get(tileID);
	}

	/**
	 * 
	* @Title: getAllTile 
	* @Description: get all the tile in the map
	* @param @return
	* @return ArrayList<Tile>    
	* @throws
	 */
	public ArrayList<Tile> getAllTile() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.addAll(buildings.values());
		tiles.addAll(barricades.values());
		tiles.addAll(survivors.values());
		tiles.addAll(zombies.values());
		return tiles;

	}

	/**
	 * 
	* @Title: getTilePanel 
	* @Description: get the tile panel
	* @param @return
	* @return TilePanel    
	* @throws
	 */
	public TilePanel getTilePanel() {
		return tilePanel;
	}

	/**
	 * 
	* @Title: getGameWorld 
	* @Description: get the game world instance
	* @param @return
	* @return GameWorld    
	* @throws
	 */
	public GameWorld getGameWorld() {
		return gameWorld;
	}

	/**
	 * 
	* @Title: getZombies 
	* @Description: get all the zombie sprite
	* @param @return
	* @return ArrayList<Zombie>    
	* @throws
	 */
	public ArrayList<Zombie> getZombies() {
		return new ArrayList<Zombie>(zombies.values());
	}

	/**
	 * 
	* @Title: getSurivors 
	* @Description: get all the surivors's instance
	* @param @return
	* @return ArrayList<Survivor>    
	* @throws
	 */
	public ArrayList<Survivor> getSurivors() {
		return new ArrayList<Survivor>(survivors.values());
	}

	/**
	 * 
	* @Title: getBarricades 
	* @Description:get all the barricade's instance
	* @param @return
	* @return ArrayList<Barricade>    
	* @throws
	 */
	public ArrayList<Barricade> getBarricades() {
		return new ArrayList<Barricade>(barricades.values());
	}

	/**
	 * 
	* @Title: getBuildings 
	* @Description: get all the building instance
	* @param @return
	* @return ArrayList<Building>    
	* @throws
	 */
	public ArrayList<Building> getBuildings() {
		return new ArrayList<Building>(buildings.values());
	}

	/**
	 * 
	* @Title: getZombie 
	* @Description: get a zombie by uuid
	* @param @param uuid
	* @param @return
	* @return Zombie    
	* @throws
	 */
	public Zombie getZombie(UUID uuid) {
		return zombies.get(uuid);
	}

	/**
	 * 
	* @Title: getSurivor 
	* @Description: get a surivor by uuid
	* @param @param uuid
	* @param @return
	* @return Survivor    
	* @throws
	 */
	public Survivor getSurivor(UUID uuid) {
		return survivors.get(uuid);
	}

	/**
	 * 
	* @Title: getBarricade 
	* @Description: get a barricade by uuid
	* @param @param uuid
	* @param @return
	* @return Barricade    
	* @throws
	 */
	public Barricade getBarricade(UUID uuid) {
		return barricades.get(uuid);
	}

	/**
	 * 
	* @Title: getBuilding 
	* @Description: get building by uuid
	* @param @param uuid
	* @param @return
	* @return Building    
	* @throws
	 */
	public Building getBuilding(UUID uuid) {
		return buildings.get(uuid);
	}

	/**
	 * 
	* @Title: getTile 
	* @Description: get a tile by uuid
	* @param @param uuid
	* @param @return
	* @return Tile    
	* @throws
	 */
	public Tile getTile(UUID uuid) {
		if (survivors.containsKey(uuid)) {
			return survivors.get(uuid);
		} else if (zombies.containsKey(uuid)) {
			return zombies.get(uuid);
		} else if (barricades.containsKey(uuid)) {
			return barricades.get(uuid);
		} else if (buildings.containsKey(uuid)) {
			return buildings.get(uuid);
		} else {
			return null;
		}
	}

	/**
	 * 
	* @Title: getFocusManager 
	* @Description: get the focus manager
	* @param @return
	* @return FocusManager    
	* @throws
	 */
	public FocusManager getFocusManager() {
		return focusManager;
	}

	/**
	 * 
	* @Title: setFocusManager 
	* @Description: set the focus manager
	* @param @param focusManager
	* @return void    
	* @throws
	 */
	public void setFocusManager(FocusManager focusManager) {
		this.focusManager = focusManager;
		tilePanel.setFocusManager(focusManager);
	}

	/**
	 * 
	* @Title: addTile 
	* @Description: add a tile to corresponding tile list
	* @param @param t
	* @return void    
	* @throws
	 */
	public void addTile(Tile t) {
		if (t instanceof Survivor) {
			survivors.put(t.getUUID(), (Survivor) t);
		} else if (t instanceof Zombie) {
			zombies.put(t.getUUID(), (Zombie) t);
		} else if (t instanceof Barricade) {
			setChanged();
			notifyObservers(t);
			barricades.put(t.getUUID(), (Barricade) t);
		} else if (t instanceof Building) {
			buildings.put(t.getUUID(), (Building) t);
			tilePanel.initTile(t);
			return;
		}
		GridMapManager.gridMap.add(t);
		tilePanel.initTile(t);
	}

	/**
	 * 
	* @Title: removeTile 
	* @Description: remove tile from the game by using an extra thread 
	* @param @param t
	* @return void    
	* @throws
	 */
	public void removeTile(final Tile t) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					t.processDestory();
					if (t instanceof Survivor) {
						if(survivors.size() <= 1){
							GameWorld.setWinningTeam(PlayerRole.ZOMBIE);
						}
					}
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					if(Config.DEBUGMODE){e.printStackTrace();}
				}
				if (t instanceof Survivor) {
					survivors.remove(t.getUUID());
				} else if (t instanceof Zombie) {
					zombies.remove(t.getUUID());
				} else if (t instanceof Barricade) {
					setChanged();
					notifyObservers(t);
					barricades.remove(t.getUUID());
				}
				GridMapManager.gridMap.remove(t);
				tilePanel.removeTile(t);
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * 
	* @Title: getNearestSurroundingHuman 
	* @Description: get the nearest human in a number of surrounding human around an zombie.
	* @param @param zombie
	* @param @param surroundingSprite
	* @param @return
	* @return Sprite    
	* @throws
	 */
	public static Sprite getNearestSurroundingHuman(Sprite zombie,
			ArrayList<Tile> surroundingSprite) {
		if (surroundingSprite.size() == 0)
			return null;
		int nearestDistanceSqrt = 100000;
		Sprite nearestSprite = null;
		for (Tile tile : surroundingSprite) {
			if (tile instanceof Survivor) {
				if (!((Sprite) tile).isDestroyed()) {
					int distanceX = tile.getX() - zombie.getX();
					int distanceY = tile.getY() - zombie.getY();
					int distanceSqrt = (int) (Math.sqrt(distanceX) + Math
							.sqrt(distanceX));
					if (distanceSqrt < nearestDistanceSqrt) {
						nearestDistanceSqrt = distanceSqrt;
						nearestSprite = (Sprite) tile;
					}
				}
			}
		}

		return nearestSprite;
	}

	/**
	 * 
	* @Title: setCommandFactory 
	* @Description: set the command factory
	* @param @param commandFactory
	* @return void    
	* @throws
	 */
	public void setCommandFactory(CommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}

	/**
	 * 
	* @Title: isLose 
	* @Description:see whether the player has lose the game 
	* @param @return
	* @return boolean    
	* @throws
	 */
	public boolean isLose() {
		try {
			boolean isLose = true;
			for (Sprite s : survivors.values()) {
				if (s.getClass() == Zombie.class) {
					continue;
				}
				if (!s.isDestroyed()) {
					isLose = false;
				}
			}
			return isLose;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	* @Title: initBuilding 
	* @Description: load all the buildings from a json file 
	* @param @throws FileNotFoundException
	* @param @throws IOException
	* @return void    
	* @throws
	 */
	private void initBuilding() throws FileNotFoundException, IOException {
		System.out.println("=====init building=====");
		JsonArray buildings = JsonArray.readFrom(new FileReader(
				Config.JSON_PATH + "buildings.json"));
		for (JsonValue building : buildings) {
			commandFactory.setPointX(building.asObject().get("startX").asInt());
			commandFactory.setPointY(building.asObject().get("startY").asInt());
			commandFactory.setBuildingName("building_"
					+ building.asObject().get("id").asInt());
			commandFactory.setTileName("Building");
			commandFactory.setFocusID(UUID.randomUUID());
			commandFactory.createCommand("BuildingCommand");
		}
	}

	/**
	 * 
	* @Title: initBuildingResource 
	* @Description: generate resource in the building when the building was first loaded
	* @param 
	* @return void    
	* @throws
	 */
	private void initBuildingResource() {
		System.out.println("=====init resource=====");
		for (Building building : buildings.values()) {
			generateBuildingResource(building.getUUID());
		}
	}

	/**
	 * 
	* @Title: generateBuildingResource 
	* @Description: re generate the building resource
	* @param @param uuid
	* @return void    
	* @throws
	 */
	public void generateBuildingResource(UUID uuid) {
		System.out.println("=====gen resource=====");
		commandFactory.setFocusID(uuid);
		int food = (int) (30 + Math.random() * 20);
		int wood = (int) (30 + Math.random() * 20);
		int iron = (int) (20 + Math.random() * 20);
		int survivor = (int) (Math.random() * 3);
		commandFactory.setResourse(food, wood, iron, survivor);
		commandFactory.createCommand("GenernateResourceCommand");
	}

	/**
	 * 
	* @Title: createSprite 
	* @Description: create a sprite at a positin
	* @param @param sprite
	* @param @param x
	* @param @param y
	* @return void    
	* @throws
	 */
	public void createSprite(String sprite, int x, int y) {
		commandFactory.setPointX(x);
		commandFactory.setPointY(y);
		commandFactory.setTileName(sprite);
		commandFactory.setFocusID(UUID.randomUUID());
		commandFactory.createCommand("CreateCommand");
	}

	/**
	 * 
	* @Title: updateTileProgress 
	* @Description: update tile progress 
	* @param @param uuid
	* @param @param n
	* @return void    
	* @throws
	 */
	public void updateTileProgress(UUID uuid, int n) {
		commandFactory.setFocusID(uuid);
		commandFactory.setProgressUpdate(n);
		commandFactory.createCommand("UpdateProgressCommand");
	}

	/**
	 * 
	* @Title: ScavengeSupplies 
	* @Description: scanvenge food and other resource in a building by uuid 
	* @param @param uuid
	* @param @param food
	* @param @param wood
	* @param @param iron
	* @param @param survivor
	* @param @param playerRole
	* @return void    
	* @throws
	 */
	public void ScavengeSupplies(UUID uuid, int food, int wood, int iron,
			int survivor, PlayerRole playerRole) {
		commandFactory.setPlayerRole(playerRole);
		commandFactory.setFocusID(uuid);
		commandFactory.setResourse(food, wood, iron, survivor);
		commandFactory.createCommand("ScavengeCommand");
	}

	/**
	 * 
	* @Title: DestroyTile 
	* @Description: destroy a tile by uuid
	* @param @param uuid
	* @return void    
	* @throws
	 */
	public void DestroyTile(UUID uuid) {
		commandFactory.setFocusID(uuid);
		commandFactory.createCommand("DestroyTileCommand");
	}

	/**
	 * 
	* @Title: generateZombie 
	* @Description: generate zombies
	* @param 
	* @return void    
	* @throws
	 */
	public void generateZombie() {
		Point tmpPoint = GridMapManager.gridMap.getZombieBasePoint();
		System.out.println("Zombie base pt: "+ tmpPoint.x + " Y: " + tmpPoint.y );
		createSprite("Zombie1", tmpPoint.x * 16, tmpPoint.y * 16);	
	}
	
	/**
	 * 
	* @Title: getState 
	* @Description: get the state of the tile management
	* @param @return
	* @return TileManagerState    
	* @throws
	 */
	public TileManagerState getState() {
		return state;
	}

	/**
	 * 
	* @Title: setState 
	* @Description:set state of tile manager
	* @param @param state
	* @return void    
	* @throws
	 */
	public void setState(TileManagerState state) {
		this.state = state;
	}
	
	/**
	 * 
	* @Title: nextState 
	* @Description: change the state into next stage
	* @param 
	* @return void    
	* @throws
	 */
	private void nextState(){
		state = TileManagerState.values()[state.ordinal() + 1];
	}
}
