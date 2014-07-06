package com.darkdensity.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.NLS;

class MenuItem extends JPanel {

	protected final Color fillColor = new Color(0, 0, 0, 70);
		//multiple language support
		private NLS nls;
		private JButton solo;
		private JButton coop;
		private JButton versus;
		private JButton options;
		private JButton exit;

		// Elastic elastic = new Elastic(0, 100);
		//main menu for the game
		public MenuItem() {
			
			//load mutilple language setting
			try {
				this.nls =  (NLS) Class.forName(Config.NLS_NAME).newInstance();
			} catch (Throwable e) {
				if(Config.DEBUGMODE){e.printStackTrace();}
			}
			this.setLayout(new GridLayout(5, 1));
			setSize(200, 400);
			this.setOpaque(false);

			// y = (parent.getHeight() - getHeight()) / 2;
			

			//System.out.print("Config:" + Config.NLS_NAME);
			//System.out.print(this.nls.soloBtn);
			ImageIcon imageIcon = new ImageIcon("res/images/panel/button.jpg");
//			button.setIcon(imageIcon);
			solo = new JButton(imageIcon);
			solo.setText(nls.soloBtn);
			coop = new JButton(nls.coopBtn,imageIcon);
			versus = new JButton(nls.vsBtn,imageIcon);
			options = new JButton(nls.optionBtn,imageIcon);
			exit = new JButton(nls.exitBtn,imageIcon);
			//initial each button
			this.initButton(solo);
			this.initButton(coop);
			this.initButton(versus);
			this.initButton(options);
			this.initButton(exit);
			
		}

		private void initButton(JButton button) {
			button.setSize(106, 29);
			button.setBorder(null);
			button.setForeground(Color.WHITE);
			//made the text in the center
			button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

			button.setIgnoreRepaint(true);
			button.setFocusable(false);
			button.setBorder(null);
			button.setContentAreaFilled(false);
			button.addActionListener(listenner);
			this.add(button);

		}
////		@Override
//		public void paint(Graphics g){
//			System.out.println("I was called, paint");
//			super.paint(g);
//			
////			paintComponent(g);
//		}

		public void paintComponent(Graphics g) {
//			System.out.println("I was called, paintComponent");
			super.paintComponent(g);
//			paintComponents(g);
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
				
				if (jb.equals(solo)) {
					System.out.println("1");
					
				} else if (jb.equals(coop)) {
					System.out.println("coop");
					
				} else if (jb.equals(versus)) {
					System.out.println("versus");
					
				} else if (jb.equals(options)) {
					System.out.println("options");
					
				}else if (jb.equals(exit)) {
					System.exit(0);
				}
			}
		};
	}