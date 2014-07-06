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
 * ****************************************
 * 
 * @ClassName: SpritePanel **************************************** Attributes:
 *             private Image dbImage; private Graphics dbGraphics; private int
 *             gameWorld.iRenderX; private int gameWorld.iRenderY;
 * 
 *             private tileManager tileManager;
 * 
 *             BufferedImage bf; JLabel label;
 *             ***************************************** Methods: public
 *             SpritePanel(tileManager tileManager,JFrame frame); public void
 *             paintComponent(Graphics g); public int getgameWorld.iRenderX();
 *             public void setgameWorld.iRenderX(int gameWorld.iRenderX) ;
 *             public int getgameWorld.iRenderY(); public void
 *             setgameWorld.iRenderY(int gameWorld.iRenderY) ; public void
 *             reset();
 */

public class TilePanel extends JPanel implements MouseListener,
		MouseMotionListener {
	private TileManager tileManager;
	private GameWorld gameWorld;
	private FocusManager focusManager;

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

	public void initTile(Tile t) {
		t.setVisible(true);
		t.setTilePanel(this);
		this.add(t);
	}

	public void removeTile(Tile t) {
		this.remove(t);
	}

	public void setFocusManager(FocusManager focusManager) {
		this.focusManager = focusManager;
	}
}
