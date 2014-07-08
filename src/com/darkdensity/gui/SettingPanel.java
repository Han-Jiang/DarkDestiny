package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.darkdensity.core.NetworkManager;
import com.darkdensity.net.lobby.ChangeCheatRequest;
import com.darkdensity.net.lobby.ChangeGameTimeRequest;
import com.darkdensity.net.lobby.ChangeRevealRequest;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.NLS;
import com.darkdensity.util.ImageLoader;

/**
 * 
 * @ClassName: SettingPanel
 * @Description: Setting panel for the game
 * @author Team A1
 * @date 19 Mar 2014 15:39:29
 */
public class SettingPanel extends JPanel implements DocumentListener,
		ItemListener {

	private JTextField gameTimetxt;
	private JLabel gameTimelbl, gameTimeValuelbl;

	private JLabel dark_lable;
	private JCheckBox dark_box;

	private JLabel cheat_lable;
	private JCheckBox cheat_box;

	private ButtonGroup difficulty_bg;
	private JLabel difficulty_lable;
	private JRadioButtonMenuItem difficulty_easy;
	private JRadioButtonMenuItem difficulty_normal;
	private JRadioButtonMenuItem difficulty_hard;
	private JRadioButtonMenuItem difficulty_impossible;

	private NLS nls;
	private NetworkManager networkManager;

	/**
	 * 
	* <p>Title: </p>Setting panel
	* <p>Description: </p> Setting panel provide setting for survive time and other options
	 */
	public SettingPanel() {
		this.nls = NLS.getInstance();
		// for single player
		this.setLayout(null);
		this.setSize(270, 600);
		this.setOpaque(false);

		//setting for the survive time
		gameTimelbl = AbstractPanel.getStyledLable(nls.winningTime_lbl);
		gameTimelbl.setLocation(10, 0);
		this.add(gameTimelbl);

		gameTimetxt = new JTextField();
		gameTimetxt.setLocation(
				gameTimelbl.getLocation().x + gameTimelbl.getWidth() + 20,
				gameTimelbl.getLocation().y);
		Dimension dimension = new Dimension(gameTimelbl.getWidth(), 30);
		gameTimetxt.setSize(dimension);
		gameTimetxt.setVisible(true);
		gameTimetxt.getDocument().addDocumentListener(this);
		AbstractPanel.setUnder(gameTimetxt, gameTimelbl, 5);
		this.add(gameTimetxt);

		
		//setting for te full reveal
		dark_lable = AbstractPanel.getStyledLable(nls.reveallbl);
		this.add(dark_lable);

		dark_box = AbstractPanel.getStyledCheckBox(nls.revealfull,
				Config.IS_FULL_REVEAL);

		this.add(dark_box);
		AbstractPanel.setUnder(dark_lable, gameTimelbl, 40);
		AbstractPanel.setUnder(dark_box, dark_lable, 5);
		dark_box.addItemListener(this);

		//setting for cheat
		cheat_lable = AbstractPanel.getStyledLable(nls.chatlbl);
		this.add(cheat_lable);

		cheat_box = AbstractPanel.getStyledCheckBox(nls.onlbl,
				Config.IS_ALLOW_CHEAT_MODE);

		this.add(cheat_box);
		AbstractPanel.setUnder(cheat_lable, dark_box, 20);
		AbstractPanel.setUnder(cheat_box, cheat_lable, 5);
		cheat_box.addItemListener(this);

		//setting for difficulty
		difficulty_lable = AbstractPanel.getStyledLable(nls.difficultylbl);
		this.add(difficulty_lable);
		difficulty_bg = new ButtonGroup();
		difficulty_easy = AbstractPanel
				.getStyledRadioButton(nls.difficulty_easylbl);
		difficulty_normal = AbstractPanel
				.getStyledRadioButton(nls.difficulty_normallbl);
		difficulty_hard = AbstractPanel
				.getStyledRadioButton(nls.difficulty_hard);
		difficulty_impossible = AbstractPanel
				.getStyledRadioButton(nls.difficlty_impossible);

		difficulty_bg.add(difficulty_easy);
		difficulty_bg.add(difficulty_normal);
		difficulty_bg.add(difficulty_hard);
		difficulty_bg.add(difficulty_impossible);
		this.add(difficulty_easy);
		this.add(difficulty_normal);
		this.add(difficulty_hard);
		this.add(difficulty_impossible);
		AbstractPanel.setUnder(difficulty_lable, cheat_box, 20);
		AbstractPanel.setUnder(difficulty_easy, difficulty_lable, 5);
		difficulty_easy.setLocation(
				this.getWidth() / 2 - difficulty_easy.getWidth(),
				difficulty_easy.getLocation().y);
		AbstractPanel.setLeft(difficulty_normal, difficulty_easy, 10);
		AbstractPanel.setUnder(difficulty_hard, difficulty_easy, 5);
		AbstractPanel.setLeft(difficulty_impossible, difficulty_hard, 10);

	}

	/**
	 * 
	* <p>Title: </p>Setting panel for the network
	* <p>Description: </p> Setting panel provide setting for survive time and other options
	 */
	public SettingPanel(NetworkManager networkManager) {
		this.nls = NLS.getInstance();
		// for multi-player
		this.setLayout(null);
		this.setSize(270, 600);
		this.setBackground(Color.red);
		this.setOpaque(false);

		//setting for the survive time
		gameTimelbl = AbstractPanel.getStyledLable(nls.winningTime_lbl);
		gameTimelbl.setLocation(10, 0);
		this.add(gameTimelbl);

		if (networkManager != null && networkManager.getServerSocket() != null) {
			this.networkManager = networkManager;
			gameTimetxt = new JTextField();
			gameTimetxt.setLocation(
					gameTimelbl.getLocation().x + gameTimelbl.getWidth() + 20,
					gameTimelbl.getLocation().y);
			Dimension dimension = new Dimension(gameTimelbl.getWidth(), 30);
			gameTimetxt.setSize(dimension);
			gameTimetxt.setVisible(true);
			gameTimetxt.getDocument().addDocumentListener(this);
			AbstractPanel.setUnder(gameTimetxt, gameTimelbl, 5);
			this.add(gameTimetxt);
		} else {
			gameTimeValuelbl = new JLabel("0");
			gameTimeValuelbl.setLocation(130, 0);
			gameTimeValuelbl.setSize(120, 20);
			this.initLabel(gameTimeValuelbl);
		}

		//setting for the full reveal
		dark_lable = AbstractPanel.getStyledLable(nls.reveallbl);
		this.add(dark_lable);

		dark_box = AbstractPanel.getStyledCheckBox(nls.revealfull,
				Config.IS_FULL_REVEAL);

		this.add(dark_box);
		AbstractPanel.setUnder(dark_lable, gameTimelbl, 40);
		AbstractPanel.setUnder(dark_box, dark_lable, 5);
		dark_box.addItemListener(this);

		//setting for cheat
		cheat_lable = AbstractPanel.getStyledLable(nls.chatlbl);
		this.add(cheat_lable);

		cheat_box = AbstractPanel.getStyledCheckBox(nls.onlbl,
				Config.IS_ALLOW_CHEAT_MODE);

		this.add(cheat_box);
		AbstractPanel.setUnder(cheat_lable, dark_box, 20);
		AbstractPanel.setUnder(cheat_box, cheat_lable, 5);
		cheat_box.addItemListener(this);

		//setting for difficulty
		difficulty_lable = AbstractPanel.getStyledLable(nls.difficultylbl);
		this.add(difficulty_lable);
		difficulty_bg = new ButtonGroup();
		difficulty_easy = AbstractPanel
				.getStyledRadioButton(nls.difficulty_easylbl);
		difficulty_normal = AbstractPanel
				.getStyledRadioButton(nls.difficulty_normallbl);
		difficulty_hard = AbstractPanel
				.getStyledRadioButton(nls.difficulty_hard);
		difficulty_impossible = AbstractPanel
				.getStyledRadioButton(nls.difficlty_impossible);

		difficulty_bg.add(difficulty_easy);
		difficulty_bg.add(difficulty_normal);
		difficulty_bg.add(difficulty_hard);
		difficulty_bg.add(difficulty_impossible);
		this.add(difficulty_easy);
		this.add(difficulty_normal);
		this.add(difficulty_hard);
		this.add(difficulty_impossible);
		AbstractPanel.setUnder(difficulty_lable, cheat_box, 20);
		AbstractPanel.setUnder(difficulty_easy, difficulty_lable, 5);
		AbstractPanel.setLeft(difficulty_normal, difficulty_easy, 10);
		AbstractPanel.setUnder(difficulty_hard, difficulty_easy, 5);
		AbstractPanel.setLeft(difficulty_impossible, difficulty_hard, 10);

	}

	/**
	 * @Title: initLabel
	 * @Description:initial the lable
	 */
	private void initLabel(JLabel jLabel) {
		jLabel.setIgnoreRepaint(true);
		jLabel.setForeground(Color.WHITE);
		this.add(jLabel);
	}

	/**
	 * 
	 * @Title: getGameTime
	 * @Description: get the game time
	 */
	public int getGameTime() {
		return Integer.parseInt(this.gameTimetxt.getText()) * 60000;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		inputValidate(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		inputValidate(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		inputValidate(e);
	}

	/**
	 * 
	* @Title: inputValidate 
	* @Description: check whether the input is valid or not
	* @param @param e
	* @return void    
	* @throws
	 */
	public void inputValidate(DocumentEvent e) {
		Document doc = e.getDocument();
		int gameTime;
		try {

			String tmp = doc.getText(0, doc.getLength());
			gameTime = Integer.parseInt(tmp);
			if (networkManager != null) {
				((MultiPlayerSettingPanel) this.getParent()).noError();
				ChangeGameTimeRequest request = new ChangeGameTimeRequest(
						gameTime);
				networkManager.getGameLobby().sendRequest(request);
			}

			Config.GAME_WINNING_TIME = gameTime * 60000;
		} catch (Throwable t) {
			if (networkManager != null) {
				((MultiPlayerSettingPanel) this.getParent()).showError();
			}
		}

	}
/**
 * 
* @Title: getGameTimeValuelbl 
* @Description: get the game time value label
* @param @return
* @return JLabel    
* @throws
 */
	public JLabel getGameTimeValuelbl() {
		return gameTimeValuelbl;
	}

	/**
	 * listener for all the setting option 
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getItemSelectable();

		ImageIcon imageIcon_selected = ImageLoader
				.getImageIcon("res/images/panel/checkbox_selected.png");
		ImageIcon imageIcon_unselected = ImageLoader
				.getImageIcon("res/images/panel/checkbox_unselected.png");

		if (source == dark_box) {
			if (dark_box.isSelected()) {
				Config.IS_FULL_REVEAL = true;
				dark_box.setIcon(imageIcon_selected);
			} else {
				Config.IS_FULL_REVEAL = false;
				dark_box.setIcon(imageIcon_unselected);
			}
			if (networkManager != null
					&& networkManager.getServerSocket() != null) {
				ChangeRevealRequest request = new ChangeRevealRequest(
						Config.IS_FULL_REVEAL);
				networkManager.getGameLobby().sendRequest(request);
			}
		}

		if (source == cheat_box) {
			if (cheat_box.isSelected()) {
				Config.IS_CHEAT_ALLOWED = true;
				cheat_box.setIcon(imageIcon_selected);
			} else {
				Config.IS_CHEAT_ALLOWED = false;
				cheat_box.setIcon(imageIcon_unselected);
			}
			if (networkManager != null
					&& networkManager.getServerSocket() != null) {
				ChangeCheatRequest request = new ChangeCheatRequest(
						Config.IS_CHEAT_ALLOWED);
				networkManager.getGameLobby().sendRequest(request);
			}
		}

	}

	/**
	 * 
	* @Title: setGameTimeText 
	* @Description: set the game time
	* @param @param s
	* @return void    
	* @throws
	 */
	public void setGameTimeText(String s) {
		this.gameTimelbl.setText(nls.winningTime_lbl + ": " + s);
	}

	/**
	 * 
	* @Title: setCheatState 
	* @Description:change the cheat setting
	* @param 
	* @return void    
	* @throws
	 */
	public void setCheatState() {
		this.cheat_box.setSelected(Config.IS_CHEAT_ALLOWED);

	}

	/**
	 * 
	* @Title: setRevealState 
	* @Description: change reveal setting
	* @param 
	* @return void    
	* @throws
	 */
	public void setRevealState() {
		this.dark_box.setSelected(Config.IS_FULL_REVEAL);
	}

}
