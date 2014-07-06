package com.darkdensity.test;

import java.awt.Color;
import java.awt.DisplayMode;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.core.ScreenManager;
import com.darkdensity.gui.MainMenuPanel;
import com.darkdensity.setting.Config;
import com.darkdensity.sound.MusicPlayer;


public class ScreenManagerTest {
	
	
	public static void main(String[] args) {
		
		//Switch to full screen 
		ScreenManager screen = new ScreenManager();
		DisplayMode displayMode = screen.getCurrentDisplayMode();
		screen.setFullScreen(displayMode);
		
		//Initialize frame
		JFrame frame = screen.getFullScreenWindow();
		frame.setBackground(Color.white);
		frame.setForeground(Color.white);
		frame.setTitle(Config.PANEL_TITLE);
		frame.setLayout(null);
		((JComponent) frame.getContentPane()).setOpaque(false);
		
		
		JPanel panel = new JPanel();
		panel.setSize(500, 500);
		panel.setLocation(5, 5);
		panel.setBackground(Color.yellow);

		frame.setContentPane(panel);
		
		frame.setVisible(true);
				
	}

}
