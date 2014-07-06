package com.darkdensity.tile;

import java.io.IOException;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.sound.Sound;

public abstract class Survivor extends Sprite {
	public Survivor() throws IOException {
		super();
		this.role = PlayerRole.SURVIVOR;
		this.reveal = 800;
		this.sound = new Sound("res/music/yes sir.wav");
		this.sound_die_path = "res/music/man-dying.wav";
	}

//	public Survivor(GameWorld gameWorld, int x, int y)
//			throws IOException {
//		super(gameWorld, x, y);
//		initAnimation(Config.SPRITE_PATH + "human1/");
//		//currentAnimation.play();
//		STEP_VELOCITY = 2;
//	}


//	@Override
//	public void progressUpdate(int n) {
//		if(!isDead()){
//			this.health -= n;
//			if(health < 0){
//				health = 0;
//				this.die();
//			}
//		}
//	}

//	@Override
//	public void setDestination(int dx, int dy) throws IOException {
//		// TODO Auto-generated method stub
//		System.out.println("set destination in Survivor");
//		super.setDestination(dx, dy);
//		//System.out.println("searching ")
//	}
	
}
