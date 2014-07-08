package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.darkdensity.core.FocusManager;
import com.darkdensity.core.GameWorld;
import com.darkdensity.core.TileManager;
import com.darkdensity.setting.Config;
import com.darkdensity.tile.Building;
import com.darkdensity.tile.Tile;

/**
 * 
* @ClassName: TilePanel
* @Description: Tile panel is responsible for contain the sprite and buildings
* @author Team A1
* @date Mar 28, 2014 2:11:13 AM
 */
public class TilePanel extends JPanel implements MouseListener,
		MouseMotionListener {
	private TileManager tileManager;
	private GameWorld gameWorld;
	private FocusManager focusManager;

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> construct a new tile panel with a new tile manager
	* @param tileManager
	 */
	public TilePanel(TileManager tileManager) {
		this.gameWorld = tileManager.getGameWorld();
		setSize(gameWorld.getWorldSize().width, gameWorld.getWorldSize().height);
		this.setLayout(null);
		setFocusable(true);
		setVisible(true);
		setOpaque(false);
		setLocation(0, 0);
		this.tileManager = tileManager;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * paint the tile panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		ArrayList<Tile> tiles = tileManager.getAllTile();
		g2d.setColor(Color.WHITE);
		for (Tile tile : tiles) {
			if ((tile.getX() + tile.getWidth()) >= GameWorld.iRenderX
					&& (tile.getY() + tile.getHeight()) >= GameWorld.iRenderY
					&& tile.getX() <= (GameWorld.iRenderX + Config.PANEL_WIDTH)
					&& tile.getY() <= (GameWorld.iRenderY + Config.PANEL_HEIGHT)) {
				g2d.drawImage(tile.getImage(),
						tile.getX() - GameWorld.iRenderX, tile.getY()
								- GameWorld.iRenderY, this);
				if (Config.DEBUGMODE) {
					g2d.drawRect(tile.getX() - GameWorld.iRenderX, tile.getY()
					- GameWorld.iRenderY, tile.getWidth(), tile.getHeight());
				}
				tile.setLocation(tile.getX() - GameWorld.iRenderX, tile.getY()
						- GameWorld.iRenderY);
			}
		}
	}

	//mouse listener for all the tiles
	@Override
	public void mouseClicked(MouseEvent e) {
		ArrayList<Tile> focusTiles = new ArrayList<Tile>();
		Class targetClass = Building.class;
		int actualX = GameWorld.iRenderX + e.getX();
		int actualY = GameWorld.iRenderY + e.getY();
		for (Component component : this.getComponents()) {
			if (((Tile) component).contains(actualX - component.getX(), actualY
					- component.getY())) {
				focusTiles.add((Tile) component);
			}
		}
		if (SwingUtilities.isLeftMouseButton(e)) {
			focusManager.focus(focusTiles);
			e.consume();
		} else if (SwingUtilities.isRightMouseButton(e)) {
			Point p = new Point(GameWorld.iRenderX + e.getX(),
					GameWorld.iRenderY + e.getY());
			focusManager.target(p, focusTiles);
		} else {
			gameWorld.dispatchEvent(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.gameWorld.dispatchEvent(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.gameWorld.dispatchEvent(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.gameWorld.dispatchEvent(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.gameWorld.dispatchEvent(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.gameWorld.dispatchEvent(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.gameWorld.dispatchEvent(e);
	}

	/**
	 * 
	* @Title: initTile 
	* @Description: initial tile, set it to visible and add it this panel
	* @param @param t
	* @return void    
	* @throws
	 */
	public void initTile(Tile t) {
		t.setVisible(true);
		t.setTilePanel(this);
		this.add(t);
	}

	/**
	 * 
	* @Title: removeTile 
	* @Description: remove the tile
	* @param @param t
	* @return void    
	* @throws
	 */
	public void removeTile(Tile t) {
		this.remove(t);
	}

	/**
	 * 
	* @Title: setFocusManager 
	* @Description: set focus manager
	* @param @param focusManager
	* @return void    
	* @throws
	 */
	public void setFocusManager(FocusManager focusManager) {
		this.focusManager = focusManager;
	}
}
