package com.darkdensity.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

import com.darkdensity.core.ControlManager.LeftButtonListener;
import com.darkdensity.core.ControlManager.LeftDraggedListener;
import com.darkdensity.core.ControlManager.LeftDraggingListener;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.gui.BuildPanel;
import com.darkdensity.gui.FuncPanel;
import com.darkdensity.gui.GameLoadingPanel;
import com.darkdensity.gui.InfoPanel;
import com.darkdensity.gui.MapForegroundPanel;
import com.darkdensity.gui.MapPanel;
import com.darkdensity.gui.MiniMapPanel;
import com.darkdensity.gui.ShadowPanel;
import com.darkdensity.gui.TilePanel;
import com.darkdensity.gui.TopMenuPanel;
import com.darkdensity.maprender.GridMap;
import com.darkdensity.net.chat.ChatPanel;
import com.darkdensity.path.SubPathManager;
import com.darkdensity.player.Resource;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.GameMode;
import com.darkdensity.setting.Constant.GameState;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.setting.Constant.TileManagerState;
import com.darkdensity.sound.MusicPlayer;
import com.darkdensity.tile.Tile;
import com.darkdensity.util.ImageLoader;

/**
 * @ClassName: GameWorld
 * @Description: TODO(What the class do)
 * @author Team A1
 * @date 24 Mar 2014 23:01:48
 */

/**
 * @ClassName: GameWorld
 * @Description: A class that implements all game logic and hold the game loop.
 *               It will use different "Manager" to complete work
 * @author Team A1
 */

public class GameWorld extends JLayeredPane {
	// private static boolean isFinishLoading = false;
	private static GameMode gameMode;
	private ControlManager controlManager;
	private FocusManager focusManager;
	private GridMapManager gridMapManager;
	private NetworkManager networkManager;
	private CommandFactory commandFactory;
	private TileManager tileManager;
	private static SubPathManager subPathManager;
	// parent frame
	private JFrame frame;

	// game render beginning point
	public static int iRenderX = 0;
	public static int iRenderY = 0;
	// mouse position at the panel
	public static int actualX = 0;
	public static int actualY = 0;

	// add panels
	private MapPanel mapPanel;
	private MiniMapPanel miniMapPanel;
	private TopMenuPanel topMenuPanel;
	private FuncPanel funcPanel;
	private InfoPanel infoPanel;
	private BuildPanel buildPanel;
	private TilePanel tilePanel;
	private MapForegroundPanel mapForegroundPanel;
	private static ShadowPanel shadowPanel;
	private static SoundManager soundManager;

	private CommandPool commandPool;
	private JLabel fpsLbl;

	private GameState gameState = GameState.INIT;
	// buffer strategy for the game world
	private BufferStrategy strategy;
	private ChatPanel chatPanel;
	private java.util.Timer timer;
	private static PlayerRole winningTeam;

	private static HashMap<PlayerRole, Team> teams;

	// time counter for the game
	private long currentTime;
	private long elapsedTime;
	private static long timeLeft;
	private long zombieGenerationTimer;
	private long consumeFoodTimer;

	// sount the FPS
	private long nextSecond = System.currentTimeMillis() + 1000;
	private int FPS = 0;
	private int FPSCounter = 0;

	private Thread gameWorldThread;

	private Thread loadingThread;
	private GameLoadingPanel gameLoadingPanel;

	/**
	 * 
	 * <p>
	 * Title:
	 * </p>
	 * Game World
	 * <p>
	 * Description:
	 * </p>
	 * this is where the game logic is handle
	 * 
	 * @param frame
	 *            : parent fame
	 * @param gameMode
	 *            : solo game mode or cooperate game mode
	 * @throws Throwable
	 */
	public GameWorld(JFrame frame, final GameMode gameMode) throws Throwable {
		this.frame = frame;
		setLayout(null);
		frame.requestFocusInWindow();
		setSize(frame.getWidth(), frame.getHeight());
		this.gameMode = gameMode;

		init();

		// a thread for the main game logic
		Runnable gameWorldRunnable = new Runnable() {
			@Override
			public void run() {
				// satrt the game loop only when the map is ready
				while (GameWorld.this.frame.getBufferStrategy() == null
						|| !gridMapManager.gridMap.isCompleted()
						|| tileManager.getState() != TileManagerState.READY) {
					try {
						// initial the tile manger
						if (gameMode == GameMode.SOLO) {
							tileManager.init();
							commandPool.execute();
						}
					} catch (Throwable e) {
						if (Config.DEBUGMODE) {
							e.printStackTrace();
						}
					}
				}
				// game loading panel
				long loadingCounter = 0;
				do {
					gameLoadingPanel.setProgress(loadingCounter++);
					gameLoadingPanel.repaint();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						if (Config.DEBUGMODE) {
							e.printStackTrace();
						}
					}
				} while (loadingCounter < 100);

				strategy = GameWorld.this.frame.getBufferStrategy();
				gameLoadingPanel.setVisible(false);
				remove(gameLoadingPanel);
				iRenderX = 3200;
				iRenderY = 1200;
				soundManager.getSoundPlayer("res/music/horror-ambient.wav")
						.play();
				gameState = GameState.RUNNING;
				GameWorld.this.gameLoop();
			}
		};

		// run the game world thread for the main game logic
		gameWorldThread = new Thread(gameWorldRunnable);
		if (gameMode == GameMode.SOLO) {
			startGame();
		}
	}

	/**
	 * 
	 * @Title: init
	 * @Description: intial the game , load resources and add panels
	 * @param @throws IOException
	 * @param @throws InterruptedException
	 * @return void
	 * @throws
	 */
	public void init() throws IOException, InterruptedException {
		this.winningTeam = null;
		this.timeLeft = Config.GAME_WINNING_TIME;
		if (GameCore.mu != null) {
			GameCore.mu.setMusicStop();
		}
		soundManager = new SoundManager();
		Constant.initBarricadeConfig();
		Constant.initAStarNode();
		zombieGenerationTimer = 0;

		mapPanel = new MapPanel(frame);

		controlManager = new ControlManager(this, frame);
		gridMapManager = new GridMapManager(mapPanel.getMapWidthPx(),
				mapPanel.getMapHeightPx(), this);

		focusManager = new FocusManager();
		// focusManager.setCommandPool(commandPool);
		focusManager.setGridMapManager(gridMapManager);
		tileManager = new TileManager(this);
		tileManager.setFocusManager(focusManager);
		// get subpath manager
		subPathManager = new SubPathManager();
		tileManager.addObserver(subPathManager);

		commandPool = new CommandPool(this);

		commandFactory = new CommandFactory();
		// set commond factory when when is solo mode
		if (gameMode == GameMode.SOLO) {
			commandFactory.setGameWorld(this);
			commandFactory.setGridMap(gridMapManager.gridMap);
			commandFactory.setCommandPool(commandPool);
		}

		tileManager.setCommandFactory(commandFactory);
		focusManager.setCommandFactory(commandFactory);

		// set up the team
		teams = new HashMap<Constant.PlayerRole, Team>();
		Team survivorTeam = new Team(Config.SURVIVOR_START_FOOD,
				Config.SURVIVOR_START_WOOD, Config.SURVIVOR_START_WOOD,
				PlayerRole.SURVIVOR);
		Team zombieTeam = new Team(Config.ZOMBIE_START_FOOD,
				Config.ZOMBIE_START_WOOD, Config.ZOMBIE_START_IRON,
				PlayerRole.ZOMBIE);
		survivorTeam.setTileManager(tileManager);
		zombieTeam.setTileManager(tileManager);
		teams.put(PlayerRole.SURVIVOR, survivorTeam);
		teams.put(PlayerRole.ZOMBIE, zombieTeam);

		miniMapPanel = new MiniMapPanel(frame, tileManager);
		topMenuPanel = new TopMenuPanel(frame);
		// initialize the fucntion panel and info panel
		funcPanel = new FuncPanel(frame);
		funcPanel.setGameWorld(this);
		funcPanel.setFocusManager(focusManager);
		infoPanel = new InfoPanel(frame);

		buildPanel = new BuildPanel(this);
		buildPanel.setGridMapManager(gridMapManager);
		buildPanel.setVisible(false);

		topMenuPanel.setLocation(frame.getWidth() - topMenuPanel.getWidth(), 0);
		topMenuPanel.setVisible(true);
		miniMapPanel.setLocation(frame.getWidth() - miniMapPanel.getWidth(),
				frame.getHeight() - miniMapPanel.getHeight());
		infoPanel.setLocation(
				(frame.getWidth() - infoPanel.getWidth()) / 2 + 50,
				frame.getHeight() - infoPanel.getHeight());
		infoPanel.setVisible(false);

		funcPanel.setLocation(0,
				(frame.getHeight() - funcPanel.getHeight()) / 2);
		if (Config.PLAYER_ROLE == PlayerRole.ZOMBIE) {
			funcPanel.setVisible(false);
		}

		focusManager.setInfoPanel(infoPanel);
		focusManager.setFuncPanel(funcPanel);

		tilePanel = tileManager.getTilePanel();
		tilePanel.setVisible(true);
		tilePanel.setLocation(0, 0);

		mapForegroundPanel = new MapForegroundPanel(frame);
		mapForegroundPanel.setLocation(0, 0);

		if (!Config.IS_FULL_REVEAL) {
			shadowPanel = new ShadowPanel(frame);
			shadowPanel.setLocation(0, 0);
			shadowPanel.setTileManager(tileManager);
			shadowPanel.setVisible(true);
		}

		gameLoadingPanel = new GameLoadingPanel(frame);
		gameLoadingPanel.setVisible(true);

		fpsLbl = new JLabel();
		fpsLbl.setSize(50, 50);
		fpsLbl.setLocation(getWidth() - 50, 30);
		add(fpsLbl, JLayeredPane.DRAG_LAYER);
		fpsLbl.setVisible(true);
		fpsLbl.setForeground(Color.yellow);

		// if (this.gameMode == GameMode.VERSUS || this.gameMode ==
		// GameMode.COOP) {
		chatPanel = new ChatPanel();
		chatPanel.setLocation(0, getHeight() - chatPanel.getHeight());
		chatPanel.setVisible(this.gameMode != GameMode.SOLO);
		chatPanel.startReceiveMessage();
		add(chatPanel, JLayeredPane.DRAG_LAYER);

		if (gameMode == GameMode.SOLO) {
			chatPanel.setCommandPool(this);
		}

		add(mapPanel, JLayeredPane.DEFAULT_LAYER);// 0
		add(tileManager.getTilePanel(), JLayeredPane.PALETTE_LAYER);// 100

		if (!Config.IS_FULL_REVEAL) {
			add(shadowPanel, JLayeredPane.MODAL_LAYER);
		}
		add(mapForegroundPanel, JLayeredPane.MODAL_LAYER);// 200

		add(buildPanel, JLayeredPane.POPUP_LAYER);// 300

		add(gameLoadingPanel, JLayeredPane.DRAG_LAYER);// 400
		add(funcPanel, JLayeredPane.DRAG_LAYER);
		add(miniMapPanel, JLayeredPane.DRAG_LAYER);
		add(topMenuPanel, JLayeredPane.DRAG_LAYER);
		add(infoPanel, JLayeredPane.DRAG_LAYER);

		controlManager.addLeftDraggingListener(new LeftDraggingListener() {

			@Override
			public void dragging(int sx, int sy, int ex, int ey) {
				sx += iRenderX;
				ex += iRenderX;
				sy += iRenderY;
				ey += iRenderY;
				focusManager.highlight(sx, sy, ex, ey);
			}
		});

		controlManager.addLeftDraggedListener(new LeftDraggedListener() {
			@Override
			public void dragged(int sx, int sy, int ex, int ey) {
				sx += iRenderX;
				ex += iRenderX;
				sy += iRenderY;
				ey += iRenderY;
				focusManager.focus(sx, sy, ex, ey);
			}
		});

		controlManager.addLeftButtonListener(new LeftButtonListener() {
			public void press(int x, int y) {
			}
		});

		// a timer for the detection of whether the player has win the game
		timer = new java.util.Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Image bimage = ImageLoader.loadImage(Config.IMG_WINNING_PATH);
				JLabel jLabel = new JLabel(new ImageIcon(bimage));
				jLabel.setLocation(
						GameWorld.this.getWidth() / 2 - bimage.getWidth(null)
								/ 2,
						GameWorld.this.getHeight() / 2 - bimage.getHeight(null)
								/ 2);
				jLabel.setSize(bimage.getWidth(null), bimage.getHeight(null));

				GameWorld.this.add(jLabel);

				MusicPlayer mu = new MusicPlayer(Config.MUSIC_WINNING_PATH);
				mu.start();
			}
		}, Config.GAME_WINNING_TIME * 1000);

		gameState = GameState.READY;
	}

	public void gameLoop() {
		currentTime = System.currentTimeMillis();
		elapsedTime = 0;
		Timer sTimer = new Timer(Config.SKIP_TICKS, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameLoopCore() != GameState.RUNNING) {
					((Timer) e.getSource()).stop();
					if (gameState == GameState.END) {
						// exitGameWorld();
					}
					try {
						gameWorldThread.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				//Check if the game is should end
				if (Config.IS_SERVER && winningTeam != null) {
					commandFactory.setPlayerRole(winningTeam);
					commandFactory.createCommand("GameEndCommand");
				}

			}
		});

		sTimer.start();
	}

	public GameState gameLoopCore() {
		//implement the core part in game loop
		elapsedTime = System.currentTimeMillis() - currentTime;
		currentTime = System.currentTimeMillis();
		timeLeft -= elapsedTime;
		zombieGenerationTimer += elapsedTime;
		// update
		//Generate zombies every X second, this can be set in config.java
		if (zombieGenerationTimer > Config.ZOMBIE_GENERATION_TIME) {
			tileManager.generateZombie();
			zombieGenerationTimer -= Config.ZOMBIE_GENERATION_TIME;
		}

		//Implement the food consumption logic in survivor team
		if (Config.IS_NETWORK_MODE || Config.IS_SERVER) {
			consumeFoodTimer += elapsedTime;
			if (consumeFoodTimer > Config.CONSUME_FOOD_TIMER) {
				commandFactory.createCommand("ConsumeFoodCommand");
				consumeFoodTimer -= Config.CONSUME_FOOD_TIMER;

				//Kill survivor if they don't have enough food
				if (getTeam(PlayerRole.SURVIVOR).getFood() <= 0) {
					commandFactory.setFocusID(tileManager.getSurivors().get(0)
							.getUUID());
					commandFactory.createCommand("DestroyTileCommand");

				}
			}
		}

		try {
			update(elapsedTime);
		} catch (Throwable e) {
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
		cameraScolling();
		// get the graphic and redraw it
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		redrawScreen(g);
		g.dispose();// must dispose and then show

		if (!strategy.contentsLost()) {
			strategy.show();
		}

		return gameState;

	}

	//Trigger the update method in each tiles, to provide visiable change to player
	public void update(long elapsedTime) throws Throwable {
		commandPool.execute();
		ArrayList<Tile> tiles = tileManager.getAllTile();
		for (Tile tile : tiles) {
			tile.update(elapsedTime);
		}
		topMenuPanel.update(elapsedTime);
	}

	public void cameraScolling() {
		//To handle the camera scrolling process when user place their cursor onto the frame

		int SCREEN_MOVE_SPEED = Config.SCREEN_MOVE_SPEED;

		// getMouseX() mouse event get x is a ,
		// relative value to the JFrame

		actualX = controlManager.getMouseX() + getX();
		actualY = controlManager.getMouseY() + getY();
		// so the actualX will be the

		int frameRight = getX() + getWidth() - Config.SENSITIVE_AREA;

		int frameDown = getY() + getHeight() - Config.SENSITIVE_AREA;

		if (actualX == 0 && iRenderX > 0) {
			iRenderX = iRenderX - SCREEN_MOVE_SPEED;
		}
		if (actualX > frameRight
				&& iRenderX < (mapPanel.getMapWidthPx() - Config.PANEL_WIDTH - Config.SENSITIVE_AREA)) {
			iRenderX = iRenderX + SCREEN_MOVE_SPEED;
		}
		if (actualY == 0 && iRenderY > 0) {
			iRenderY = iRenderY - SCREEN_MOVE_SPEED;
		}
		if (actualY > frameDown
				&& iRenderY < (mapPanel.getMapHeightPx() - Config.PANEL_HEIGHT - Config.SENSITIVE_AREA)) {
			iRenderY = iRenderY + SCREEN_MOVE_SPEED;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime > nextSecond) {
			nextSecond += 1000;
			FPS = FPSCounter;
			FPSCounter = 0;
		}
		FPSCounter++;
		fpsLbl.setText(FPS + "fps");

		// mapForegroundPanel.setPainXY(iRenderX, iRenderY);

	}

	/**
	 * 
	 * @Title: redrawScreen
	 * @Description: TODO(What the method do)
	 * @param @param g
	 * @return void
	 * @throws
	 */
	private void redrawScreen(Graphics2D g) {
		// this.paintChildren(g);
		mapPanel.paintComponent(g);
		tilePanel.paintComponent(g);
		mapForegroundPanel.paintComponent(g);

		if (buildPanel.isVisible()) {
			buildPanel.paintComponent(g);
		}

		controlManager.dragActionDrawer(g);
		this.paint(g);
	}

	public void paint(Graphics g) {
		super.paint(g);
	}

	public void exitGameWorld() {
		iRenderX = 0;
		iRenderY = 0;
		gameState = GameState.INIT;
		soundManager.getThreadPool().shutdown();
		subPathManager.getThreadPool().shutdown();
		frame.dispose();
		new GameCore();
	}

	public JFrame getFrame() {
		return frame;
	}

	public TileManager getTileManager() {
		return tileManager;
	}

	boolean isInDrag(int sx, int sy, int ex, int ey, int x, int y, int width,
			int length) {
		if (sx >= ex && sy > ey) { // from bottom right to top left
			Rectangle rect1 = new Rectangle(ex, ey, Math.abs(sx - ex),
					Math.abs(sy - ey));
			Rectangle rect2 = new Rectangle(x, y, width, length);
			return rect1.intersects(rect2);

		}
		if (sx <= ex && sy > ey) {// from bottom left to top right
			Rectangle rect1 = new Rectangle(sx, ey, Math.abs(sx - ex),
					Math.abs(sy - ey));
			Rectangle rect2 = new Rectangle(x, y, width, length);
			return rect1.intersects(rect2);
		}
		if (sx <= ex && sy < ey) {// from top left to bottom right
			Rectangle rect1 = new Rectangle(sx, sy, Math.abs(ex - sx),
					Math.abs(ey - sy));
			Rectangle rect2 = new Rectangle(x, y, width, length);
			return rect1.intersects(rect2);
		} else { // from top right to bottom left
			Rectangle rect1 = new Rectangle(ex, sy, Math.abs(sx - ex),
					Math.abs(ey - sx));
			Rectangle rect2 = new Rectangle(x, y, width, length);
			return rect1.intersects(rect2);

		}
	}

	/**
	 * 
	 * @Title: getWorldSize
	 * @Description: TODO(What the method do)
	 * @param @return
	 * @return Dimension
	 * @throws
	 */
	public Dimension getWorldSize() {
		return new Dimension(this.mapPanel.getMapWidthPx(),
				this.mapPanel.getMapHeightPx());
	}

	public FocusManager getFocusManager() {
		return focusManager;
	}

	public GridMap getGridMap() {
		return gridMapManager.gridMap;
	}

	public BuildPanel getBuildPanel() {
		return buildPanel;
	}

	public void setNetworkManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
		this.commandPool.setNetworkManager(networkManager);
		// commandFactory.setTileManager(tileManager);
		commandFactory.setGameWorld(this);
		commandFactory.setGridMap(gridMapManager.gridMap);
		commandFactory.setCommandPool(commandPool);
		// commandFactory.setFocusManager(focusManager);
		chatPanel.setCommandPool(this);

		// this.preloadCommandThread.start();
		if (networkManager.getServerSocket() != null) {
			Config.IS_SERVER = true;
		} else {
			Config.IS_SERVER = false;
		}
	}

	public ChatPanel getChatPanel() {
		return chatPanel;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public CommandPool getCommandPool() {
		return commandPool;
	}

	//Trigger the init sprite method in tile manager to init the tile in the game start
	public void initSprite() {
		try {
			if (networkManager.getServerSocket() != null) {
				while (tileManager.getState() != TileManagerState.READY) {
					System.out.println("NotReady");
					tileManager.init();
					commandPool.execute();
				}
				commandFactory.createCommand("ReadyCommand");
				System.out.println("no of commnad:"
						+ commandPool.getCommandPool().size());
			}
		} catch (Throwable e) {
			if (Config.DEBUGMODE) {
				if (Config.DEBUGMODE) {
					e.printStackTrace();
				}
			}
		}
	}

	public void startGame() {
		iRenderX = 3200;
		iRenderY = 1200;
		gameWorldThread.start();
		frame.requestFocusInWindow();

	}

	public CommandFactory getCommandFactory() {
		return commandFactory;
	}

	public static GameMode getGameMode() {
		return gameMode;
	}

	public static SubPathManager getSubPathManager() {
		return subPathManager;
	}

	public static Team getTeam(PlayerRole playerRole) {
		return teams.get(playerRole);
	}

	public static long getTimeLeft() {
		return timeLeft;
	}

	public static SoundManager getSoundManager() {
		return soundManager;
	}

	public static void changeReval() {
		shadowPanel.setVisible(!shadowPanel.isVisible());
	}

	public static void setWinningTeam(PlayerRole role) {
		winningTeam = role;
	}

	public void gameEnd(PlayerRole role) {
		//Show appropriate message and play music when user win / lose the game
		if (role.equals(Config.PLAYER_ROLE)) {
			Image bimage = ImageLoader.loadImage(Config.IMG_WINNING_PATH);
			JLabel jLabel = new JLabel(new ImageIcon(bimage));
			jLabel.setLocation(
					GameWorld.this.getWidth() / 2 - bimage.getWidth(null) / 2,
					GameWorld.this.getHeight() / 2 - bimage.getHeight(null) / 2);
			jLabel.setSize(bimage.getWidth(null), bimage.getHeight(null));
			GameWorld.this.add(jLabel, JLayeredPane.DRAG_LAYER);
			soundManager.getSoundPlayer(Config.MUSIC_WINNING_PATH).play();
		} else {
			Image bimage = ImageLoader.loadImage(Config.IMG_LOSING_PATH);
			JLabel jLabel = new JLabel(new ImageIcon(bimage));
			jLabel.setLocation(
					GameWorld.this.getWidth() / 2 - bimage.getWidth(null) / 2,
					GameWorld.this.getHeight() / 2 - bimage.getHeight(null) / 2);
			jLabel.setSize(bimage.getWidth(null), bimage.getHeight(null));
			GameWorld.this.add(jLabel, JLayeredPane.DRAG_LAYER);
			soundManager.getSoundPlayer(Config.MUSIC_LOSING_PATH).play();
		}

		GameWorld.this.timer.cancel();
		gameState = GameState.END;
	}

	
	//Update player's team resource 
	public void consumeTeamResource(PlayerRole role, Resource resource) {
		commandFactory.setPlayerRole(role);
		commandFactory.setResourse(resource.getFood(), resource.getWood(),
				resource.getIron(), resource.getSurvivor());
		commandFactory.createCommand("ConsumeResourceCommand");
	}
}