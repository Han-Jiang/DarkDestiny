package com.darkdensity.path;

import java.awt.Point;

import com.darkdensity.maprender.GridMap;
import com.darkdensity.setting.Constant.Direction;

public class AStarNode {
	AStarNode parent;
    int nodeDepth;
    int estimatedValue;
    Point posPoint;
    Point tilePoint;
    GridMap map;
	Direction direction = Direction.SOUTH;
	int interval = 16;
	int accuracy = 16/interval;
    
	public AStarNode(int posx, int posy) {
		this.posPoint = new Point(posx/interval, posy/interval);
		this.tilePoint = new Point(posPoint.x/accuracy, posPoint.y/accuracy);
		this.parent = null;
		this.nodeDepth = 0;
		this.estimatedValue = 0;
		
	}
    
	public void setMap(GridMap hashMap){
		this.map =hashMap;
	}
	
    
	/**ASatarNode constructor
	 * @param AStarNode - for copy node
	 */
	public AStarNode(AStarNode aNode) {
		this.nodeDepth = aNode.nodeDepth;
		this.parent = aNode.parent; // !!!
		this.estimatedValue = aNode.estimatedValue;
		this.posPoint = aNode.posPoint;
		this.tilePoint = aNode.tilePoint;
		this.map = aNode.map;
		this.accuracy = aNode.accuracy;
	}
	
	
	public int getEstimatedValue() {
		return estimatedValue;
	}
	
	public int setEstimatedValue(int estimatedValue) {
		return this.estimatedValue = estimatedValue;
	}


	public Point getPos(){
		return posPoint;
	}
	
	public int getNodeDepth(){
		return nodeDepth;
	}
	
	public boolean move(int direction) {
		this.parent = new AStarNode(this);
		switch (direction) {
		case 4:
			return this.moveEmptySouth();
		case 5:
			return this.moveEmptyWest();
		case 6:
			return this.moveEmptyNorth();
		case 7:
			return this.moveEmptyEast();
		case 0:
			return this.moveEmptySouthEast();
		case 1:
			return this.moveEmptySouthWest();
		case 2:
			return this.moveEmptyNorthEast();
		case 3:
			return this.moveEmptyNorthEast();
		default:
			return false;
		}
	}
	
	void changePosPoint(int vx, int vy){
		posPoint = new Point(posPoint.x+vx, posPoint.y+vy);
		tilePoint = new Point(posPoint.x/accuracy, posPoint.y/accuracy);
	}
	
	
	boolean moveEmptyEast() {
		if(map.getGrid((posPoint.x+1)/accuracy, (posPoint.y)/accuracy) != null && !map.getGrid((posPoint.x+1)/accuracy, (posPoint.y)/accuracy).isBlocked()){
			changePosPoint(1, 0);
			direction = Direction.EAST;
			return true;
		}
		return false;
	}


	boolean moveEmptyNorth() {
		if(map.getGrid((posPoint.x)/accuracy, (posPoint.y-1)/accuracy) != null && !map.getGrid((posPoint.x)/accuracy, (posPoint.y-1)/accuracy).isBlocked()){
			changePosPoint(0, -1);
			direction = Direction.NORTH;
			return true;
		}
		return false;
	}


	boolean moveEmptyWest() {
		if(map.getGrid((posPoint.x-1)/accuracy, (posPoint.y)/accuracy) != null && !map.getGrid((posPoint.x-1)/accuracy, (posPoint.y)/accuracy).isBlocked()){
			changePosPoint(-1, 0);
			direction = direction.WEST;
			return true;
		}
		return false;
	}


	boolean moveEmptySouth() {
		if(map.getGrid((posPoint.x)/accuracy, (posPoint.y+1)/accuracy) != null && !map.getGrid((posPoint.x)/accuracy, (posPoint.y+1)/accuracy).isBlocked()){
			changePosPoint(0, 1);
			direction = Direction.SOUTH;
			return true;
		}
		return false;
	}
	boolean moveEmptyNorthEast() {
		if(map.getGrid((posPoint.x+1)/accuracy, (posPoint.y-1)/accuracy) != null && !map.getGrid((posPoint.x+1)/accuracy, (posPoint.y-1)/accuracy).isBlocked()){
			changePosPoint(1, -1);
			direction = Direction.NORTH_EAST;
			return true;
		}
		return false;
	}


	boolean moveEmptySouthWest() {
		if(map.getGrid((posPoint.x-1)/accuracy, (posPoint.y+1)/accuracy) != null && !map.getGrid((posPoint.x-1)/accuracy, (posPoint.y+1)/accuracy).isBlocked()){
			changePosPoint(-1, 1);
			direction = Direction.SOUTH_WEST;
			return true;
		}
		return false;
	}


	boolean moveEmptyNorthWest() {
		if(map.getGrid((posPoint.x-1)/accuracy, (posPoint.y-1)/accuracy) != null && !map.getGrid((posPoint.x-1)/accuracy, (posPoint.y-1)/accuracy).isBlocked()){
			changePosPoint(-1, -1);
			direction = Direction.NORTH_WEST;
			return true;
		}
		return false;
	}


	boolean moveEmptySouthEast() {
		if(map.getGrid((posPoint.x+1)/accuracy, (posPoint.y+1)/accuracy) != null && !map.getGrid((posPoint.x+1)/accuracy, (posPoint.y+1)/accuracy).isBlocked()){
			changePosPoint(1, 1);
			direction = Direction.SOUTH_EAST;
			return true;
		}
		return false;
	}

	public AStarNode getParent() {
		return parent;
	}
	
	public void addDepth(){
		nodeDepth++;
	}
	
	public Direction getDirection(){
		return direction;
	}


}
