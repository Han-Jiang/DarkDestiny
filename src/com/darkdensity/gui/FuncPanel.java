package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.core.FocusManager;
import com.darkdensity.core.GameWorld;
import com.darkdensity.player.Resource;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.BarricadeClass;
import com.darkdensity.tile.LargeBarricade;
import com.darkdensity.tile.SmallBarricade;
import com.darkdensity.util.ImageLoader;
/**
 * 
* @ClassName: FuncPanel
* @Description: Function panel, provide the function set which the player can choose 

* @author Team A1 - Ting Yuen Lam
*/
public class FuncPanel extends AbstractPanel implements MouseMotionListener{	
	private JButton buildBtn, bBtn, cBtn, smallBarricadeBtn, largeBarricadeBtn;
	
	private GameWorld gameWorld;
	private FocusManager focusManager;
	private JPanel objectPanel;
	private Image buildIcon, funcPanel, moreIcon, smallBarricadeIcon, largeBarricadeIcon;
	
	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> i construct a new function panel with JFrame
	* @param frame
	* @throws IOException
	 */
	public FuncPanel(final JFrame frame) throws IOException {
		//initial thee panle
		super(frame);
		buildIcon = ImageLoader.loadImage(Config.FUNC_PANEL_BUILDICON);
		funcPanel = ImageLoader.loadImage(Config.FUNC_PANEL_BG);
		moreIcon = ImageLoader.loadImage(Config.FUNC_PANEL_MOREICON);
		smallBarricadeIcon = ImageLoader.loadImage(Config.FUNC_PANEL_SMALLBARRICADEICON);
		largeBarricadeIcon = ImageLoader.loadImage(Config.FUNC_PANEL_LARGEBARRICADEICON);
		setSize(funcPanel.getWidth(null) * 3, funcPanel.getHeight(null) * 2);// set the size
		setVisible(true);
		setLayout(null);
		setOpaque(true);
		
		// add button
		buildBtn = new JButton();		
		bBtn = new JButton();
		cBtn = new JButton();
		smallBarricadeBtn = new JButton();
		largeBarricadeBtn = new JButton();
		
		//object panel use for cpalce barricade of different size 
		objectPanel = new JPanel();
		objectPanel.setSize(100, 200);
		objectPanel.setLocation(funcPanel.getWidth(null), 0);
		objectPanel.setVisible(false);
		objectPanel.setOpaque(true);
		objectPanel.setBackground(new Color(255, 255, 255, 0));
		
		//initial small barricade
		SmallBarricade tmpSmallBarricade = new SmallBarricade();
		Resource tmpResource = tmpSmallBarricade.getConsumeResource();
		smallBarricadeBtn.setIcon(new ImageIcon(smallBarricadeIcon));
		smallBarricadeBtn.setLocation(10, 10);
		smallBarricadeBtn.setSize(82, 82);
		smallBarricadeBtn.setVisible(true);
		smallBarricadeBtn.setToolTipText("Build Small Barricade: wood: " + tmpResource.getWood() + ", iron: " + tmpResource.getIron());
		
		//initial large barricade
		LargeBarricade tmpLargeBarricade = new LargeBarricade();
		tmpResource = tmpLargeBarricade.getConsumeResource();
		largeBarricadeBtn.setIcon(new ImageIcon(largeBarricadeIcon));
		largeBarricadeBtn.setLocation(10, 92);
		largeBarricadeBtn.setSize(82, 82);
		largeBarricadeBtn.setVisible(true);
		largeBarricadeBtn.setToolTipText("Build Large Barricade: wood: " + tmpResource.getWood() + ", iron: " + tmpResource.getIron());
	
		//add to the panle
		objectPanel.add(smallBarricadeBtn);
		objectPanel.add(largeBarricadeBtn);
		
		//initial the button and add them to the panel
		
		buildBtn.setIcon(new ImageIcon(buildIcon));
		buildBtn.setLocation(13, 10);
		buildBtn.setSize(40, 40);
		buildBtn.setVisible(true);
		buildBtn.setToolTipText("Build Function");
		
		bBtn.setIcon(new ImageIcon(moreIcon));
		bBtn.setLocation(13, 57);
		bBtn.setSize(40, 40);
		bBtn.setVisible(true);
		
		cBtn.setIcon(new ImageIcon(moreIcon));
		cBtn.setLocation(13, 104);
		cBtn.setSize(40, 40);
		cBtn.setVisible(true);
		
		buildBtn.addMouseMotionListener(this);
		bBtn.addMouseMotionListener(this);
		cBtn.addMouseMotionListener(this);
		
		initButton(buildBtn,null);
		initButton(bBtn,null);
		initButton(cBtn,null);
		initButton(smallBarricadeBtn,null);
		initButton(largeBarricadeBtn,null);
		
		add(objectPanel);
		add(buildBtn);
		add(bBtn);
		add(cBtn);
		
		this.addMouseMotionListener(this);
		
		//add action listener to the items
		buildBtn.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.requestFocusInWindow();
				objectPanel.setVisible(!objectPanel.isVisible());
			}
		});
		
		smallBarricadeBtn.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.requestFocusInWindow();
				objectPanel.setVisible(false);
				BuildPanel buildPanel = gameWorld.getBuildPanel();
				buildPanel.setBarricade(BarricadeClass.SmallBarricade);
				buildPanel.setVisible(true);
			}
		});
		
		largeBarricadeBtn.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.requestFocusInWindow();
				objectPanel.setVisible(false);
				BuildPanel buildPanel = gameWorld.getBuildPanel();
				buildPanel.setBarricade(BarricadeClass.LargerBarricade);
				buildPanel.setVisible(true);
			}
		});
	}
	
	/**
	 * 
	* @Title: setGameWorld 
	* @Description: TODO(What the method do) 
	*
	 */
	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	/**
	 * 
	* @Title: setFocusManager 
	* @Description: set the focus manager
	* @param @param focusManager
	* @return void    
	* @throws
	 */
	public void setFocusManager(FocusManager focusManager) {
		this.focusManager = focusManager;
	}


	/**
	 * paint out the function panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(funcPanel, 0, 0, this);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//gameWorld.dispatchEvent(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//dispacthe the mouse move event to the game world
		MouseEvent e2 = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getXOnScreen(), e.getYOnScreen(),e.getClickCount(),false);
		gameWorld.dispatchEvent(e2);
	}

}
