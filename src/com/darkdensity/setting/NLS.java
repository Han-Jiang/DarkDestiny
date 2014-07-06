package com.darkdensity.setting;

/**
 * 
 * @ClassName: NLS
 * @Description: English language file for the game
 * @author Team A1
 * @date 19 Mar 2014 16:21:12
 */
public class NLS {
	public static NLS nls;
	
	public String soloBtn = "Solo";
	public String coopBtn = "Coop";
	public String vsBtn = "Versus";
	public String startBtn = "Start";
	public String optionBtn = "Option";
	public String backBtn = "Back";
	public String exitBtn = "Exit";
	public String chatlbl = "Chat";
	public String saveBtn = "Save";

	// Setting page
	public String teambtn_human = "Human";
	public String teambtn_zombie = "Zombie";
	public String winningTime_lbl = "Game Time: (Min)";
	public String reveallbl = "Reveal";
	public String revealfull = "Full";
	public String cheatlbl = "Cheat";
	public String onlbl = "On";
	public String difficultylbl = "Difficulty Setting";
	public String  difficulty_easylbl = "Easy";
	public String difficulty_normallbl = "Normal";
	public String difficulty_hard = "Hard";
	public String difficlty_impossible = "Impossible";
	
	//multiplayer
	public String createGamebtn = "Create Game";
	public String iplbl = "IP:";
	public String connectbtn ="Connect";
	public String playernamelbl = "Player name:";
	
	//Option 
	public String languagelbl = "Language Setting";
	public String languageEng = "English";
	public String languageZh = "中文";
	public String language_french = "Français";
	public String language_spain = "Español";
	public String colorBlind = "Color blind mode";
	
	

	public static NLS getInstance() {
		NLS nls;
		try {
			nls = (NLS) Class.forName(Config.NLS_NAME).newInstance();
		} catch (Throwable e) {
			nls = new NLS();
		}
		return nls;
	}
}