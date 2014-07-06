package com.darkdensity.setting;

import java.awt.Color;

import com.darkdensity.player.Resource;
import com.darkdensity.setting.Constant.GameMode;
import com.darkdensity.setting.Constant.PlayerRole;

/**
 * Config file, contains general setting information such as dimension, file
 * path, setting variables
 * 
 * @Author Team A1
 */
public class Config {

	// Game Settings
	public static long GAME_WINNING_TIME = 10 * 60000;
	public static final long ZOMBIE_GENERATION_TIME = 7500; // 7.5s
	public static final long CONSUME_FOOD_TIMER = 10000; // 10s


	public static final int UPDATE_PROGRESS_ELAPSEDTIME = 333;

	// Resource	
	public static final int SURVIVOR_START_FOOD = 50;
	public static final int SURVIVOR_START_WOOD = 0;
	public static final int SURVIVOR_START_IRON = 0;
	
	public static final int ZOMBIE_START_FOOD = 0;
	public static final int ZOMBIE_START_WOOD = 0;
	public static final int ZOMBIE_START_IRON = 0;
	
	
	public static final Resource LB_RESOURCE = new Resource(0, 50, 150, 0);
	public static final Resource SB_RESOURCE = new Resource(0, 30, 70, 0);
	public static final int SURVIVOR_CONSUMPTION = 5;

	public static final int LB_MAX_HEALTH = 1200;
	public static final int SB_MAX_HEALTH = 600;

	public static final int SURVIVOR1_MAX_HEALTH = 500;
	public static final int SURVIVOR2_MAX_HEALTH = 400;
	public static final int ZOMBIE1_HEALTH = 1200;

	public static final int SURVIVOR1_SPEED = 10;
	public static final int SURVIVOR2_SPEED = 9;
	public static final int ZOMBIE1_SPEED = 3;

	public static final int SURVIVOR1_ATTACK = 40;
	public static final int SURVIVOR2_ATTACK = 30;
	public static final int ZOMBIE1_ATTACK = 70;

	public static final int SURVIVOR1_REVEAL = 300;
	public static final int SURVIVOR2_REVEAL = 200;
	public static final int ZOMBIE1_REVEAL = 400;

	// General setting
	public static boolean DEBUGMODE = false;
	public static int PANEL_WIDTH = 1280;
	public static int PANEL_HEIGHT = 800;
	public static int SENSITIVE_AREA = 20;
	public static int SCREEN_MOVE_SPEED = 30;
	public static final boolean IS_FULL_SCREEN = true;
	// public static final String NLS_NAME =
	// "com.darkdensity.setting.NLS_zh_HK";
	public static String NLS_NAME = "com.darkdensity.setting.NLS";
	public static final String NLS_NAME_EN = "com.darkdensity.setting.NLS";
	public static final String NLS_NAME_ZH = "com.darkdensity.setting.NLS_zh";
	public static final String NLS_NAME_FR = "com.darkdensity.setting.NLS_fr";
	public static final String NLS_NAME_ES = "com.darkdensity.setting.NLS_es";

	// 768 is too large for my laptop
	public static final String PANEL_TITLE = "Dark Destiny";
	public static final boolean PANEL_IS_UNDECORATED = true;
	public static final boolean PANEL_RESIZABLE = false;
	public static final int PANEL_STARTUP_POSITION_X = 0;
	public static final int PANEL_STARTUP_POSITION_Y = 0;

	// Resource path
	private static final String RESOURCE_PATH = "res/";
	private static final String IMAGES_PATH = RESOURCE_PATH + "images/";
	private static final String MUSIC_PATH = RESOURCE_PATH + "music/";
	public static final String JSON_PATH = RESOURCE_PATH + "json/";

	public static final String SPRITE_PATH = IMAGES_PATH + "sprite/";
	public static final String BARRICADE_PATH = IMAGES_PATH + "barricade/";
	// public static final String BUILDING_PATH = IMAGES_PATH + "building/";
	public static final String BUILDING_PATH = IMAGES_PATH + "building_gif/";
	public static final String MAPSETTING_PATH = IMAGES_PATH + "map/";
	public static final String PANEL_PATH = IMAGES_PATH + "panel/";
	public static final String MINIMAP_PATH = IMAGES_PATH + "map/minimap/";
	public static final String LOADING_PATH = IMAGES_PATH + "loading/";

	public static final String BACKGROUND_MUSIC_PATH = MUSIC_PATH
			+ "underwater-delusion.wav";
	public static final String CURSOR_PATH = IMAGES_PATH + "cursor/normal.png";
	public static final String MUSIC_WINNING_PATH = MUSIC_PATH + "you_win.wav";
	public static final String IMG_WINNING_PATH = IMAGES_PATH + "Humanwin.png";
	public static final String IMG_LOSING_PATH = IMAGES_PATH + "Zombiewin.png";
	public static final String MUSIC_LOSING_PATH = MUSIC_PATH + "you_lose.wav";
	public static final String LOOBY_BACKGROUND = IMAGES_PATH
			+ "lobby/gameLobby.png";
	
	public static final String ICON_FOOD = IMAGES_PATH + "icon/food.png";
	public static final String ICON_WOOD = IMAGES_PATH + "icon/Dzwood.gif";
	public static final String ICON_IRON = IMAGES_PATH + "icon/Dzmetal.gif";
	public static final String ICON_SURVIVOR = IMAGES_PATH + "icon/BWBMy.png";
	public static final String ICON_ATTACK = IMAGES_PATH + "icon/attack.png";
	public static final String ICON_DEFENSE = IMAGES_PATH + "icon/defense.png";
	public static final String ICON_SPEED = IMAGES_PATH + "icon/speed.png";
	public static final String ICON_HEALTH = IMAGES_PATH + "icon/health.png";

	public static final String GAME_UI_CHECK_BOX_SELECTED = IMAGES_PATH
			+ "panel/checkbox_selected.png";
	public static final String GAME_UI_CHECK_BOX_UNSELECTED = IMAGES_PATH
			+ "panel/checkbox_unselected.png";
	public static final String GAME_UI_LABLE = IMAGES_PATH + "/panel/lable.png";
	public static final String GAME_UI_FUNCTION_BUTTON = IMAGES_PATH
			+ "/panel/start.png";
	public static final String GAME_UI_FUNCTION_BUTTON_DARK = IMAGES_PATH
			+ "/panel/function_button.png";

	public static final String FUNC_PANEL_BG = PANEL_PATH + "functionPanel.png";
	public static final String FUNC_PANEL_BUILDICON = PANEL_PATH
			+ "buildIcon.png";
	public static final String FUNC_PANEL_MOREICON = PANEL_PATH
			+ "moreIcon.png";
	public static final String FUNC_PANEL_SMALLBARRICADEICON = PANEL_PATH
			+ "smallBarricadeIcon.png";
	public static final String FUNC_PANEL_LARGEBARRICADEICON = PANEL_PATH
			+ "largeBarricadeIcon.png";
	public static final String MINIMAP_BORDER = PANEL_PATH + "minimapPanel.png";
	public static final String MINIMAP = MINIMAP_PATH + "minimap.jpg";
	public static final String MAP_FOREGROUND_PATH = MINIMAP_PATH + "fg"
			+ ".png";
	public static final String INFO_PANEL_PATH = PANEL_PATH + "infoPanel.png";
	public static final String MENU_PATH = PANEL_PATH + "topmenu.png";
	public static final String MAP = MINIMAP_PATH + "BigMap.jpg";
	public static final String SURVIVOR_IMAGE = LOADING_PATH + "human.png";
	public static final String ZOMBIE_IMAGE = LOADING_PATH + "zombie.png";
	public static final String VS_IMAGE = LOADING_PATH + "vs_red.png";
	public static final String RUNNING_SURVIVOR = LOADING_PATH
			+ "runningSurvivor.gif";
	public static final String RUNNING_ZOMBIE = LOADING_PATH
			+ "runningZombie.gif";
	public static final String IMAGE_BUTTON_PATH = IMAGES_PATH
			+ "panel/button.jpg";

	// Player information
	public static String PLAYER_NAME = "Bowie";

	public static PlayerRole PLAYER_ROLE = PlayerRole.SURVIVOR;

	// Network
	public static Boolean IS_SERVER = true;
	public static final int CHAT_PORT = 5000;
	public static final String CHAT_GROUP = "225.4.5.6";
	public static boolean IS_NETWORK_MODE = false;

	public static final int FRAMES_PER_SECOND = 48;
	public static final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

	public static int RESOURCE_DELAY = 20;

	public static Boolean IS_FULL_REVEAL = false;
	public static Boolean IS_COLOR_BLIND_MODE = true;
	public static Boolean IS_ALLOW_CHEAT_MODE = true;
	public static boolean IS_CHEAT_ALLOWED = true;

	public static final Color MENU_FILL_COLOR = new Color(0, 0, 0, 200);

}