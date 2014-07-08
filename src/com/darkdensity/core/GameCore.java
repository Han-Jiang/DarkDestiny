package com.darkdensity.core;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.gui.MainMenuPanel;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.GameMode;
import com.darkdensity.sound.MusicPlayer;
import com.darkdensity.util.ImageLoader;




/**
* @ClassName: GameCore
* @Description: Entry point for the whole game
* @author Team A1 - Hei Yin Wong
*/

public class GameCore {

	private JPanel displayPanel;
	public static JFrame frame;
	GameWorld gameWorld = null;
	
	public static MusicPlayer mu;
	
	/**
	 * 
	* <p>Title: </p> Game core
	* <p>Description: </p> a JFrame that contains all the game gui
	 */
	public GameCore() {
		if(!Config.DEBUGMODE){
			System.out.close();
		}
		// Switch to full screen 
		ScreenManager screen = new ScreenManager();
		DisplayMode displayMode = screen.getCurrentDisplayMode();
		screen.setFullScreen(displayMode);
		
		// Initialize frame
		if(Config.IS_FULL_SCREEN){
			frame = screen.getFullScreenWindow();
		}else{
			frame = new JFrame();
			frame.setSize(1024, 700);// set the size
			frame.setFocusable(true);
			frame.setResizable(false);// forbid resize
			frame.setLocationRelativeTo(null);// center
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setUndecorated(true);
		}
		
		frame.setBackground(Color.white);
		frame.setForeground(Color.white);
		frame.setTitle(Config.PANEL_TITLE);
		frame.setLayout(null);
		// Set the frame to transparent
		((JComponent) frame.getContentPane()).setOpaque(false);
		frame.setVisible(true);
		
		
		// Set the cursor for the game
		frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				ImageLoader.loadImage(Config.CURSOR_PATH), new Point(11, 2), "cursor"));
		
		// Show main menu
		MainMenuPanel mp = new MainMenuPanel(frame);
		displayPanel = mp;
		frame.setContentPane(displayPanel);
		
		// Play background music
		mu = new MusicPlayer(Config.BACKGROUND_MUSIC_PATH);
		mu.start();
	}


}
