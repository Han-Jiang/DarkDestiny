package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.darkdensity.core.GameWorld;
import com.darkdensity.core.TileManager;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.tile.Building;
import com.darkdensity.tile.Survivor;
import com.darkdensity.tile.Survivor2;
import com.darkdensity.tile.Tile;
import com.darkdensity.tile.Zombie;
import com.darkdensity.util.ImageLoader;


/**
 * 
* @ClassName: MiniMapPanel
* @Description: the mini map, show the tiles' position on the minimap and control map scrolling
* @author Team A1
* @date Mar 28, 2014 12:09:21 AM
=======
 * @ClassName: GameWorld
 * @Description: TODO(What the class do)
 * @author Team A1 - Han Jiang
 * @date 24 Mar 2014 23:01:48
 */
public class MiniMapPanel extends AbstractPanel {
	private int width = 263;
	private int height = 210;
	private int miniMapWidth = 243;
	private int miniMapHeight = 192;
	private int borderSizeTop = 9;
	private int borderSizeLeft = 14;
	private int scale = 20;

	private int rectX = 0;
	private int rectY = 0;
	private int rectWidth = 0;
	private int rectHeight = 0;

	private GameWorld gameWorld;
	private TileManager tileManager;
	private MouseListener listener = new MouseListener();

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> construct a minimap with JFrame and tile manager
	* @param frame
	* @param tileManager
	 */
	public MiniMapPanel(JFrame frame, TileManager tileManager) {
		super(frame);
		setSize(width, height);// set the size

		this.gameWorld = tileManager.getGameWorld();
		this.tileManager = tileManager;

		rectWidth = frame.getWidth() / scale;
		rectHeight = frame.getHeight() / scale;

		setFocusable(false);
		setVisible(true);

		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}

	/**
	 * paint the paint component
	 */
	public void paintComponent(Graphics g) {
		
		rectX = GameWorld.iRenderX / scale + borderSizeLeft;
		rectY = GameWorld.iRenderY / scale + borderSizeTop;
		Graphics2D g2d = (Graphics2D) g;
		Image border = ImageLoader.loadImage(Config.MINIMAP_BORDER);
		// load the mini map image
		Image miniMap = ImageLoader.loadImage(Config.MINIMAP);

		// draw the mini map and border
		g2d.drawImage(miniMap, borderSizeLeft, borderSizeTop, this);

		// draw all the sprite on the minimap
		g2d.setColor(Color.blue);
		for (Tile tile : tileManager.getAllTile()) {
			// draw with different color according to type
			if (tile.getRole() == PlayerRole.NEUTRAL) {
				g2d.setColor(Color.yellow);
			} else if (tile.getRole() == Config.PLAYER_ROLE) {
				g2d.setColor(Color.blue);
			} else {
				if (tile.getRole() != Config.PLAYER_ROLE && Config.DEBUGMODE) {
					g2d.setColor(Color.red);
				} else {
					continue;
				}
			}

			int tileX = (tile.getX() + tile.getTileWidth() / 2) / scale + borderSizeLeft;
			int tileY = (tile.getY() + tile.getTileHeight() / 2) / scale + borderSizeTop;
			g2d.drawRect(tileX, tileY, 5, 5);
		}

		g2d.setColor(Color.red);
		g2d.drawRect(rectX, rectY, rectWidth, rectHeight);
		g2d.drawImage(border, 0, 0, this);
	}

	/**
	 * 
	* @ClassName: MouseListener
	* @Description: use to control the map scrolling
	* @author Team A1
	* @date Mar 28, 2014 1:10:36 AM
	 */
	private class MouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			moveMiniMap(e.getX(), e.getY());
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			MouseEvent e2 = new MouseEvent((Component) e.getSource(),
					e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(),
					e.getYOnScreen(), e.getClickCount(), false, e.getButton());
			gameWorld.dispatchEvent(e2);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			moveMiniMap(e.getX(), e.getY());
		}

		/**
		 * 
		 * @Title: moveMiniMap
		 * @Description: move the rectangle on the minimap by taking (x,y) as
		 *               it's center point
		 * @param int x x position on the map
		 * @param int y y position on the map
		 * @return void
		 * @throws
		 */
		public void moveMiniMap(int x, int y) {
			x -= borderSizeLeft;
			y -= borderSizeTop;

			if (x > 0 && x < miniMapWidth && y > 0 && y < miniMapHeight) {

				// limiti bound of the minimap
				if (x < rectWidth / 2) {
					x = rectWidth / 2;
				}
				if (y < rectHeight / 2) {
					y = rectHeight / 2;
				}

				if (x > (miniMapWidth - rectWidth / 2)) {
					x = miniMapWidth - rectWidth / 2;
				}
				if (y > (miniMapHeight - rectHeight / 2)) {
					y = miniMapHeight - rectHeight / 2;
				}
				// move the game world rendering point
				GameWorld.iRenderX = (x - rectWidth / 2) * scale;
				GameWorld.iRenderY = (y - rectHeight / 2) * scale;
			}
		}

	}

}
