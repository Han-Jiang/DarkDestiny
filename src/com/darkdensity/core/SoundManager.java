package com.darkdensity.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.darkdensity.sound.Sound;
import com.darkdensity.sound.SoundPlayer;


public class SoundManager{
	
	ExecutorService threadPool;
	
	/**
	 * 
	* <p>Title: </p>  SoundManager
	* <p>Description: </p> new a sound manger with executor service
	 */
	public SoundManager() {
		// TODO Auto-generated constructor stub
		threadPool = Executors.newFixedThreadPool(4);
	}
	/**
	 * 
	* @Title: main 
	* @Description: sound player test
	* @param @param args
	* @return void    
	* @throws
	 */
	public static void main(String[] args){
		SoundManager soundManager = new SoundManager();
		
		SoundPlayer background = soundManager.getSoundPlayer("res/music/victory.wav");
		background.play();
		
		SoundPlayer soundPlayer = soundManager.getSoundPlayer("res/music/bg.wav");
		soundPlayer.play();
	
		soundManager.threadPool.shutdown();
		
	}
	
	/**
	 * 
	* @Title: getSoundPlayer 
	* @Description: get sound player instance with a sound file path
	* @param @param fileName
	* @param @return
	* @return SoundPlayer    
	* @throws
	 */
	public SoundPlayer getSoundPlayer(String fileName){
		Sound sound = new Sound(fileName);
		return new SoundPlayer(sound,threadPool);
	}
	
	/**
	 * 
	* @Title: getSoundPlayer 
	* @Description:  get sound player instance with a sound 
	* @param @param sound
	* @param @return
	* @return SoundPlayer    
	* @throws
	 */
	public SoundPlayer getSoundPlayer(Sound sound){
		return new SoundPlayer(sound,threadPool);
	}
	
	/**
	 * 
	* @Title: getThreadPool 
	* @Description: return the sound's executor service
	* @param @return
	* @return ExecutorService    
	* @throws
	 */
	public ExecutorService getThreadPool(){
		return threadPool;
	}



}
