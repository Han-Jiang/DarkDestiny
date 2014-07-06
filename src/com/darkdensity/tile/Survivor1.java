package com.darkdensity.tile;

import java.io.IOException;

import com.darkdensity.core.GameWorld;
import com.darkdensity.core.SoundManager;
import com.darkdensity.setting.Config;
import com.darkdensity.sound.Sound;

public class Survivor1 extends Survivor {
	public Survivor1() throws IOException {
		super();
		initAnimation(Config.SPRITE_PATH + "human1/", Config.PLAYER_ROLE == role);
		this.speed = STEP_VELOCITY = Config.SURVIVOR1_SPEED;
		this.maxHealth = this.health = Config.SURVIVOR1_MAX_HEALTH;
		this.attack = Config.SURVIVOR1_ATTACK;
		this.defense = 50;
		this.reveal = Config.SURVIVOR1_REVEAL;
		
	}

}
