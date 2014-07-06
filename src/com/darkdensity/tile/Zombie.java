package com.darkdensity.tile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import sun.nio.cs.ext.TIS_620;

import com.darkdensity.core.GameWorld;
import com.darkdensity.core.GridMapManager;
import com.darkdensity.core.TileManager;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant.PlayerRole;
import com.darkdensity.sound.Sound;

public abstract class Zombie extends Sprite {

	long currentTime = 0;
	long updateUnitTime = 1000;

	public Zombie() throws IOException {
		super();
		this.role = PlayerRole.ZOMBIE;
		this.reveal = 700;
		this.sound = new Sound("res/music/creepy-low-voice.wav");
		this.sound_die_path = "res/music/man-dying.wav";
	}

	// @Override
	// public void progressUpdate(int n) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	//
	// // public Zombie(GameWorld gameWorld, int x, int y) throws IOException {
	// // super(gameWorld, x, y);
	// // initAnimation(Config.SPRITE_PATH + "zombie1/");
	// // // currentAnimation.play();
	// // STEP_VELOCITY = 1;
	// // }
	//
	//
	// @Override
	// public void update(long elapsedTime) throws IOException {
	// super.update(elapsedTime);
	// //if(Player.getPlayer()==Player.Player_Human){
	// if(GameWorld.getGameMode() == Config.GAME_MODE_SOLO){
	// currentTime += elapsedTime;
	// if(currentTime > updateUnitTime){
	// //System.out.println("renew");
	// currentTime -= updateUnitTime;
	// int rx,ry;
	// Random r = new Random();
	// rx = r.nextInt(2)==1?-16*5:+16*5;
	// ry = r.nextInt(2)==1?-16*5:+16*5;
	// int newX = x+rx;
	// int newY = y+ry;
	// //System.out.println("random x y" + rx + ry);
	// ArrayList<Tile> nearSpritesList =
	// GridMapManager.gridMap.getGrids(GridMapManager.pxToTileX(x),
	// GridMapManager.pxToTileY(y));
	// //if(!nearSpritesList.isEmpty())
	// //System.out.println("near sprites list not empty");
	// Sprite nearestSprite =
	// TileManager.getNearestSurroundingHuman(this,nearSpritesList);
	// setDestination(newX, newY);
	// if(nearestSprite != null){
	// //System.out.println("human near zombie!");
	// setDestination(nearestSprite.getX(),nearestSprite.getY());
	// setTargetTile(nearestSprite);
	// }else{
	// setDestination(newX, newY);
	// }
	// }
	// //System.out.println("zombie destination("+dx + " , " + dy+")");
	// super.toNextPoint();
	// }
	// else{ // player is zombie side
	// super.update(elapsedTime);
	// }
	// }

	@Override
	public void update(long elapsedTime) throws IOException {
		super.update(elapsedTime);

		currentTime += elapsedTime;
		if (currentTime > updateUnitTime) {
			currentTime -= updateUnitTime;
			if (!Config.IS_NETWORK_MODE) {
				for (Survivor survivor : tileManager.getSurivors()) {
					if (survivor.isDestroyed()) {
						continue;
					}
					setDestination(survivor.getX() + survivor.getTileWidth()
							/ 2, survivor.getY() + survivor.getTileHeight() / 2);
					setTargetTile(survivor);
					break;
				}
			} else {
				Sprite s = tileManager.getNearestHuman(this);
				if (s != null) {
					// System.out.println("X: "+ s.getX() + " Y: "+ s.getY());
					setDestination(s.getX() + s.getTileWidth() / 2, s.getY()
							+ s.getTileHeight() / 2);
					setTargetTile(s);
				}
			}
		}

		super.toNextPoint();
		// } else { // player is zombie side
		// super.update(elapsedTime);
		// }
	}
	// // @Override
	// // public void mouseClicked(MouseEvent e) {
	// // // super.mouseClicked(e);
	// // System.out.print("Mouse clicked detected in Zombie");
	// //
	// // if(Player.getPlayer() == Player.Player_Zombie)
	// // tileManager.getGameWorld().getFocusManager().focus(this);
	// //
	// // // the requirement for winning is all the human is dead
	// // if (e.getButton() == 3) {
	// // LinkedList<Tile> sprites = tileManager.getGameWorld()
	// // .getFocusManager().getfocusingSprites();
	// // if (!sprites.isEmpty()) {
	// // this.die();
	// // //System.out.print("Kill zombie");
	// // }
	// // e.consume();
	// // }
	// // }
	// // @Override
	// // public void setDestination(int x,int y){
	// // if(GridMapManager.gridMap.getGrid(GridMapManager.pxToTileX(x),
	// GridMapManager.pxToTileY(y))==null)
	// // return;
	// // nextTilePoint = new
	// Point(GridMapManager.pxToTileX(x),GridMapManager.pxToTileX(y));
	// // // System.out.println("next tile"+nextTilePoint);
	// //
	// // }
	// @Override
	// public void setDestination(int dx,int dy) throws IOException{
	// if(Config.PLAYER_ROLE == PlayerRole.ZOMBIE){
	// int dtileX = GridMapManager.pxToTileX(dx);
	// int dtileY = GridMapManager.pxToTileY(dy);
	// if(GridMapManager.gridMap.getGrid(dtileX, dtileY)==null){
	// return;
	// }
	// if(!GridMapManager.gridMap.blockInBetween(new Point(dtileX,dtileY), new
	// Point(x/16,y/16), 2))
	// nextPoint = new Point(dx,dy);
	// else{
	// super.setDestination(dx, dy);
	// }
	// //System.out.println("next tile"+nextTilePoint);
	// }
	// else{
	// super.setDestination(dx, dy);
	// }
	//
	// }
}
