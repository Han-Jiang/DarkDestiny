package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.NLS;
import com.darkdensity.util.ImageLoader;

/**
 * 
 * @ClassName: optionPanel
 * @Description: Setting panel for the game
 * @author Team A1
 * @date 19 Mar 2014 15:39:29
 */
public class OptionPanel extends JPanel implements ItemListener {

	private JButton saveButton;

	private JFrame frame;

	private JCheckBox colorBlind;

	private ButtonGroup language_bg;
	private JLabel languagelbl;
	private JRadioButtonMenuItem language_english;
	private JRadioButtonMenuItem language_chinese;
	private JRadioButtonMenuItem language_french;
	private JRadioButtonMenuItem language_spain;
	private NLS nls;

	/**
	 * 
	* <p>Title: </p> Option Panel
	* <p>Description: </p> Option panel provides option setting for the game
	* @param frame
	 */
	public OptionPanel(JFrame frame) {

		this.frame = frame;
		this.nls = NLS.getInstance();
		this.setLayout(null);
		this.setSize(frame.getWidth(), frame.getHeight());
		this.setBackground(new Color(0, 0, 0, 70));
		this.setOpaque(false);

		// add multi-language setting
		languagelbl = AbstractPanel.getStyledLable(nls.languagelbl);
		languagelbl.setLocation(getWidth() / 2 - 100, getHeight() / 2);
		this.add(languagelbl);
		language_bg = new ButtonGroup();
		language_english = AbstractPanel.getStyledRadioButton(nls.languageEng);
		language_chinese = AbstractPanel.getStyledRadioButton(nls.languageZh);
		language_french = AbstractPanel.getStyledRadioButton(nls.language_french);
		language_spain = AbstractPanel.getStyledRadioButton(nls.language_spain);

		language_bg.add(language_english);
		language_bg.add(language_chinese);
		language_bg.add(language_french);
		language_bg.add(language_spain);
		this.add(language_english);
		this.add(language_chinese);
		this.add(language_french);
		this.add(language_spain);
		language_chinese.addActionListener(listenner);
		language_english.addActionListener(listenner);
		language_french.addActionListener(listenner);
		language_spain.addActionListener(listenner);

		// set the position of all the button
		// AbstractPanel.setUnder(language_lable, userNameTextFiled, 20);
		AbstractPanel.setUnder(language_english, languagelbl, 5);
		language_english.setLocation(
				this.getWidth() / 2 - language_english.getWidth(),
				language_english.getLocation().y);
		AbstractPanel.setLeft(language_chinese, language_english, 10);
		AbstractPanel.setUnder(language_french, language_english, 5);
		AbstractPanel.setLeft(language_spain, language_french, 10);

		
		// add option setting for color blind
		colorBlind = AbstractPanel.getStyledCheckBox(nls.colorBlind,
				Config.IS_COLOR_BLIND_MODE);

		colorBlind.setMnemonic(KeyEvent.VK_G);
		colorBlind.setSelected(Config.IS_COLOR_BLIND_MODE);
		this.add(colorBlind);
		AbstractPanel.setUnder(colorBlind, language_french, 20);
		colorBlind.setLocation(getWidth() / 2 - colorBlind.getWidth() / 2,
				colorBlind.getLocation().y);
		colorBlind.addItemListener(this);

		saveButton = AbstractPanel.getStyledFunctionButton(nls.saveBtn);
		saveButton.addActionListener(listenner);
		this.add(saveButton);

		saveButton.setLocation(getWidth() - saveButton.getWidth() - 20,
				getHeight() - saveButton.getHeight() - 20);

	}

	/**
	 * paint the panel with a shadow background
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Image background = ImageLoader.loadImage(Config.LOOBY_BACKGROUND);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Config.MENU_FILL_COLOR);
		g.fillRoundRect(saveButton.getLocation().x - 20, 0,
				saveButton.getWidth() + 40, 400, 10, 10);
		g.drawRoundRect(saveButton.getLocation().x - 20, 0,
				saveButton.getWidth() + 40 - 2, 400 - 2, 10, 10);
	}

	/**
	 * add action listener for multi-language 
	 */
	ActionListener listenner = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource().equals(saveButton)) {
				frame.setContentPane(new MainMenuPanel(frame));
			}else if (e.getSource().equals(language_chinese)){
				Config.NLS_NAME = Config.NLS_NAME_ZH;
			}else if (e.getSource().equals(language_english)){
				Config.NLS_NAME = Config.NLS_NAME_EN;
			}else if (e.getSource().equals(language_french)){
				Config.NLS_NAME = Config.NLS_NAME_FR;
			}else if (e.getSource().equals(language_spain)){
				Config.NLS_NAME = Config.NLS_NAME_ES;
			}
		}
	};

	//add listener for color blind mode setting
	@Override
	public void itemStateChanged(ItemEvent e) {

		Object source = e.getItemSelectable();

		if (source == colorBlind) {

			if (colorBlind.isSelected()) {
				ImageIcon imageIcon = ImageLoader
						.getImageIcon("res/images/panel/checkbox_selected.png");
				colorBlind.setIcon(imageIcon);
				Config.IS_COLOR_BLIND_MODE = true;

			} else {

				ImageIcon imageIcon = ImageLoader
						.getImageIcon("res/images/panel/checkbox_unselected.png");
				colorBlind.setIcon(imageIcon);
				Config.IS_COLOR_BLIND_MODE = false;

			}
		}
		

	}

}
