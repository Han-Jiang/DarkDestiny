package com.darkdensity.tile;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

import javax.swing.JComponent;

import com.darkdensity.core.FocusManager;
import com.darkdensity.core.GameWorld;
import com.darkdensity.core.TileManager;
import com.darkdensity.gui.TilePanel;
import com.darkdensity.setting.Constant.PlayerRole;


public abstract class Tile extends JComponent implements MouseListener, MouseMotionListener 
		{
	protected int x, y;
	protected int tileX, tileY;
	protected boolean focusing, entered, destroyed;
	protected int maxHealth, health, defense, reveal;
	protected UUID UUID;
	protected int tileWidth, tileHeight;

	protected FocusManager focusManager;
	protected TileManager tileManager;
	protected TilePanel tilePanel;
	protected PlayerRole role;
//	protected JLabel imageLabel;

	public Tile(){
		this.focusing = this.entered = false;
		this.destroyed = false;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
//		imageLabel = new JLabel();
//		imageLabel.setVisible(true);
//		add(imageLabel);
	}
	
	public void setTilePanel(TilePanel tilePanel) {
		this.tilePanel = tilePanel;
	}

	public void update(long elapsedTime) throws IOException {
	}

	@Override
	public int getX() {		
		return x;
	}

	public void setX(int x) {
		this.x = x;
		this.setLocation(x, y);
	}
	
	@Override
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		this.setLocation(x, y);
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public int getTileX() {
		return tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public boolean isFocusing() {
		return focusing;
	}

	public void focus() {
		focusing = true;
	}

	public void blur() {
		focusing = false;
	}
	
	public void entered(){
		entered = true;
	}
	
	public void exited(){
		entered = false;
	}


	public int getHealth() {
		return health;
	}
	
	public void updateHealth(int update){
		this.health += update;
		if(this.health <= 0){
			this.health = 0;
			this.destroyTile();
		} else if(this.health >= maxHealth) {
			this.health = maxHealth;
		}
	}

	public UUID getUUID() {
		return UUID;
	}

	public void setUUID(UUID uuid) {
		UUID = uuid;
	}

	public float getDefence() {
		return 0;
	}

	public Point getPoint() {
		return new Point(x, y);
	}
	
	// Mouse listenser
	@Override
	public void mouseDragged(MouseEvent e) {
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false);
		tilePanel.dispatchEvent(e2);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//entered = (clickAlphaValue(e.getX(), e.getY()) > 0)? true: false;
		//System.out.println("Alpha: " + (clickAlphaValue(e.getX(), e.getY()) > 0) + " entered: " + entered);
		//MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false);
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false);
		tilePanel.dispatchEvent(e2);
//		System.out.println("Mouse move: " + clickAlphaValue(e.getX(), e.getY()));
		entered = isAlpha(e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.print("Mouse clicked in tile at: " + this.getX() + " Y: "
				//+ this.getY());
		//this.gameWorld.dispatchEvent(e);
//		System.out.println("Mouse Click on Tile: " +  this.getClass().getSimpleName());
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false);
		tilePanel.dispatchEvent(e2);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
//		System.out.println("Mouse Entered: " + this.getClass());
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), GameWorld.iRenderX + e.getXOnScreen(), GameWorld.iRenderY + e.getYOnScreen(), e.getClickCount(), false);
		tilePanel.dispatchEvent(e2);
//		if(this instanceof Sprite)
//			System.out.println(clickAlphaValue(e.getX(), e.getY()));
//		entered = true;
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
//		System.out.println("Mouse Exited: " + this.getClass());
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false);
		tilePanel.dispatchEvent(e2);
		entered = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
//		System.out.println("mouse Press on Tile: " +  this.getClass().getSimpleName());
//		System.out.println(this.getParent().getClass().getSimpleName());
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false);
		tilePanel.dispatchEvent(e2);
//		this.gameWorld.dispatchEvent(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false);
		tilePanel.dispatchEvent(e2);
	};

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}
	
	public abstract BufferedImage getImage();
	
	private Boolean isAlpha(int x, int y) 
	  {
	    return (this.getImage().getRGB(x, y) & 0xFF000000) == 0xFF000000;
	  }

	public void setGameWorld(GameWorld gameWorld) {
		this.tileManager = gameWorld.getTileManager();
	}

	public void setTileManager(TileManager tileManager) {
		this.tileManager = tileManager;
	}
	
	public void setFocusManager(FocusManager focusManager) {
		this.focusManager = focusManager;
	}

	@Override
	public boolean contains(int x, int y) {
		if(super.contains(x, y)){
			return isAlpha(x, y);
		}
		return false;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}		
	
	public abstract void progressUpdate(int n);
	
	public void destroyTile() {
		if(this instanceof Sprite){
			((Sprite)this).die_sound();
		}
		tileManager.DestroyTile(this.getUUID());
	}
//	public void paintComponents(Graphics g) {
////		super.paintComponents(g);
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.drawImage(this.getImage(), this.getX() - GameWorld.iRenderX, this.getY()
//				- GameWorld.iRenderY, this.tileWidth, this.tileHeight, this);
//	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void processDestory(){
		this.destroyed = true;
	}
	
	public PlayerRole getRole() {
		return role;
	}

	public int getReveal() {
		return reveal;
	}
}
