package com.darkdensity.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.core.GameWorld;
import com.darkdensity.setting.Config;
import com.darkdensity.util.ImageLoader;


/**
 * @ClassName: MapPanel
 * @Description: Store and show the map panel (it will now handle user input)
 * @author Team A1 - Hei Yin Wong
 * @date 24 Mar 2014 23:01:48
*/

public class MapPanel extends JPanel {
	private Image map;
	private JFrame frame;
	
	public MapPanel(JFrame frame) {
		this.frame = frame;
		setLayout(null);
		setSize(frame.getWidth(), frame.getHeight());
		//Get map image
		map = ImageLoader.loadImage(Config.MAP);
	}

	/**
	 * paint the part of map that show the in the screen
	 */
	@Override
	public void paintComponent(Graphics g) {
		
		//Redraw the map into correction location
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(map, 0 - GameWorld.iRenderX, 0 - GameWorld.iRenderY, this);
	}
	
	/**
	 * 
	* @Title: getMapWidthPx 
	* @Description: get the width of the map
	* @param @return
	* @return int    
	* @throws
	 */
	public int getMapWidthPx(){
		return map.getWidth(null);
	}
	
	/**
	* @Title: getMapHeightPx 
	* @Description: get the height of the map 
	* @param @return
	* @return int    
	* @throws
	 */
	public int getMapHeightPx(){
		return map.getHeight(null);
	}
}
