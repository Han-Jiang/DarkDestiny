package com.darkdensity.gui;


/**
 * @ClassName: GameLoadingPanel
 * @Description: TODO(What the class do)
 * @author Team A1 - Han Jiang, Ting Yuen Lam
 * @date 24 Mar 2014 23:01:48
 */



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.darkdensity.setting.Config;
import com.darkdensity.util.ImageLoader;

public class GameLoadingPanel extends AbstractPanel {
	private int borderWidth = 50;
	private int borderHeight = 50;
	private JLabel loadingBar, runningSurvivor, runningZombie;
	private Image zombie, human, vs;
	private double progress;
	private int distance;

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> an loading panel shows the loading information of the game 
	* @param frame
	* @throws IOException
	 */
	public GameLoadingPanel(JFrame frame) throws IOException {
		super(frame);
		setLayout(null);
		setSize(frame.getWidth(), frame.getHeight());
		setBackground(Color.BLACK);

		//iamge shows on the top
		zombie = ImageLoader.loadImage(Config.ZOMBIE_IMAGE);
		human = ImageLoader.loadImage(Config.SURVIVOR_IMAGE);
		vs = ImageLoader.loadImage(Config.VS_IMAGE);
		
		// a running huamn
		runningSurvivor = new JLabel(new ImageIcon(Config.RUNNING_SURVIVOR));
		runningSurvivor.setSize(65, 60);
		runningSurvivor.setLocation(getWidth() / 3 * 2, getHeight() * 4 / 5 - runningSurvivor.getHeight());
		add(runningSurvivor);
		
		// a runinig zombie
		runningZombie = new JLabel(new ImageIcon(Config.RUNNING_ZOMBIE));
		runningZombie.setSize(88, 87);
		runningZombie.setLocation(getWidth() / 3, getHeight() * 4 / 5 - runningZombie.getHeight());
		add(runningZombie);
		//get the distance between the huamn and teh zombie
		distance = getWidth() / 3 - runningZombie.getWidth();
	}

	/**
	 * paint the panel
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(human, getWidth() - 400 - borderWidth , borderHeight, 400, 400, this);
		g2d.drawImage(zombie, borderWidth , borderHeight,
				400, 400, this);
		g2d.drawImage(vs, 400 + borderWidth, borderHeight, 400, 400, this);
	}

	/**
	 * 
	* @Title: setProgress 
	* @Description: update the loading progress of the game 
	* @param @param progress
	* @return void    
	* @throws
	 */
	public void setProgress(double progress) {
		this.progress = progress;
		runningZombie.setLocation((int) (getWidth() / 3 + distance * progress / 100), runningZombie.getLocation().y);
	}
}
