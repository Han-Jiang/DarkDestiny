package com.darkdensity.tile;

import java.io.IOException;

import com.darkdensity.setting.Config;
import com.darkdensity.sound.Sound;

public class Zombie1 extends Zombie{
	public Zombie1() throws IOException {
		super();
		initAnimation(Config.SPRITE_PATH + "zombie1/",  Config.PLAYER_ROLE == role);
		this.speed = STEP_VELOCITY = Config.ZOMBIE1_SPEED;
		this.maxHealth = this.health = Config.ZOMBIE1_HEALTH;
		this.attack = Config.ZOMBIE1_ATTACK;
		this.defense = 40;
		this.reveal = Config.ZOMBIE1_REVEAL;
		this.sound = new Sound("res/music/deep-monster-roar-2.wav");
		}
}
