package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.darkdensity.core.GameWorld;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.util.ImageLoader;
import com.sun.xml.internal.ws.client.ContentNegotiation;

/**
 * 
* @ClassName: TopMenuPanel
* @Description: top menu panel, provide the user with information 
* of how much resource he have 
* about the resource he have
* @author Team A1
* @date 19 Mar 2014 15:50:19
 */
public class TopMenuPanel extends AbstractPanel {
	private BufferedImage buffer = null;
	
	private Image menu;
	private JLabel lblFood, lblIron, lblWood, lblSurvivor, lblTime;
	private long leftTime;
	private static int count = 0;

	public TopMenuPanel(JFrame frame) {
		super(frame);
		setLayout(null);
		menu = ImageLoader.loadImage(Config.MENU_PATH);
		setSize(menu.getWidth(null), menu.getHeight(null));// set the size
//		setBackground(Color.red);
		
		//add lables for resource
		lblFood = new JLabel();
		lblIron = new JLabel();
		lblWood = new JLabel();
		lblSurvivor = new JLabel();
		lblTime = new JLabel();
		
		lblFood.setForeground(Color.white);
		lblIron.setForeground(Color.white);
		lblWood.setForeground(Color.white);
		lblSurvivor.setForeground(Color.white);
		lblTime.setForeground(Color.white);
		
		lblTime.setLocation(4, 0);
		lblTime.setSize(100,47);
		lblTime.setVisible(true);
		
		lblFood.setLocation(77, 0);
		lblFood.setSize(100,47);
		lblFood.setVisible(true);
		
		lblIron.setLocation(162, 0);
		lblIron.setSize(100,47);
		lblIron.setVisible(true);
		
		lblWood.setLocation(215, 0);
		lblWood.setSize(100,47);
		lblWood.setVisible(true);
		
		lblSurvivor.setLocation(269, 0);
		lblSurvivor.setSize(100,47);
		lblSurvivor.setVisible(true);

		add(lblTime);
		add(lblFood);
		add(lblIron);
		add(lblWood);
		add(lblSurvivor);
	}

	/**
	 * paint the panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(menu, 0, 0, this);
	}
	
	/**
	 * update the resource information
	 */
	public void update(long elapsedtime) { // 1000 millisecond = 1 second
		leftTime = GameWorld.getTimeLeft();
		int leftMinute = (int) (leftTime/(1000*60));
		int leftSecond = (int) (leftTime/1000-leftMinute*60);
		if( leftMinute <= 0 && leftSecond <= 0 )
			GameWorld.setWinningTeam(PlayerRole.SURVIVOR);
		else{
			lblTime.setText(String.valueOf((leftMinute/10==0)?"0"+leftMinute:leftMinute) + ":" + String.valueOf((leftSecond/10==0)?"0"+leftSecond:leftSecond));
		}
		Team team = GameWorld.getTeam(Config.PLAYER_ROLE);
		lblFood.setText(String.valueOf(team.getFood()));
		lblWood.setText(String.valueOf(team.getWood()));
		lblIron.setText(String.valueOf(team.getIron()));
		lblSurvivor.setText(String.valueOf(team.getNumOfSprite()));
//		System.out.println(Config.PLAYER_ROLE);
	}	
}
