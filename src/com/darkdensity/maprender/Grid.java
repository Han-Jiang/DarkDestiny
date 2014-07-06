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
	
	public Grid(){
		blocked = false;
	}

	public Boolean isBlocked() {
		return blocked;
	}

	public void block() {
		this.blocked = true;
	}
	
	public void release() {
		this.blocked = false;
	}
}
