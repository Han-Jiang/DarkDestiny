package com.darkdensity.test;


import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TestMenu {
	
	
	public static void main(String[] args) {
				
		//Initialize frame
		JFrame frame = new JFrame();
		frame.setSize(600,600);
		frame.setBackground(Color.white);
		frame.setForeground(Color.white);
		frame.setTitle("HEllO");
		frame.setLayout(null);
//		((JComponent) frame.getContentPane()).setOpaque(false);
		
		JPanel menuPanel = new MenuItem();
		menuPanel.setLocation(frame.getWidth() - menuPanel.getWidth(), 0);
		frame.add(menuPanel);
		
		JPanel panel = new JPanel();
		panel.setSize(500, 500);
		panel.setLocation(25, 25);
		panel.setBackground(Color.yellow);
		frame.add(panel);

//		frame.setContentPane(panel);
		
		frame.setVisible(true);
				
	}

}
