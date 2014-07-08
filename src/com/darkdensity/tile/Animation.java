package com.darkdensity.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.Direction;

/**
 * 
* @ClassName: Animation
* @Description: Animation class provide animation for the sprite
* @author Team A1
* @date Mar 28, 2014 6:45:02 AM
 */
public class Animation {
	private BufferedImage[] frames; // store all the frames (128 in total)
	private BufferedImage currFrame; // current frame
	private volatile boolean running = false;
	private long previousTime, speed;
	private Direction direction;
	private int startFrame, currentFrame;
	private int rows, cols;
	private Boolean highlight;
	private int highlightOffset;

	public Animation(String filename, String highlightFilename, int rows, int cols) {
		this.direction = Direction.SOUTH;
		this.rows = rows;
		this.cols = cols;
		this.highlightOffset = rows * cols;
		this.currentFrame = this.startFrame = 0;
		this.speed = Config.SKIP_TICKS;
		this.highlight = false;
		try {
			this.frames = Constant.getFrames(filename, highlightFilename, rows, cols);
		} catch (IOException e) {
			if(Config.DEBUGMODE){e.printStackTrace();}
		}
	}
	/**
	 * 
	* @Title: getCurrFrame 
	* @Description: get the current frame
	* @param @return
	* @return BufferedImage    
	* @throws
	 */
	public BufferedImage getCurrFrame() {
		currFrame = frames[((highlight)? highlightOffset: 0) + currentFrame];
		return currFrame;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}
	
	/**
	 * 
	* @Title: update 
	* @Description: update the animation with time
	* @param @param time
	* @return void    
	* @throws
	 */
	public void update(long time) {
		if (running && (time - previousTime) >= speed) {
			currentFrame = (++currentFrame) % cols + startFrame;
			previousTime = time;
		}
	}

	/**
	 * 
	* @Title: play 
	* @Description: paly the animation
	* @param 
	* @return void    
	* @throws
	 */
	public void play() {// initial
		running = true;
		previousTime = 0;
	}

	public void stop() {
		running = false;
		previousTime = 0;
		currentFrame = direction.ordinal() * cols;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		startFrame = direction.ordinal() * cols;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	/**
	 * 
	* @Title: focus 
	* @Description: set the animation to focus 
	* @param 
	* @return void    
	* @throws
	 */
	public void focus(){
		this.highlight = true;
	}
	
	
	/**
	 * 
	* @Title: blur 
	* @Description: unfocus the animation
	* @param 
	* @return void    
	* @throws
	 */
	public void blur(){
		this.highlight = false;
	}
}
