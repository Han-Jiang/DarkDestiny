package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.darkdensity.core.GameWorld;
import com.darkdensity.net.chat.ChatPanel;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.GameMode;

/*
 * ****************************************
 * Class: SoloPanel
 * ****************************************
 * Attributes:
 * private JButton startBtn, backBtn;
 * ActionListener listenner;
 * *****************************************
 * Methods:
 * private void initButton(JButton button);
 * public void setRectangleXY(int x, int y) ;
 * public void redraw(Graphics g) ;
 * public void paintComponent(Graphics g);
 * public void update(long elapsedTime);
 * public void reset();
 */

/**
 * 
 * @ClassName: SoloPanel
 * @Description: Solo panel of the game, provide setting of solo mode of the
 *               game
 * @author Team A1
 * @date 19 Mar 2014 15:41:43
 */
public class SoloPanel extends AbstractPanel {
	private JButton startGameBtn, backBtn;

	private ArrayList<JLabel> playerLabelList;

	private SettingPanel settingPanel;
	private ArrayList<JButton> teamButtonList;

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> Solo panel, to start game in solo mode
	* @param parent
	 */
	public SoloPanel(JFrame parent) {
		super(parent);

		setLayout(null);

		//palyer name 
		JLabel playerListlbl = new JLabel(nls.playernamelbl);
		playerListlbl.setLocation(30, 100);
		playerListlbl.setSize(100, 20);
		this.initLabel(playerListlbl);
		this.playerLabelList = new ArrayList<JLabel>();
		this.teamButtonList = new ArrayList<JButton>();

		JLabel playerLabel = new JLabel(Config.PLAYER_NAME);
		initLabel(playerLabel);
		playerLabel.setLocation(30, 100 + 20);
		playerLabel.setSize(200, 20);
		this.playerLabelList.add(playerLabel);

		//set a team
		JButton teamButton = new JButton();
		teamButton.setLocation(200, 100 + 20);
		teamButton.setSize(150, 20);
		teamButton.setText(nls.teambtn_human);
		this.initButton(teamButton);
		this.teamButtonList.add(teamButton);

		JLabel playerLabel2 = new JLabel("Computer");
		initLabel(playerLabel2);
		playerLabel2.setLocation(30, 100 + 40);
		playerLabel2.setSize(200, 20);
		this.playerLabelList.add(playerLabel2);

		JButton teamButton2 = new JButton();
		teamButton2.setLocation(200, 100 + 40);
		teamButton2.setSize(150, 20);
		teamButton2.setText(nls.teambtn_zombie);
		this.initButton(teamButton2);
		this.teamButtonList.add(teamButton2);

		//start the game
		startGameBtn = new JButton(nls.startBtn);
		initButton(startGameBtn);
		startGameBtn.setSize(100, 20);
		startGameBtn.setLocation(parent.getWidth() - startGameBtn.getWidth(),
				20);

		settingPanel = new SettingPanel();
		this.add(settingPanel);
		settingPanel
				.setLocation(this.getWidth() - settingPanel.getWidth(), 100);

	}

	/**
	 * 
	* @Title: initLabel 
	* @Description: add attributes and listener to the label
	* @param @param jLabel
	* @return void    
	* @throws
	 */
	private void initLabel(JLabel jLabel) {
		jLabel.setIgnoreRepaint(true);
		jLabel.setForeground(Color.WHITE);
		this.add(jLabel);
	}

	/**
	 * 
	* @Title: initButton 
	* @Description:  add attributes and listener to the button
	* @param @param button
	* @return void    
	* @throws
	 */
	protected void initButton(JButton button) {
		super.initButton(button, listenner);
		this.add(button);

	}

	//paint the panel
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	// add listener to the button
	ActionListener listenner = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton) e.getSource();
			JFrame jFrame = (JFrame) SoloPanel.this.getRootPane().getParent();
			if (jb.equals(startGameBtn)) {
				try {
					jFrame.setContentPane(new GameWorld(jFrame, GameMode.SOLO));
				} catch (Throwable e1) {

					e1.printStackTrace();
				}
			} else if (jb.equals(backBtn)) {
				jFrame.setContentPane(new MainMenuPanel(jFrame));
			}

		}
	};

}
