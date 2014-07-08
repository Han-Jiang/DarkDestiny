package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.darkdensity.player.Resource;
import com.darkdensity.setting.Config;
import com.darkdensity.tile.Barricade;
import com.darkdensity.tile.Building;
import com.darkdensity.tile.Sprite;
import com.darkdensity.tile.Tile;
import com.darkdensity.util.ImageLoader;

/**
 * 
* @ClassName: InfoPanel
* @Description: GUI, show the sprite info which is currently focusing
* @author Team A1 - Ting Yuen Lam
* @date 19 Mar 2014 15:10:14
 */
public class InfoPanel extends AbstractPanel {

	private Image infoPanel;
	private Tile focusingTile = null;
	private JLabel iconJLabel, firstIcon, thirdIcon, secondIcon, forthIcon,
			firstLabel, thirdLabel, secondLabel, forthLabel;
	private Rectangle progressBar;
	private int progressBarWidth = 188;
	
	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> Construct  a new instance by a JFrame 
	* @param frame
	 */
	public InfoPanel(JFrame frame) {
		super(frame);
		//initial the JPanel
		infoPanel = ImageLoader.loadImage(Config.INFO_PANEL_PATH);
		setLayout(null);
		//set the size 
		setSize(infoPanel.getWidth(null), infoPanel.getHeight(null));
		setFocusable(false);

		//define the items that shows in the info panel
		iconJLabel = new JLabel();
		
		//icon for each item
		firstIcon = new JLabel();
		firstIcon.setHorizontalAlignment(JLabel.CENTER);
		secondIcon = new JLabel();
		secondIcon.setHorizontalAlignment(JLabel.CENTER);
		thirdIcon = new JLabel();
		thirdIcon.setHorizontalAlignment(JLabel.CENTER);
		forthIcon = new JLabel();
		forthIcon.setHorizontalAlignment(JLabel.CENTER);
		
		//value for each item
		firstLabel = new JLabel();
		thirdLabel = new JLabel();
		secondLabel = new JLabel();
		forthLabel = new JLabel();
		
		//location and size for each item
		iconJLabel.setLocation(36, 15);
		iconJLabel.setSize(187, 112);
		iconJLabel.setHorizontalAlignment(JLabel.CENTER);

		firstIcon.setSize(24, 24);
		firstIcon.setLocation(240, 20);

		secondIcon.setSize(24, 24);
		secondIcon.setLocation(240, 50);

		thirdIcon.setSize(24, 24);
		thirdIcon.setLocation(240, 80);

		forthIcon.setSize(24, 24);
		forthIcon.setLocation(240, 110);

		//location and size for each item
		firstLabel.setSize(50, 24);
		firstLabel.setLocation(270, 20);
		firstLabel.setForeground(Color.white);

		secondLabel.setSize(50, 24);
		secondLabel.setLocation(270, 50);
		secondLabel.setForeground(Color.white);

		thirdLabel.setSize(50, 24);
		thirdLabel.setLocation(270, 80);
		thirdLabel.setForeground(Color.white);

		forthLabel.setSize(50, 24);
		forthLabel.setLocation(270, 110);
		forthLabel.setForeground(Color.white);

		//add all the items to the panel
		add(iconJLabel);
		add(firstIcon);
		add(secondIcon);
		add(thirdIcon);
		add(forthIcon);
		
		add(firstLabel);
		add(secondLabel);
		add(thirdLabel);
		add(forthLabel);
		
		//blood bar for the tile
		progressBar = new Rectangle(35, 137, 0, 0);
	}

	/**
	 * paint the panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(infoPanel, 0, 0, this);
	
		if (focusingTile != null) {
			//get the image
			Image i = focusingTile.getImage();
			//if the image is too big, it should be resized
			if (focusingTile.getWidth() > 187 || focusingTile.getHeight() > 112) {
				i = ImageLoader.resize(focusingTile.getImage(), 187, 112);
			}
			//show the image
			iconJLabel.setIcon(new ImageIcon(i));
			//show the progress bar or the blood bar
			updateText();
			if (focusingTile instanceof Building) {
				progressBar.setSize(progressBarWidth * ((Building) focusingTile).getProgress() / ((Building) focusingTile).getProgressMaximum() , 5);
			} else {
				progressBar.setSize(progressBarWidth * focusingTile.getHealth() / focusingTile.getMaxHealth(), 5);
			}
		}
		g2.setColor(Color.yellow);
		g2.fill(progressBar);
	}

	/**
	 * 
	* @Title: setFocusingTile 
	* @Description: Set the focusing tile to show the detail infomation of it
	* @param @param focusingTile
	* @return void    
	* @throws
	 */
	public void setFocusingTile(Tile focusingTile) {
		this.focusingTile = focusingTile;
		if (focusingTile == null) {
			//only show when there is a focusing tile
			this.setVisible(false);
		} else {
			this.setVisible(true);
			//show resources information when it is building
			if (focusingTile instanceof Building) {
				firstIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_FOOD)));
				thirdIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_IRON)));
				secondIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_WOOD)));
				forthIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_SURVIVOR)));
			} else {
				//if is human or zombie, show creature information,
				firstIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_HEALTH)));
				secondIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_ATTACK)));
				thirdIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_DEFENSE)));
				forthIcon.setIcon(new ImageIcon(ImageLoader
						.loadImage(Config.ICON_SPEED)));
			}
			//update the text of teh label
			updateText();
		}
	}
	
	private void updateText(){
		//update the building resources information
			if (focusingTile instanceof Building) {
				Resource r = ((Building) focusingTile).getResource();
				
				//when the building has no resource
				if (r == null) {
					firstLabel.setText(": -");
					secondLabel.setText(": -");
					thirdLabel.setText(": -");
					forthLabel.setText(": -");
				} else {
					firstLabel.setText(": " + r.getFood());
					secondLabel.setText(": " + r.getWood());
					thirdLabel.setText(": " + r.getIron());
					forthLabel.setText(": " + r.getSurvivor());
				}
				progressBar.setSize(progressBarWidth * ((Building) focusingTile).getProgress() / ((Building) focusingTile).getProgressMaximum() , 5);
			} else {
				//when the tile si a sprite, show the health and other information
				firstLabel.setText(": " + focusingTile.getHealth());
				thirdLabel.setText(": " + focusingTile.getDefense());

				if (focusingTile instanceof Barricade) {
					secondLabel.setText(": -");
					forthLabel.setText(": -");
				} else {
					secondLabel.setText(": " + ((Sprite) focusingTile).getAttack());
					forthLabel.setText(": " + ((Sprite) focusingTile).getSpeed());
				}
				progressBar.setSize(progressBarWidth * focusingTile.getHealth() / focusingTile.getMaxHealth(), 5);
			}
		
	}
}
