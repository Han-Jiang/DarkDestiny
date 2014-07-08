package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.darkdensity.core.NetworkManager;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.NLS;

/**
 * 
* @ClassName: VersusPanel
* @Description: Versus Panel enable the user to start the game in "Versus" mode
* @author Team A1
* @date Mar 28, 2014 2:22:08 AM
 */
public class VersusPanel extends AbstractPanel {
	private JButton startBtn, backBtn, createServerBtn, connectBtn;
	private NetworkManager networkManager;
	private JFrame parentFrame;
	private JTextField playername;
	private JTextField iptxt;
	
	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> construct a new versus panel
	* @param parent
	 */
	public VersusPanel(JFrame parent) {
		super(parent);
		
		this.parentFrame = parent;
		setLayout(null);
		
		//create a server
		createServerBtn = getStyledButton(nls.createGamebtn);
		createServerBtn.setLocation(getWidth() / 2 - createServerBtn.getWidth()
				/ 2, getHeight() / 2);
		createServerBtn.addActionListener(listenner);
		this.add(createServerBtn);
		
	
		//palyer name
		JLabel playernamelbl = new JLabel(nls.playernamelbl);
		playernamelbl.setSize(100, 20);
		playernamelbl.setLocation(createServerBtn.getLocation().x, getHeight() / 2
				- createServerBtn.getHeight() - 10);
		this.initLabel(playernamelbl);
		
		playername = new JTextField();
		playername.setVisible(true);
		playername.setSize(150, 20);
		playername.setLocation(playernamelbl.getLocation().x + playernamelbl.getWidth(), getHeight()/2  - createServerBtn.getHeight() -10);
		this.add(playername);

		//option for ip
		JLabel iplbl = new JLabel(nls.iplbl);
		iplbl.setSize(20, 20);
		iplbl.setLocation(createServerBtn.getLocation().x, getHeight() / 2
				+ createServerBtn.getHeight() + 20);
		this.initLabel(iplbl);

		iptxt = new JTextField();
		iptxt.setVisible(true);
		iptxt.setSize(150, 20);
		iptxt.setLocation(iplbl.getLocation().x + iplbl.getWidth() + 20,
				iplbl.getLocation().y);
		this.add(iptxt);

		
		//connect to the server
		connectBtn = getStyledButton(nls.connectbtn);
		connectBtn.setLocation(createServerBtn.getLocation().x,
				iplbl.getLocation().y + iplbl.getHeight() + 20);
		this.initButton(connectBtn);

	}

	/**
	 * 
	* @Title: initLabel 
	* @Description: initial a label  
	* @param @param jLabel
	* @return void    
	* @throws
	 */
	private void initLabel(JLabel jLabel) {
		jLabel.setForeground(Color.WHITE);
		jLabel.setIgnoreRepaint(true);
		jLabel.setVisible(true);
		this.add(jLabel);
	}

	/**
	 * 
	* @Title: initButton 
	* @Description: initial a button
	* @param @param button
	* @return void    
	* @throws
	 */
	private void initButton(JButton button) {
		super.initButton(button, listenner);
		this.add(button);

	}

	/**
	 * paint the panel
	 */
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

	
	/**
	 * add listener to all the button and labels
	 */
	ActionListener listenner = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton) e.getSource();
			JFrame jFrame = (JFrame) VersusPanel.this.parentFrame;
			if(playername.getText().isEmpty()){
				return;
			}else{
				Config.PLAYER_NAME = playername.getText();
			}
			if (jb.equals(backBtn)) {
				jFrame.setContentPane(new MainMenuPanel(jFrame));
			} else if (jb.equals(createServerBtn)) {
				try {
					networkManager = new NetworkManager();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (jb.equals(connectBtn)) {
				System.out.println("Connect to: " + iptxt.getText());
				if (!iptxt.getText().equals("")) {
					try {
						networkManager = new NetworkManager(iptxt.getText());
					} catch (Throwable e1) {
						e1.printStackTrace();
					}

				}
			}

		}
	};

}
