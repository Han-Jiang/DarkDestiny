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
* @Description: AbstractPanel panel provide the common attribute for all the panel
*  and provide method return styled button, label and other ui 
* @author Team A1 - Tin Yuen Lam
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

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	/**
	 * 
	* @Title: update 
	* @Description:update the panel's data by elapased time
	* @param @param elapsedTime
	* @return void    
	* @throws
	 */
	public void update(long elapsedTime) {
	};

	/**
	 * 
	* @Title: reset 
	* @Description: reset the panel's some component to initial status
	* @param 
	* @return void    
	* @throws
	 */
	public void reset() {
	};

	
	/**
	 * 
	* @Title: getStyledButton 
	* @Description: return a styled button with image background
	* @param @param text
	* @param @return
	* @return JButton    
	* @throws
	 */
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
	
	/**
	 * 
	* @Title: getStyledFunctionButton 
	* @Description: return a styled button with big iamge backgound,
	* mainly used for the main menu of the game
	* @param @param text
	* @param @return
	* @return JButton    
	* @throws
	 */
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
	
	
	/**
	 * 
	* @Title: getStyledLable 
	* @Description: get a style label with iamge background
	* @param @param text
	* @param @return
	* @return JLabel    
	* @throws
	 */
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
	
	
	/**
	 * 
	* @Title: getStyledRadioButton 
	* @Description: get a styled radio button with image bcakground
	* @param @param text
	* @param @return
	* @return JRadioButtonMenuItem    
	* @throws
	 */
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
	
	/**
	 * 
	* @Title: getStyledCheckBox 
	* @Description: get a styled check box with image background
	* @param @param text
	* @param @param isSelected
	* @param @return
	* @return JCheckBox    
	* @throws
	 */
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
	
	/**
	 * 
	* @Title: setBottomRight 
	* @Description:  set the first component's position to the right bottom of
	* of the second component
	* @param @param a
	* @param @param b
	* @param @param border
	* @return void    
	* @throws
	 */
	public static void setBottomRight(JComponent a, JComponent b,int border){
		a.setLocation(b.getWidth()-a.getWidth()-border,b.getHeight()-a.getHeight()-border);
	}
	/**
	 * 
	* @Title: setTopRight 
	* @Description:  set the first component's position to the top right of
	* of the second component
	* @param @param a
	* @param @param b
	* @param @param border
	* @return void    
	* @throws
	 */
	public static void setTopRight(JComponent a, JComponent b,int border){
		a.setLocation(b.getWidth()-a.getWidth()-border,border);
	}
	
	/**
	 * 
	* @Title: setUnder 
	* @Description:  set the first component's position to the bottom
	*  of the second component
	* @param @param a
	* @param @param b
	* @param @param border
	* @return void    
	* @throws
	 */
	public static void setUnder(JComponent a, JComponent b,int border){
		a.setLocation(b.getLocation().x,b.getLocation().y+b.getHeight()+border);
	}
	
	/**
	 * 
	* @Title: setOnTop 
	* @Description:  set the first component's position to the top
	* of the second component
	* @param @param a
	* @param @param b
	* @param @param border
	* @return void    
	* @throws
	 */
	public static void setOnTop(JComponent a, JComponent b,int border){
		a.setLocation(b.getLocation().x,b.getLocation().y-a.getHeight()-border);
	}
	
	/**
	 * 
	* @Title: setLeft 
	* @Description:  set the first component's position to the left
	* of the second component
	* @param @param a
	* @param @param b
	* @param @param border
	* @return void    
	* @throws
	 */
	public static void setLeft(JComponent a, JComponent b,int border){
		a.setLocation(b.getLocation().x+b.getWidth()+border,b.getLocation().y);
	}
	
}
