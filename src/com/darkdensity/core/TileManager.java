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

	public TileManager(GameWorld gameWorld) throws IOException {
		this.gameWorld = gameWorld;
		this.tilePanel = new TilePanel(this);
		this.survivors = new HashMap<UUID, Survivor>();
		this.zombies = new HashMap<UUID, Zombie>();
		this.barricades = new HashMap<UUID, Barricade>();
		this.buildings = new HashMap<UUID, Building>();
		this.state = TileManagerState.INIT_SPRITE_BUILDING;
	}

	public void init() throws IOException {
		if (state == TileManagerState.INIT_SPRITE_BUILDING) {
			initSprite();
			initBuilding();
		} else if (state == TileManagerState.INIT_BUILDING) {
			initBuildingResource();
		}
		nextState();
	}

	private void initSprite() throws IOException {
		System.out.println("=====init sprite=====");
		createSprite("Survivor1", 218 * 16, 101 * 16);

		createSprite("Survivor2", 220 * 16, 106 * 16);

		for (int i = 0; i < 10; i++) {
			Point tmpPoint = GridMapManager.gridMap.getRandomPoint();
			createSprite("Zombie1", tmpPoint.x * 16, tmpPoint.y * 16);
		}

	}

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

	public int getSurvivorsNum() {
		return survivors.size();
	}

	public int getZombiesNum() {
		return zombies.size();
	}

	public Sprite getSurvivor(int tileID) {
		return survivors.get(tileID);
	}

	public Sprite getZombie(int tileID) {
		return zombies.get(tileID);
	}

	public Barricade getBarricad(int tileID) {
		return barricades.get(tileID);
	}

	public ArrayList<Tile> getAllTile() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.addAll(buildings.values());
		tiles.addAll(barricades.values());
		tiles.addAll(survivors.values());
		tiles.addAll(zombies.values());
		return tiles;

	}

	public TilePanel getTilePanel() {
		return tilePanel;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public ArrayList<Zombie> getZombies() {
		return new ArrayList<Zombie>(zombies.values());
	}

	public ArrayList<Survivor> getSurivors() {
		return new ArrayList<Survivor>(survivors.values());
	}

	public ArrayList<Barricade> getBarricades() {
		return new ArrayList<Barricade>(barricades.values());
	}

	public ArrayList<Building> getBuildings() {
		return new ArrayList<Building>(buildings.values());
	}

	public Zombie getZombie(UUID uuid) {
		return zombies.get(uuid);
	}

	public Survivor getSurivor(UUID uuid) {
		return survivors.get(uuid);
	}

	public Barricade getBarricade(UUID uuid) {
		return barricades.get(uuid);
	}

	public Building getBuilding(UUID uuid) {
		return buildings.get(uuid);
	}

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

	public FocusManager getFocusManager() {
		return focusManager;
	}

	public void setFocusManager(FocusManager focusManager) {
		this.focusManager = focusManager;
		tilePanel.setFocusManager(focusManager);
	}

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

	public void setCommandFactory(CommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}

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

	private void initBuildingResource() {
		System.out.println("=====init resource=====");
		for (Building building : buildings.values()) {
			generateBuildingResource(building.getUUID());
		}
	}

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

	public void createSprite(String sprite, int x, int y) {
		commandFactory.setPointX(x);
		commandFactory.setPointY(y);
		commandFactory.setTileName(sprite);
		commandFactory.setFocusID(UUID.randomUUID());
		commandFactory.createCommand("CreateCommand");
	}

	public void updateTileProgress(UUID uuid, int n) {
		commandFactory.setFocusID(uuid);
		commandFactory.setProgressUpdate(n);
		commandFactory.createCommand("UpdateProgressCommand");
	}

	public void ScavengeSupplies(UUID uuid, int food, int wood, int iron,
			int survivor, PlayerRole playerRole) {
		commandFactory.setPlayerRole(playerRole);
		commandFactory.setFocusID(uuid);
		commandFactory.setResourse(food, wood, iron, survivor);
		commandFactory.createCommand("ScavengeCommand");
	}

	public void DestroyTile(UUID uuid) {
		commandFactory.setFocusID(uuid);
		commandFactory.createCommand("DestroyTileCommand");
	}

	public void generateZombie() {
		Point tmpPoint = GridMapManager.gridMap.getZombieBasePoint();
		System.out.println("Zombie base pt: "+ tmpPoint.x + " Y: " + tmpPoint.y );
		createSprite("Zombie1", tmpPoint.x * 16, tmpPoint.y * 16);	
	}

	public TileManagerState getState() {
		return state;
	}

	public void setState(TileManagerState state) {
		this.state = state;
	}
	
	private void nextState(){
		state = TileManagerState.values()[state.ordinal() + 1];
	}
}
