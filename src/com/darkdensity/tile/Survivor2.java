package com.darkdensity.tile;

import java.io.IOException;

import com.darkdensity.setting.Config;
import com.darkdensity.sound.Sound;

public class Survivor2 extends Survivor {

	public Survivor2() throws IOException {
		super();
		initAnimation(Config.SPRITE_PATH + "human2/",  Config.PLAYER_ROLE ==  role);
		this.speed = STEP_VELOCITY = Config.SURVIVOR2_SPEED;
		this.maxHealth = this.health = Config.SURVIVOR2_MAX_HEALTH;
		this.attack = Config.SURVIVOR2_ATTACK;
		this.defense = 40;
		this.reveal = Config.SURVIVOR2_REVEAL;
//		this.sound = new Sound("res/music/ready_to_go.wav");
	}
	
//	public Survivor2(GameWorld gameWorld, int x, int y)
//			throws IOException {
//		super(gameWorld, x, y);
//		initAnimation(Config.SPRITE_PATH + "human2/");
//		//currentAnimation.play();
//		STEP_VELOCITY = 2;
//	}
}
