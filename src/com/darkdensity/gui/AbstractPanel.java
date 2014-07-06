package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.NLS;
import com.darkdensity.util.ImageLoader;

/* ****************************************
 * Class: Abstractpanel
 * ****************************************
 * Attributes:
 * 	protected NLS nls;
 protected final Color borderColor = new Color(255, 0, 0, 100);
 protected final Color fillColor = new Color(0, 0, 0, 70);
 protected final Color textColor = new Color(76, 196, 40);
 protected final Color titleColor = new Color(164, 180, 248);
 protected final Font font = new Font("Tahoma", Font.BOLD, 12);
 protected Image background = ImageLoader
 .loadImage("res/images/lobby/gameLobby.jpg");
 protected JFrame frame;

 * ****************************************
 * Methods:
 public Abstractpanel(JFrame frame);
 protected void initButton(final JButton button, ActionListener listenner);
 public abstract void paintComponent(Graphics g);
 public void update(long elapsedTime) ;
 public void reset() ;
 * */

/**
 * 
* @ClassName: AbstractPanel
* @Description: TODO(What the class do)
* @author Team A1
* @date 19 Mar 2014 13:52:00
 */
public abstract class AbstractPanel extends JPanel {
	// Mutileple language support
	protected NLS nls;
	protected final Color borderColor = new Color(255, 0, 0, 100);
	protected final Color fillColor = new Color(0, 0, 0, 70);
	protected final Color textColor = new Color(76, 196, 40);
	protected final Color titleColor = new Color(164, 180, 248);
	protected final Font font = new Font("Tahoma", Font.BOLD, 12);
	protected Image background = ImageLoader.loadImage(Config.LOOBY_BACKGROUND);
	
	// the parent JFrame
	protected JFrame frame;

	/** 
	 * 
	* <p>Title: </p> Abstract panel
	* <p>Description: </p>
	* @param frame
	 */
	public AbstractPanel(JFrame frame) {
		try {
			this.nls = (NLS) Class.forName(Config.NLS_NAME).newInstance();
		} catch (Throwable e) {
			this.nls = new NLS();
		}
		this.frame = frame;
		setLayout(null);
		setSize(frame.getWidth(), frame.getHeight());
	}

	/**
	 * 
	* @Title: initButton 
	* @Description: initial button style of all the JPanle
	 */
	protected void initButton(final JButton button, ActionListener listenner) {
		button.setBorder(null);
		button.setForeground(Color.WHITE);
		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.addActionListener(listenner);
	}

	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}

	public void update(long elapsedTime) {
	};

	public void reset() {
	};

	
	public static JButton getStyledButton(String text){

		ImageIcon imageIcon = ImageLoader.getImageIcon(Config.IMAGE_BUTTON_PATH);
		JButton button = new JButton(imageIcon);
		button.setText(text);
		
		button.setSize(106, 29);
		button.setBorder(null);
		button.setForeground(Color.WHITE);
		//made the text in the center
		button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		
		return button;
	}
	
	public static JButton getStyledFunctionButton(String text){

		ImageIcon imageIcon = ImageLoader.getImageIcon(Config.GAME_UI_FUNCTION_BUTTON_DARK);
		JButton button = new JButton(imageIcon);
		button.setText(text);
		
		button.setSize(196, 53);
		button.setBorder(null);
		button.setForeground(Color.WHITE);
		//made the text in the center
		button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		
		return button;
	}
	
	
	public static JLabel getStyledLable(String text){
		
		ImageIcon imageIcon = ImageLoader.getImageIcon(Config.GAME_UI_LABLE);
		JLabel lable = new JLabel(imageIcon);
		lable.setText(text);
		
		lable.setSize(216, 29);
		lable.setBorder(null);
		lable.setForeground(Color.WHITE);
		//made the text in the center
		lable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

		lable.setIgnoreRepaint(true);
		lable.setFocusable(false);
		lable.setBorder(null);
		
		return lable;
	}
	
	
	public static JRadioButtonMenuItem getStyledRadioButton(String text){

		ImageIcon imageIcon = ImageLoader.getImageIcon(Config.IMAGE_BUTTON_PATH);
		JRadioButtonMenuItem button = new JRadioButtonMenuItem(imageIcon);
		button.setText(text);
		
		button.setSize(136, 29);
		button.setBorder(null);
		button.setForeground(Color.WHITE);
		button.setOpaque(false);
		//made the text in the center
		button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		
		return button;
	}
	
	
	public static JCheckBox getStyledCheckBox(String text,boolean isSelected){
		
		ImageIcon imageIcon; 
		
		if(isSelected){
			imageIcon = ImageLoader.getImageIcon(Config.GAME_UI_CHECK_BOX_SELECTED);
		}else{
			imageIcon = ImageLoader.getImageIcon(Config.GAME_UI_CHECK_BOX_UNSELECTED);
		}
		JCheckBox button = new JCheckBox(imageIcon);
		button.setText(text);

		button.setSize(142, 36);
		button.setBorder(null);
		button.setForeground(Color.WHITE);
		//made the text in the center
		button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		
		return button;
		
	}
	
	public static void setBottomRight(JComponent a, JComponent b,int border){
		a.setLocation(b.getWidth()-a.getWidth()-border,b.getHeight()-a.getHeight()-border);
	}
	public static void setTopRight(JComponent a, JComponent b,int border){
		a.setLocation(b.getWidth()-a.getWidth()-border,border);
	}
	
	public static void setUnder(JComponent a, JComponent b,int border){
		a.setLocation(b.getLocation().x,b.getLocation().y+b.getHeight()+border);
	}
	
	public static void setOnTop(JComponent a, JComponent b,int border){
		a.setLocation(b.getLocation().x,b.getLocation().y-a.getHeight()-border);
	}
	
	public static void setLeft(JComponent a, JComponent b,int border){
		a.setLocation(b.getLocation().x+b.getWidth()+border,b.getLocation().y);
	}
	
}
