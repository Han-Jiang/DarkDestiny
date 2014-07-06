package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.core.GameWorld;
import com.darkdensity.core.TileManager;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.tile.Sprite;
import com.darkdensity.tile.Tile;

enum ShadowState {
	INIT, READY
};

public class ShadowPanel extends JPanel {
	private JFrame frame;
	private TileManager tileManager;
	private GameWorld gameWorld;
	private ShadowState shadowState;
	private VolatileImage volatileImg;

	public ShadowPanel(JFrame frame) {
		this.frame = frame;
		shadowState = ShadowState.INIT;
		setVisible(true);
		setOpaque(false);
		setLayout(null);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsConfiguration gc = ge.getDefaultScreenDevice()
				.getDefaultConfiguration();
		BufferedImage buffer = gc.createCompatibleImage(frame.getWidth(),
				frame.getHeight(), Transparency.TRANSLUCENT);
		Graphics2D graphics = buffer.createGraphics();
		Area fogArea = new Area(new Rectangle(0, 0, frame.getWidth(),
				frame.getHeight()));
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		if (Config.PLAYER_ROLE == PlayerRole.SURVIVOR) {
			tiles.addAll(tileManager.getSurivors());
			tiles.addAll(tileManager.getBarricades());
		} else if (Config.PLAYER_ROLE == PlayerRole.ZOMBIE) {
			tiles.addAll(tileManager.getZombies());
		}

		for (Tile tile : tiles) {
			int centerX = tile.getX() + tile.getTileWidth() / 2
					- GameWorld.iRenderX - tile.getReveal() / 2;
			int centerY = tile.getY() + tile.getTileHeight() / 2
					- GameWorld.iRenderY - tile.getReveal() / 2;
			fogArea.subtract(new Area(new Ellipse2D.Float(centerX, centerY,
					tile.getReveal(), tile.getReveal())));
		}
		
		int alphaValue = 0;
		if(Config.PLAYER_ROLE == PlayerRole.SURVIVOR){
			alphaValue = (int) (Math
					.sin(GameWorld.getTimeLeft() % Constant.DAY_PERIOD * Math.PI / Constant.DAY_PERIOD) * 127 + 128);
		} else {
			alphaValue = (int) (Math
					.sin(GameWorld.getTimeLeft() % Constant.DAY_PERIOD * Math.PI / Constant.DAY_PERIOD) * 63 + 64);
		}
	
		graphics.setColor(new Color(0, 0, 0, alphaValue));
		graphics.fill(fogArea);
		graphics.dispose();
		g2d.drawImage(buffer, null, 0, 0);
	}

	public void setTileManager(TileManager tileManager) {
		this.tileManager = tileManager;
		this.gameWorld = tileManager.getGameWorld();
		setSize(gameWorld.getWorldSize().width, gameWorld.getWorldSize().height);
	}

	// This method produces a new volatile image.
	private void createBackBuffer() {
		GraphicsConfiguration gc = getGraphicsConfiguration();
		volatileImg = gc.createCompatibleVolatileImage(getWidth(), getHeight());
	}

}
