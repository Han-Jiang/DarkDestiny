package com.darkdensity.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.core.GameWorld;
import com.darkdensity.setting.Config;
import com.darkdensity.util.ImageLoader;

/*
 * ****************************************
 * Class: MapPanel
 * ****************************************
 * Attributes:
 * private Map map;
 * private JFrame frame;
 * *****************************************
 * Methods:
 * public MapPanel(JFrame frame);
 * public Map getMap();
 * public void paintComponent(Graphics g);
 * public void update(long elapsedTime);
 * public void reset();
 */
public class MapPanel extends JPanel {
	private Image map;
	private JFrame frame;
	
	public MapPanel(JFrame frame) {
		this.frame = frame;
		setLayout(null);
		setSize(frame.getWidth(), frame.getHeight());
		map = ImageLoader.loadImage(Config.MAP);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(map, 0 - GameWorld.iRenderX, 0 - GameWorld.iRenderY, this);
	}
	
	public int getMapWidthPx(){
		return map.getWidth(null);
	}
	
	public int getMapHeightPx(){
		return map.getHeight(null);
	}
}
