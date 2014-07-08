package com.darkdensity.gui;

/**
 * @ClassName: MainMenuMenuItem
 * @Description: TODO(What the class do)
 * @author Team A1 - Hei Yin Wong
 * @date 24 Mar 2014 23:01:48
 */



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

class MainMenuMenuItem extends JPanel {

	protected final Color fillColor = new Color(0, 0, 0, 200);
		//multiple language support
		private NLS nls;
		private JButton solo;
		private JButton coop;
		private JButton versus;
		private JButton options;
		private JButton exit;

		//main menu for the game
		public MainMenuMenuItem() {
			
			//load mutilple language setting
			try {
				this.nls =  (NLS) Class.forName(Config.NLS_NAME).newInstance();
			} catch (Throwable e) {
				if(Config.DEBUGMODE){e.printStackTrace();}
			}
			this.setLayout(new GridLayout(5, 1));
			setSize(250, 400);
			this.setOpaque(false);
			
			
			//get all the styled button
			solo = AbstractPanel.getStyledFunctionButton(nls.soloBtn);
			coop = AbstractPanel.getStyledFunctionButton(nls.coopBtn);
			versus = AbstractPanel.getStyledFunctionButton(nls.vsBtn);
			options = AbstractPanel.getStyledFunctionButton(nls.optionBtn);
			exit = AbstractPanel.getStyledFunctionButton(nls.exitBtn);
			
			//initial each button
			this.initButton(solo);
			this.initButton(coop);
			this.initButton(versus);
			this.initButton(options);
			this.initButton(exit);
			
		}

		/**
		 * 
		* @Title: initButton 
		* @Description: add listener to the button and add it to the panel
		* @param @param button
		* @return void    
		* @throws
		 */
		private void initButton(JButton button) {
			button.addActionListener(listenner);
			this.add(button);
		}
		
		/**
		 * paint the component
		 */
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(fillColor);
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
			g.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 10, 10);			
		}
		
		/**
		 * action performed when each item is clicked
		 */
		ActionListener listenner = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton jb = (JButton) e.getSource();
				
				JFrame jFrame = (JFrame) getRootPane().getParent();
				
				if (jb.equals(solo)) {
					// enter the solo panel 
					jFrame.setContentPane(new SoloPanel(jFrame));
					
				} else if (jb.equals(coop)) {
					// enter the solo panel
					jFrame.setContentPane(new SoloPanel(jFrame));
					
				} else if (jb.equals(versus)) {
					
					// enter the versus panel
					jFrame.setContentPane(new VersusPanel(jFrame));
					
				} else if (jb.equals(options)) {
					
					jFrame.setContentPane(new OptionPanel(jFrame));
					
				}else if (jb.equals(exit)) {
					System.exit(0);
				}
			}
		};
	}