package com.darkdensity.core;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.darkdensity.factory.CommandFactory;
import com.darkdensity.gui.FuncPanel;
import com.darkdensity.gui.InfoPanel;
import com.darkdensity.maprender.Grid;
import com.darkdensity.setting.Config;
import com.darkdensity.tile.Sprite;
import com.darkdensity.tile.Tile;

/**
 * 
 * @ClassName: FocusManager
 * @Description: Focus manager, handle all the mouse choosing event include left
 *               click, left drag and right click
 * @author Team A1 - Han Jiang, Yingjing Feng, Ting Yuen Lam, Hei Yin Wong
 */
/**
* @ClassName: FocusManager
* @Description: TODO(What the class do)
* @author Team A1
* @date 25 Mar 2014 04:28:46
*/

public class FocusManager {
	private ArrayList<Tile> focusingTile = new ArrayList<Tile>();
	private GridMapManager gridMapManager;
	private CommandFactory commandFactory;
	private FuncPanel funcPanel;
	private InfoPanel infoPanel;
	
	public FocusManager() {}

	/** 
	* @Title: setCommandFactory 
	* @Description: set the ComandFactory
	* @param @param commandFactory
	* @return void    
	* @throws 
	*/
	public void setCommandFactory(CommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}

	/**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param gridMapManager
	*/
	
	public FocusManager(GridMapManager gridMapManager) {
		this.gridMapManager = gridMapManager;
	}

	/** 
	* @Title: setFuncPanel 
	* @Description: set funcPanel
	* @param @param funcPanel
	* @return void    
	* @throws 
	*/ 
	public void setFuncPanel(FuncPanel funcPanel) {
		this.funcPanel = funcPanel;
	}

	/** 
	* @Title: setInfoPanel 
	* @Description: set infoPanel
	* @param @param infoPanel
	* @return void    
	* @throws 
	*/ 
	
	public void setInfoPanel(InfoPanel infoPanel) {
		this.infoPanel = infoPanel;
	}

	/** 
	* @Title: getGridMapManager 
	* @Description: get gridMapManager
	* @param @return
	* @return GridMapManager    
	* @throws 
	*/ 
	
	public GridMapManager getGridMapManager() {
		return gridMapManager;
	}

	/** 
	* @Title: setGridMapManager 
	* @Description: set gridMapManager
	* @param @param gridMapManager
	* @return void    
	* @throws 
	*/ 
	
	public void setGridMapManager(GridMapManager gridMapManager) {
		this.gridMapManager = gridMapManager;
	}

	/**
	 * 
	 * @Title: focus
	 * @Description: focus a list of grids
	 * @param @param tiles list of tile to be focused
	 * @return void
	 * @throws
	 */
	public void focus(ArrayList<Tile> tiles) {
			
		blur();
		for (Tile tile : tiles) {

			if(tile instanceof Sprite){
				((Sprite) tile).speak();
			}
			tile.focus();
			focusingTile.add(tile);
		}
		if (focusingTile.size() > 0) {
			infoPanel.setFocusingTile(focusingTile.get(0));
		}
	}
	
	/** 
	* @Title: highlight 
	* @Description: highlight the tiles
	* @param @param tiles
	* @return void    
	* @throws 
	*/
	public void highlight(ArrayList<Tile> tiles) {
		blur();
		for (Tile t : tiles) {
			
			t.focus();
			focusingTile.add(t);
		} 
		if (focusingTile.size() > 0) {
			infoPanel.setFocusingTile(focusingTile.get(0));
		}
	}

	/** 
	* @Title: blur 
	* @Description: to cancel highlight of tile
	* @param 
	* @return void    
	* @throws 
	*/ 
	private void blur() {
		for (Tile t : focusingTile) {
			t.blur();
		}
		focusingTile.clear();
		infoPanel.setFocusingTile(null);
	}

	/**
	 * @Title: focus
	 * @Description: focus from (fx,fy) to (tx,ty) for dragging
	 * @param int fx unit in tile
	 * @param int fy int in tile
	 * @param int tx int in tile
	 * @param int ty int in tile
	 * @return void
	 * @throws
	 */
	public void highlight(int fx, int fy, int tx, int ty) {
		Point fromTile = GridMapManager.pxToTile(fx, fy);
		Point toTile = GridMapManager.pxToTile(tx, ty);
		ArrayList<Tile> tiles = getTiles(fromTile, toTile);
		highlight(tiles);
	}
	
	/** 
	* @Title: focus 
	* @Description: focus a dragging rectangle
	* @param @param fx
	* @param @param fy
	* @param @param tx
	* @param @param ty
	* @return void    
	* @throws 
	*/ 
	
	public void focus(int fx, int fy, int tx, int ty) {
		Point fromTile = GridMapManager.pxToTile(fx, fy);
		Point toTile = GridMapManager.pxToTile(tx, ty);
		ArrayList<Tile> tiles = getTiles(fromTile, toTile);
		focus(tiles);
	}

	/** 
	* @Title: target 
	* @Description: set the tiles as target
	* @param @param p
	* @param @param targets
	* @return void    
	* @throws 
	*/ 
	
	public void target(Point p, ArrayList<Tile> targets) {
		for (Tile tile : focusingTile) {
			if (tile instanceof Sprite && ((Sprite) tile).getRole() == Config.PLAYER_ROLE) {
				if (targets.size() == 0) {
					move((Sprite) tile, p.x, p.y);
					((Sprite) tile).setTargetTile(null);
				} else {
					((Sprite) tile).setTargetTile(targets.get(0));
					move((Sprite) tile, targets.get(0).getX()
							+ targets.get(0).getTileWidth() / 2, targets.get(0)
							.getY() + targets.get(0).getTileHeight() / 2);
				}
			}
		}
	}

	/**
	 * 
	 * @Title: getTiles
	 * @Description: get all the tile from fromTile to toTile as the start and
	 *               end point of a rectangle of tiles
	 * @param @param fromTile
	 * @param @param toTile
	 * @param @return
	 * @return ArrayList<Tile>
	 * @throws
	 */
	private ArrayList<Tile> getTiles(Point fromTile, Point toTile) {
		ArrayList<Tile> result = new ArrayList<Tile>();
		for (int y = fromTile.y; y <= toTile.y; ++y) {
			for (int x = fromTile.x; x < toTile.x; ++x) {
				Grid grid = GridMapManager.gridMap.getGrid(x, y);
				if (grid != null) {
					for (Tile tile : grid) {
						result.add(tile);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @Title: move
	 * @Description: move the focuing tile to point(x,y)
	 * @param @param x
	 * @param @param y
	 * @return void
	 * @throws
	 */
	public void move(Sprite sprite, int x, int y) {

		commandFactory.setPointX(x);
		commandFactory.setPointY(y);
		commandFactory.setFocusID(sprite.getUUID());
		commandFactory.createCommand("MoveCommand");
	}

	/**
	 * @Title: getfocusingSprites
	 * @Description: get all the focusing tiles
	 * @return ArrayList<Tile> list of focusing tile
	 * @throws
	 */
	public ArrayList<Tile> getfocusingSprites() {
		return focusingTile;
	}
}
