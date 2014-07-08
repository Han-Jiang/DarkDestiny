package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.darkdensity.core.GameWorld;
import com.darkdensity.core.GridMapManager;
import com.darkdensity.core.TileManager;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.maprender.Grid;
import com.darkdensity.player.Resource;
import com.darkdensity.player.Team;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.BarricadeClass;
import com.darkdensity.setting.Constant.BarricadeDirection;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.tile.Barricade;
import com.darkdensity.tile.LargeBarricade;
import com.darkdensity.tile.SmallBarricade;

/**
 * 
 * @ClassName: BuildPanel
 * @Description: BuildPanel, panel for build barricade
 * @author Team A1 - Hei Yin Wong, Ting Yuen Lam
 * @date 19 Mar 2014 14:47:53
 */
public class BuildPanel extends JPanel implements KeyListener, MouseListener,
		MouseMotionListener {
	GameWorld gameWorld;
	CommandFactory commandFactory;
	GridMapManager gridMapManager;
	TileManager tileManager;

	// for showing the size do barricade
	int rectWidth, rectHeight, rectX, rectY;
	Boolean allow;

	HashMap<BarricadeDirection, ArrayList<Point>> blockingAreas;
	HashMap<Point, Boolean> checkResultHashMap;

	BarricadeDirection barricadeDirection;

	LargeBarricade largeBarricade;
	SmallBarricade smallBarricade;

	// current barricade, can be small or big
	Barricade currBarricade;
	Team survivorTeam;

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> This panel is for building barricade, it show the barricade's
	* animation and show it with different color to indicate whether it can be build at that place
	* @param gameworld
	* @throws IOException
	 */
	public BuildPanel(GameWorld gameworld) throws IOException {
		// initial the panel
		setSize(gameworld.getWidth(), gameworld.getHeight());
		// setSize(100, 100);
		this.setLayout(null);
		setFocusable(true);
		setVisible(true);
		setOpaque(false);
		setLocation(0, 0);

		this.gameWorld = gameworld;
		this.commandFactory = gameworld.getCommandFactory();
		this.survivorTeam = gameWorld.getTeam(PlayerRole.SURVIVOR);
		blockingAreas = Constant.SB_BLOCKINGAREA;

		barricadeDirection = barricadeDirection.RIGHT;

		// add key listener for chang of the barricade's position
		this.addKeyListener(this);
		gameworld.getFrame().addKeyListener(this);
		// gameworld.getFrame().addMouseListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		// this.requestFocus();

		
		tileManager = gameworld.getTileManager();

		// initial barricade
		largeBarricade = new LargeBarricade(0, 0, tileManager);
		smallBarricade = new SmallBarricade(0, 0, tileManager);
		largeBarricade.setDirection(barricadeDirection);
		smallBarricade.setDirection(barricadeDirection);
		currBarricade = smallBarricade;
	}

	/**
	 * 
	 * @Title: areaCheck
	 * @Description: check whether the area is allowed to build the barricade
	 * @param
	 * @return void
	 * @throws
	 */
	public void areaCheck() {
		allow = true;
		checkResultHashMap = new HashMap<Point, Boolean>();
		// ArrayList<Point> tmp = blockingAreas.get(barricadeDirection);
		for (Point p : blockingAreas.get(barricadeDirection)) {
			Point point = new Point(GameWorld.actualX + p.x
					* Constant.TILE_WIDTH, GameWorld.actualY + p.y
					* Constant.TILE_HEIGHT);
			Grid grid = gridMapManager.gridMap.getGrid(
					gridMapManager.pxToTileX(GameWorld.iRenderX + point.x),
					gridMapManager.pxToTileY(GameWorld.iRenderY + point.y));
			Boolean check = (grid != null);
			checkResultHashMap.put(point, (check && !grid.isBlocked()));
			if (!check || grid.isBlocked()) {
				allow = false;
			}
		}
	}

	/**
	 * 
	* @Title: resourceCheck 
	* @Description: check whether the player has enough resource to build brricade
	* @param @return
	* @return Boolean    
	* @throws
	 */
	public Boolean resourceCheck() {
		Resource consumeResource = currBarricade.getConsumeResource();
		if (survivorTeam.getWood() < consumeResource.getWood()
				|| survivorTeam.getIron() < consumeResource.getWood()) {
			return false;
		}
		;
		return true;
	}

	/**
	 * Setter for the Grid Map Manager
	 */
	public void setGridMapManager(GridMapManager gridMapManager) {
		this.gridMapManager = gridMapManager;
	}

	/**
	 * paint all the barricades
	 */
	@Override
	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		rectWidth = currBarricade.getTileWidth();
		rectHeight = currBarricade.getTileHeight();
		rectX = GameWorld.actualX - rectWidth / 2;
		rectY = GameWorld.actualY - rectHeight / 2;
		
		if (resourceCheck()) {
			areaCheck();	
			Iterator<Point> iterator = checkResultHashMap.keySet().iterator();
			// if the area is blocking, draw it with red, else draw it with
			// green
			while (iterator.hasNext()) {
				Point point = iterator.next();
				if (checkResultHashMap.get(point)) {
					g2d.setColor(Color.GREEN);
				} else {
					g2d.setColor(Color.RED);
				}
				Rectangle rect = new Rectangle(point.x, point.y,
						Constant.TILE_WIDTH, Constant.TILE_HEIGHT);
				g2d.fill(rect);
			}
		} else {
			g2d.setColor(Color.RED);
			Rectangle rect = new Rectangle(rectX, rectY,
					currBarricade.getWidth(), currBarricade.getHeight());
			g2d.fill(rect);
		}
		//draw the barricade image
		g2d.drawImage(currBarricade.getImage(), rectX, rectY,
				currBarricade.getTileWidth(), currBarricade.getTileHeight(),
				this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Key listener for the panel,
		// use to control the direction of the barricade
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			barricadeDirection = (barricadeDirection == BarricadeDirection.LEFT) ? BarricadeDirection.RIGHT
					: BarricadeDirection.LEFT;
			currBarricade.setDirection(barricadeDirection);
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.setVisible(false);
			e.consume();
		} else {
			gameWorld.dispatchEvent(e);
		}
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		gameWorld.dispatchEvent(e);
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		gameWorld.dispatchEvent(e);
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		gameWorld.dispatchEvent(e);
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		gameWorld.dispatchEvent(e);
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		gameWorld.dispatchEvent(e);
	}

	/**
	 * build a barricade where the mouse is clicked
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// check if it is left click and the area is allowed for building
		if (SwingUtilities.isLeftMouseButton(e) && allow) {
			commandFactory.setFocusID(UUID.randomUUID());
			commandFactory.setPointX(GameWorld.iRenderX + rectX);
			commandFactory.setPointY(GameWorld.iRenderY + rectY);
			commandFactory
					.setTileName(currBarricade.getClass().getSimpleName());
			commandFactory.setBarricadeDirection(barricadeDirection);
			commandFactory.createCommand("BarricadeCommand");
		} else {
			gameWorld.dispatchEvent(e);
		}
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		gameWorld.dispatchEvent(e);
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		gameWorld.dispatchEvent(e);
	}

	/**
	 * dispatch the key event to the gameWorld
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		gameWorld.dispatchEvent(e);
	}

/**
 * 
* @Title: setBarricade 
* @Description: set the barricade attribution
* @param @param barricade
* @return void    
* @throws
 */
	public void setBarricade(BarricadeClass barricade) {
		blockingAreas = (barricade == BarricadeClass.LargerBarricade) ? Constant.LB_BLOCKINGAREA
				: Constant.SB_BLOCKINGAREA;
		currBarricade = (barricade == BarricadeClass.LargerBarricade) ? largeBarricade
				: smallBarricade;
		currBarricade.setDirection(barricadeDirection);
	}
}
