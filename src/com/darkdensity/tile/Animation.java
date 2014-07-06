package com.darkdensity.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.Direction;

/* ****************************************
 * Class: Animation
 * ****************************************
 * Attributes:
 * 	private BufferedImage[] frames; //store all the frames (128 in total)
 public BufferedImage sprite; //current frame
 public boolean highlighted = false;
 private volatile boolean running = false;
 private long previousTime, speed;
 private int frameAtPause, currentFrame;
 private int direction;
 private int startFrame, endFrame;
 public static final int FACE_NORTH = 1;
 public static final int FACE_WEST = 2;
 public static final int FACE_SOUTH = 3;
 public static final int FACE_EAST = 4;
 public static final int FACE_NORTH_WEST = 5;
 public static final int FACE_SOUTH_WEST = 6;
 public static final int FACE_SOUTH_EAST = 7;
 public static final int FACE_NORTH_EAST = 8;
 * ****************************************
 * Methods:
 * public int getCurrentFrame(); // get current frame of the animation 
 * public void createFrames(String filename) throws IOException; // load the images for the frames 
 * public void setSpeed(long speed);// set the speed
 * public void update(long time);
 * public void play();
 * public void stop();
 * public void pause();
 * public void resume();
 * public void setDirection(int direction); // set the direction, and change the begin image and end image of the animation
 * void setHighLight();
 * void resetHighLight();
 * public boolean isRunning();
 * public void setRunning(boolean running); // set the state 
 * public void setStandHighLight();
 * public void resetStandHighLight(); 
 * */

public class Animation {
	private BufferedImage[] frames; // store all the frames (128 in total)
	private BufferedImage currFrame; // current frame
//	public boolean highlighted = false;
	private volatile boolean running = false;
	private long previousTime, speed;
	private Direction direction;
	private int startFrame, currentFrame;
	private int rows, cols;
	private Boolean highlight;
	private int highlightOffset;
	//private int noOfDirections;

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

	public BufferedImage getCurrFrame() {
		currFrame = frames[((highlight)? highlightOffset: 0) + currentFrame];
		return currFrame;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public void update(long time) {
		if (running && (time - previousTime) >= speed) {
			currentFrame = (++currentFrame) % cols + startFrame;
			previousTime = time;
		}
//		currFrame = frames[currentFrame];
	}

	public void play() {// initial
		running = true;
		previousTime = 0;
	}

	public void stop() {
		running = false;
		previousTime = 0;
		currentFrame = direction.ordinal() * cols;
//		currFrame = frames[currentFrame];
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
	
	public void focus(){
		this.highlight = true;
	}
	
	public void blur(){
		this.highlight = false;
	}
}
