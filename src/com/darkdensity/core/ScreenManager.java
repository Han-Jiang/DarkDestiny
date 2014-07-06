package com.darkdensity.core;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import com.darkdensity.setting.Config;

public class ScreenManager {
	
/*
 * Class: ScreenManager
 * *****************************
 * Attributes: 
 * private GraphicsDevice device;
 * *****************************
 * Methods:
 * public DisplayMode getCurrentDisplayMode() {}
 * public void setFullScreen(DisplayMode displayMode) {}
 * public JFrame getFullScreenWindow() {}
 * */
	
	
	
	private GraphicsDevice device;
	


	public ScreenManager() {
		//Init ScreenManager by getting default screen devices info
		GraphicsEnvironment environment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
	}

	public DisplayMode getCurrentDisplayMode() {
		//Get the default display mode EG: 1280 X 800 for mac book pro
		return device.getDisplayMode();
	}

	
	public DisplayMode getFisrtSupportedDisplayMode(DisplayMode[] modes){
		DisplayMode supportedModes[] = device.getDisplayModes();
        for (int i = 0; i < modes.length; i++) {
            for (int j = 0; j < supportedModes.length; j++) {
                if (modes[i].equals(supportedModes[j])) {
                    return modes[i];
                }
            }
        }
        return null;
	}
	
	

	
    public DisplayMode[] getSupportedDisplayModes() {
        return device.getDisplayModes();
    }
    
    
	public void setFullScreen(DisplayMode displayMode) {
		//Create and init a full screen JFrame
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);

		device.setFullScreenWindow(frame);

		if (displayMode != null && device.isDisplayChangeSupported()) {
			try {
				device.setDisplayMode(displayMode);
			} catch (IllegalArgumentException ex) {

			}
			frame.setSize(displayMode.getWidth(), displayMode.getHeight());
			Config.PANEL_WIDTH = displayMode.getWidth();
			Config.PANEL_HEIGHT = displayMode.getHeight();
		}
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					//Create buffer strategy which can speed up image processing 
					if (frame.getBufferStrategy() == null) {
						frame.createBufferStrategy(2);
					}
				}
			};
			new Thread(r).start();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public JFrame getFullScreenWindow() {
		//Get a full screen JFrame 
		return (JFrame) device.getFullScreenWindow();
	}
}