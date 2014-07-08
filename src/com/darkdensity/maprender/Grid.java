package com.darkdensity.maprender;

import java.util.LinkedList;

import com.darkdensity.tile.Tile;

/**
 * 
* @ClassName: Grid
* @Description: A gird is a linked list of tiles
* @author Team A1
* @date 19 Mar 2014 15:51:11
 */
public class Grid extends LinkedList<Tile>{
	private Boolean blocked;
	
	/**
	 * construct a new grid 
	 */
	public Grid(){
		blocked = false;
	}
	/**
	 * see whether the grid 
	 */
	public Boolean isBlocked() {
		return blocked;
	}

	/**
	 * block the grid
	 */
	public void block() {
		this.blocked = true;
	}
	
	/**
	 * release the grid
	 */
	public void release() {
		this.blocked = false;
	}
}
