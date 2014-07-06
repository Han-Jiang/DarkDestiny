package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.NLS;

/*
 * ****************************************
 * Class: MainMenuPanel
 * ****************************************
 * Attributes:
 * public class GamePanel extends JLayeredPane:
 * private final int gameMode;
 * private ControlManager controlManager;
 * private MapPanel mapPanel;
 * private MiniMapPanel miniMapPanel;
 * private SpriteManager spriteManager;
 * private SpritePanel spritePanel;
 * private boolean isRunning = true;
 * private Map map;
 * private JFrame frame;
 * private Sprite sprite;
 * *****************************************
 * Methods:
 * public GamePanel(JFrame frame, int gameMode) throws IOException;
 * public void init() throws IOException;
 * public void gameLoop();
 * public boolean gameLoopCore() ;
 * public void update(long elapsedTime) throws Throwable;
 * public void cameraScolling();
 * private void redrawScreen(Graphics2D g);
 * public void exitGameWorld();
 * public JFrame getFrame();
 * public SpriteManager getSpriteManager();
 * boolean isInDrag(int sx, int sy, int ex, int ey, int x, int y, int width, int length);
 */
public class MainMenuPanel extends AbstractPanel {
	private MainMenuMenuItem menuItem;

	public MainMenuPanel(JFrame frame) {
		super(frame);
		JPanel menuPanel = new MainMenuMenuItem();
		menuPanel.setLocation(frame.getWidth() - menuPanel.getWidth(), 0);
		add(menuPanel);
	}
    
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw the background image
		g.drawImage(super.background, 0, 0, getWidth(), getHeight(), this);

	}

	@Override
	public void update(long elapsedTime) {
//		menuItem.update(elapsedTime);
	}

	@Override
	public void reset() {
		// item.elastic.slide(0, 100);
	}
}
