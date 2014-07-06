package com.darkdensity.player;

/**
 * 
* @ClassName: Resource
* @Description: resource　class handle with how much resources
*  the team can use
* @author Team A1
* @date 19 Mar 2014 16:11:02
 */
public class Resource {
	private int food, iron, wood, survivor;

	public Resource(int food, int iron, int wood, int survivor) {
		super();
		this.food = food;
		this.iron = iron;
		this.wood = wood;
		this.survivor = survivor;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getIron() {
		return iron;
	}

	public void setIron(int iron) {
		this.iron = iron;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getSurvivor() {
		return survivor;
	}

	public void setSurvivor(int survivor) {
		this.survivor = survivor;
	}
	
	public String toString(){
		return "Food: " + food + " , Wood: " + wood + " , Iron: " + iron + " , survivor: " + survivor;
	}
}
