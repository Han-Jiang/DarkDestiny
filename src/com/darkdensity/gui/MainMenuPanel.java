package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.NLS;

/**
 * @ClassName: MainMenuPanel
 * @Description: Display the main menu to user
 * @author Team A1 - Hei Yin Wong
 * @date 24 Mar 2014 23:01:48

 */



public class MainMenuPanel extends AbstractPanel {
	private MainMenuMenuItem menuItem;

	public MainMenuPanel(JFrame frame) {
		super(frame);
		//add the menu panel
		JPanel menuPanel = new MainMenuMenuItem();
		menuPanel.setLocation(frame.getWidth() - menuPanel.getWidth(), 0);
		add(menuPanel);
	}
    
	/**
	 * paint the panel with background image
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw the background image
		g.drawImage(super.background, 0, 0, getWidth(), getHeight(), this);

	}

}
