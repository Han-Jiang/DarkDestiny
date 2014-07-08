package com.darkdensity.net.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.darkdensity.core.CommandPool;
import com.darkdensity.core.GameWorld;
import com.darkdensity.factory.CommandFactory;
import com.darkdensity.setting.Cheat;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.GameMode;
import com.darkdensity.setting.NLS;

public class ChatPanel extends JPanel implements KeyListener {
	/**
	 * 
	 * @ClassName: ChatPanel
	 * @Description: Chat panel provices a ui for player send & receive text
	 *               message
	 * @author Team A1 - Hei Yin Wong
	 * @date Mar 28, 2014 3:27:21 AM
	 */
	private static final long serialVersionUID = 6003306592117278779L;
	// TODO: change the udp address, and other variable into config.java
	private NLS nls;
	private final Color fillColor = new Color(0, 0, 0, 70);
	BufferedImage buffer = null;
	private ChatManager cm;
	private JTextField tf;
	private JTextArea tv;
	private int x = 0;
	private int y = 0;
	private final int width = 400;
	private final int height = 230;
	private Runnable receiveMessageRunnable;
	private Thread receiveMessageThread;
	private boolean isReceiveMessage;
	private Cheat cheat;
	private CommandPool commandPool;
	private CommandFactory commandFactory;

	public ChatPanel() {
		//Init chat panel with ui component 
		this.cm = new ChatManager();
		cheat = new Cheat();
		this.setBackground(fillColor);
		try {
			this.nls = (NLS) Class.forName(Config.NLS_NAME).newInstance();
		} catch (Throwable e) {
			// this.nls = new NLS();
		}
		tv = new JTextArea(10, 30);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		tv.setBorder(border);
		tv.setBackground(Color.WHITE);
		tv.setEditable(false);
		tv.setFocusable(false);
		this.add(tv);
		JLabel jl = new JLabel(nls.chatlbl);
		this.add(jl);
		tf = new JTextField(20);
		this.add(tf);
		JButton btnSend = new JButton("Send");

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatPanel.this.sendMessage();
			}

		});

		this.add(btnSend);
		this.setSize(this.width, this.height);

		this.tf.addKeyListener(this);

	}

	protected void sendMessage() {
		// Send out the message
		String message = tf.getText();
		try {
			if (!message.isEmpty()) {
				// If the input message is a cheat, trigger cheat and return
				System.out.println("Cheat size: " + cheat.size());
				String cheatCommandName = cheat.get(message);
				if (cheatCommandName != null && commandPool != null
						&& Config.IS_CHEAT_ALLOWED) {
					commandFactory.setPlayerRole(Config.PLAYER_ROLE);
					commandFactory.createCommand(cheatCommandName);
				} else {
					cm.sendMessage(message);
				}
				tf.setText("");
				if (GameWorld.getGameMode() == GameMode.SOLO) {
					this.setVisible(false);
				}
				SwingUtilities.getRoot(this).requestFocusInWindow();
			}
		} catch (Throwable t) {
			if (Config.DEBUGMODE) {
				t.printStackTrace();
			}
		}

	}

	public void receiveMessage() {
		// Show the message when message is received
		while (this.isReceiveMessage) {
			try {
				String s = cm.receiveMessage();
				tv.append(s + "\n"); // TODO: Change to dynamic user name after
										// implementing mult-player game
			} catch (Exception e) {
			}
		}
	}

	public void startReceiveMessage() {
		// Start receive message thread
		this.isReceiveMessage = true;
		this.receiveMessageRunnable = new Runnable() {
			public void run() {
				receiveMessage();
			};
		};
		this.receiveMessageThread = new Thread(this.receiveMessageRunnable);

		this.receiveMessageThread.start();
	}

	public void stopReceiveMessage() {
		// Stop receiving message thread
		try {
			this.isReceiveMessage = false;
			this.cm.closeSocket();
			this.receiveMessageThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			if (Config.DEBUGMODE) {
				e.printStackTrace();
			}
		}
	}

	public void focusTf() {
		this.tf.requestFocusInWindow();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				this.sendMessage();
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				SwingUtilities.getRoot(this).dispatchEvent(e);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Do nothing , but do not consume key event
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing , but do not consume key event
	}

	public void setCommandPool(GameWorld gameWorld) {
		this.commandPool = gameWorld.getCommandPool();
		this.commandFactory = gameWorld.getCommandFactory();
		cm.setCommandPool(gameWorld);
	}
}
