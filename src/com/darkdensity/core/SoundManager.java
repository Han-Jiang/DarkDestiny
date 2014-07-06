package com.darkdensity.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.darkdensity.sound.Sound;
import com.darkdensity.sound.SoundPlayer;


public class SoundManager{
	
	ExecutorService threadPool;
	
	public SoundManager() {
		// TODO Auto-generated constructor stub
		threadPool = Executors.newFixedThreadPool(4);
	}
	
	public static void main(String[] args){
		SoundManager soundManager = new SoundManager();
//		soundManager.play("res/media/background.wav");
		
		
		SoundPlayer background = soundManager.getSoundPlayer("res/music/victory.wav");
		background.play();
		
//		SoundPlayer soundPlayer = soundManager.getSoundPlayer("res/music/bg.wav");
//		soundPlayer.play();
//		
//		SoundPlayer background = soundManager.getSoundPlayer("res/music/underwater-delusion.wav");
//		background.play();
//		
		
		
		
		soundManager.threadPool.shutdown();
		
	}
	
	
//	public void play(SoundPlayer soundPlayer){
//		threadPool.execute(soundPlayer);
//	}
//	
//	public void play(String fileName){
//		Sound sound = new Sound(fileName);
//		threadPool.execute(new SoundPlayer(sound,threadPool));
//	}
	
	public SoundPlayer getSoundPlayer(String fileName){
		Sound sound = new Sound(fileName);
		return new SoundPlayer(sound,threadPool);
	}
	
	public SoundPlayer getSoundPlayer(Sound sound){
		return new SoundPlayer(sound,threadPool);
	}
	
	public ExecutorService getThreadPool(){
		return threadPool;
	}



}
