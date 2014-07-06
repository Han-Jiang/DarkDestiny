package com.darkdensity.test;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Snippet {
	public static void main(String[] args) throws MalformedURLException {
	
//	        URL url = new URL("<URL to your Animated GIF>");
	        Icon icon = new ImageIcon("res/images/loadingBar.gif");
	        JLabel label = new JLabel(icon);
	
	        JFrame f = new JFrame("Animation");
	        f.getContentPane().add(label);
	        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        f.pack();
	        f.setLocationRelativeTo(null);
	        f.setVisible(true);
	    }
}

