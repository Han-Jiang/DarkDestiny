package com.darkdensity.player;

import com.darkdensity.core.TileManager;
import com.darkdensity.setting.Constant.PlayerRole;

/**
 * 
* @ClassName: Team
* @Description: A team have resource and player role
* @author Team A1
* @date 19 Mar 2014 16:14:11
 */
public class Team {
	protected PlayerRole playerRole;
	protected int food, wood, iron, foodConsume, numOfSprite;
		
	protected TileManager tileManager;
	
	public Team(int food, int wood,
			int iron, PlayerRole playerRole) {
		super();
//		resource = new Resource(food, iron, wood, 0);
		this.food = food;
		this.wood = wood;
		this.iron = iron;
		this.playerRole = playerRole;
		this.foodConsume = foodConsume;
	}
	
	/**
	 * 
	* @Title: getNumOfSprite 
	* @Description: get the number of sprite
	* @param @return
	* @return int    
	* @throws
	 */
	public int getNumOfSprite(){
		return (playerRole == PlayerRole.SURVIVOR)? tileManager.getSurvivorsNum(): tileManager.getZombiesNum();
	}
	
	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}
	
	public void addFood(int food){
		this.food += food;
	}
	
	public void minusFood(int food){
		this.food -= food;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}
	
	public void addWood(int wood){
		this.wood += wood;
	}
	
	public void minusWood(int wood){
		this.wood -= wood;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}
	
	public void addIron(int iron){
		this.iron += iron;
	}
	
	public void minusIron(int iron){
		this.iron -= iron;
	}

	public int getFoodConsume() {
		return foodConsume;
	}

	public void setFoodConsume(int foodConsume) {
		this.foodConsume = foodConsume;
	}

	public void setTileManager(TileManager tileManager) {
		this.tileManager = tileManager;
	}

	public PlayerRole getPlayerRole() {
		return playerRole;
	}

	public void setPlayerRole(PlayerRole playerRole) {
		this.playerRole = playerRole;
	}
	
	
}
