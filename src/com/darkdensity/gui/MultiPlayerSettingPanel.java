package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.darkdensity.core.NetworkManager;
import com.darkdensity.net.chat.ChatPanel;
import com.darkdensity.net.chat.VoiceChatManager;
import com.darkdensity.net.lobby.SwapTeamRequest;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.PlayerRole;

/**
 * 
 * @ClassName: MultiPlayerSettingPanel
 * @Description: Game lobby page for multiplayer
 * @author Team A1 - Hei Yin Wong
 * @date 19 Mar 2014 15:37:40
 */
public class MultiPlayerSettingPanel extends AbstractPanel {

	private JButton startGameBtn, backBtn;
	private NetworkManager networkManager;
	private ArrayList<JLabel> playerLabelList;
	private ChatPanel chatPanel;
	private JLabel serverIPLbl;
	private JButton sendVoiceMessageBtn;
	private SettingPanel settingPanel; //The small setting panel in the right hand side
	private ArrayList<JButton> teamButtonList;

	/**
	 * 
	* <p>Title: </p>
	* <p>Description: </p> construct a new multi player mode 
	* @param parent
	* @param networkManager
	 */
	public MultiPlayerSettingPanel(JFrame parent, NetworkManager networkManager) {
		super(parent);
		this.networkManager = networkManager;
		setLayout(null);

		serverIPLbl = new JLabel();
		serverIPLbl.setLocation(30, 20);
		serverIPLbl.setSize(150, 20);
		serverIPLbl.setText("Server IP:" + networkManager.getServerAddress());
		this.initLabel(serverIPLbl);

		JLabel playerListlbl = new JLabel("Player");
		playerListlbl.setLocation(30, 100);
		playerListlbl.setSize(100, 20);
		this.initLabel(playerListlbl);
		this.playerLabelList = new ArrayList<JLabel>();
		this.teamButtonList = new ArrayList<JButton>();

		//labels show the palyer's infomation
		for (int i = 1; i < 5; i++) {
			JLabel playerLabel = new JLabel("Waiting for connect");
			initLabel(playerLabel);
			playerLabel.setLocation(30, 100 + 20 * i);
			playerLabel.setSize(200, 20);
			this.playerLabelList.add(playerLabel);

			JButton teamButton = new JButton();
			teamButton.setLocation(200, 100 + 20 * i);
			teamButton.setSize(150, 20);
			teamButton.setText(nls.teambtn_human);
			this.initButton(teamButton);
			this.teamButtonList.add(teamButton);
		}

		//start the game 
		if (networkManager.getServerSocket() != null) {
			startGameBtn = new JButton("Start Game");
			initButton(startGameBtn);
			startGameBtn.setSize(100, 20);
			startGameBtn.setLocation(
					parent.getWidth() - startGameBtn.getWidth(), 20);
		}
		
		// chat panel
		chatPanel = new ChatPanel();
		chatPanel.setLocation(10, this.getHeight() - chatPanel.getHeight());
		add(chatPanel);
		chatPanel.startReceiveMessage();

		// send voice message 
		sendVoiceMessageBtn = new JButton("Send voice message");
		initButton(sendVoiceMessageBtn);
		sendVoiceMessageBtn.setSize(100, 20);
		sendVoiceMessageBtn.setLocation(chatPanel.getWidth(),
				this.getHeight() - 40);

		//setting panel for color biling, full reveal and so om
		settingPanel = new SettingPanel(networkManager);
		this.add(settingPanel);
		settingPanel
				.setLocation(this.getWidth() - settingPanel.getWidth(), 100);

	}

	/**
	 * @Title: setVCM
	 * @Description: set the voice chat manager
	 * @param VoiceChatManager
	 *            vcm
	 * @return void
	 * @throws
	 */
	public void setVCM(VoiceChatManager vcm) {
		sendVoiceMessageBtn.addMouseListener(new VoiceMessageButtonHandler(
				this.networkManager.getVoiceChatManager()));
	}

	private void initLabel(JLabel jLabel) {
		jLabel.setIgnoreRepaint(true);
		jLabel.setForeground(Color.WHITE);
		this.add(jLabel);
	}

	protected void initButton(JButton button) {
		super.initButton(button, listenner);
		this.add(button);

	}

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

	public void updatePlayerList() {
		System.out.println("Update player list");
		ArrayList<String> playerList = this.networkManager.getPlayerList();
		for (int i = 0; i < playerList.size(); i++) {
			try {
				this.playerLabelList.get(i).setText(playerList.get(i));
			} catch (Exception e) {
				// TODO to handle more than 5 player
			}
		}
	}

	ActionListener listenner = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JButton jb = (JButton) e.getSource();
			JFrame jFrame = (JFrame) MultiPlayerSettingPanel.this.getRootPane()
					.getParent();
			if (jb.equals(startGameBtn)) {
				try {
					networkManager
							.startGame(jFrame, settingPanel.getGameTime());
					chatPanel.stopReceiveMessage();

				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (jb.equals(backBtn)) {
				jFrame.setContentPane(new MainMenuPanel(jFrame));
			} else {
				for (int i = 0; i < teamButtonList.size(); i++) {
					JButton teamButton = teamButtonList.get(i);
					if (playerLabelList.get(i).getText()
							.equals(Config.PLAYER_NAME)) {
						SwapTeamRequest swapTeamRequest;
						if (teamButton.getText().equals(nls.teambtn_human)) {
							teamButton.setText(nls.teambtn_zombie);
							Config.PLAYER_ROLE = PlayerRole.ZOMBIE;
							swapTeamRequest = new SwapTeamRequest(
									Config.PLAYER_NAME, PlayerRole.ZOMBIE);
						} else {
							teamButton.setText(nls.teambtn_zombie);
							teamButton.setText(nls.teambtn_human);
							Config.PLAYER_ROLE = PlayerRole.SURVIVOR;
							swapTeamRequest = new SwapTeamRequest(
									Config.PLAYER_NAME, PlayerRole.SURVIVOR);
						}

						teamButton.setBorder(null);
						teamButton.setBorder(null);
						teamButton.setContentAreaFilled(false);
						
						networkManager.getGameLobby().sendRequest(swapTeamRequest);
						
						System.out.println("Player: "
								+ swapTeamRequest.getPlayerName());
						System.out.println("To team: "
								+ swapTeamRequest.getTeam());

					}
				}
			}
		}
	};

	public void stopChatPanel() {
		this.chatPanel.stopReceiveMessage();
	}

	private class VoiceMessageButtonHandler implements MouseListener {

		private VoiceChatManager vcm;

		public VoiceMessageButtonHandler(VoiceChatManager vcm) {
			this.vcm = vcm;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			vcm.captureAudio();

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			vcm.stopCapture();
			vcm.sendVoiceMessage();
		}

	}

	public void swapTeam(SwapTeamRequest request) {
		// TODO Auto-generated method stub
		for (int i = 0; i < playerLabelList.size(); i++) {
			if (playerLabelList.get(i).getText()
					.equals(request.getPlayerName())) {
				JButton teamButton = teamButtonList.get(i);
				if (request.getTeam().equals(PlayerRole.SURVIVOR)) {
					teamButton.setText(nls.teambtn_human);
				} else {
					teamButtonList.get(i).setText(nls.teambtn_zombie);
				}
				teamButton.setBorder(null);
				teamButton.setBorder(null);
				teamButton.setContentAreaFilled(false);
				break;
			}
		}
	}

	public void showError() {
		// TODO Auto-generated method stub
		this.startGameBtn.setEnabled(false);
		
	}
	public void noError() {
		// 
		this.startGameBtn.setEnabled(true);
	}

	public SettingPanel getSettingPanel() {
		return settingPanel;
	}

	
	

}
