package com.darkdensity.core;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.darkdensity.gui.MainMenuPanel;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.GameMode;

/**
 * 
 * @ClassName: GameCore
 * @Description: Game Core, define game logic and run, this one is for windows mode
 * @author TeamA1 - Han Jiang
 * 
 */
public class GameCoreST extends JFrame implements KeyListener {

	private static final int TIMER_DELAY = 10;

	public static BufferStrategy strategyST;
	public static long currentTimeST;
	public static long elapsedTimeST;

	public static boolean GAME_STATUS_IS_RUNNING = true;
	public static boolean IS_PAUSED = false;


	JFrame gameCoreFrame;
	
	GameWorld mGame = null;


	/**
	 * The entrance of the game
	 */
	public static void main(String[] args) {
		final GameCoreST mGame = new GameCoreST();

		strategyST = mGame.getBufferStrategy();
		currentTimeST = System.currentTimeMillis();
		elapsedTimeST = 0;

		Timer sTimer = new Timer(TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mGame.gameLoopForSwingTimer();
			}
		});
		sTimer.start();
	}

	public GameCoreST() {
		gameCoreFrame = this;
		// initialize JFrame
		setTitle("Dark Destiny");// set the title
		setSize(1024, 700);// set the size
		setFocusable(true);
		setResizable(false);// forbid resize
		setLayout(null);
		setLocationRelativeTo(null);// center
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		
		showMainPanel();

		setVisible(true);// show directly
		// create buffer strategy
		createBufferStrategy(2);
	}

	public void gameLoopForSwingTimer() {

		elapsedTimeST = System.currentTimeMillis() - currentTimeST;
		currentTimeST = System.currentTimeMillis();

		if (IS_PAUSED) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			updateData(elapsedTimeST);

			// get the graphic and redraw it
			Graphics2D g = (Graphics2D) strategyST.getDrawGraphics();
			
			redrawScreen(g);
			g.dispose();

			if (!strategyST.contentsLost()) {
				strategyST.show();
			}
		}

	}

	/**
	 * update data in the game
	 */
	public void updateData(long elapsedTime) {
		
		if(mGame!=null){
			try {
				mGame.update(elapsedTime);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				if(Config.DEBUGMODE){e.printStackTrace();}
			}
		}
		
	}


	/**
	 * redraw the screen
	 */
	private void redrawScreen(Graphics2D g) {
		 paintAll(g);
	}
	
	public void showMainPanel(){
		getContentPane().removeAll();
		MainMenuPanel mp = new MainMenuPanel(this);
		add(mp);
		mGame = null;
	}
	
	public void showGamePanel(){
		getContentPane().removeAll();
		// Read the node information
	
		try {
			mGame = new GameWorld(this, GameMode.SOLO);
		} catch (Throwable e) {
			if(Config.DEBUGMODE){e.printStackTrace();}
		}
		setContentPane(mGame);
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.out.print("Hello ");
		}
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			showMainPanel();
		}
		if (e.getKeyCode() == KeyEvent.VK_F2) {
			showGamePanel();
		}
		e.consume();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
