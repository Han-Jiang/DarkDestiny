package com.darkdensity.path;

import java.awt.Point;

import com.darkdensity.core.GridMapManager;
import com.darkdensity.maprender.GridMap;
import com.darkdensity.setting.Constant.Direction;

public class SimpleAStarNode {
	SimpleAStarNode parent;
    int nodeDepth;
    int estimatedValue;
    Point posPoint;
	Direction direction = Direction.SOUTH;
    
	public SimpleAStarNode(Point point) {
		this.posPoint = point;
		this.parent = null;
		this.nodeDepth = 0;
		this.estimatedValue = 0;
		getDirection();
	}
	
	/**ASatarNode constructor
	 * @param AStarNode - for copy node
	 */
	public SimpleAStarNode(SimpleAStarNode aNode) {
		this.nodeDepth = aNode.nodeDepth;
		this.parent = aNode.parent; // !!!
		this.estimatedValue = aNode.estimatedValue;
		this.posPoint = aNode.posPoint;
		getDirection();
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

	public void setParent(SimpleAStarNode parentNode){
		this.parent = parentNode;
	}

	public SimpleAStarNode getParent() {
		return parent;
	}
	
	public void addDepth(){
		nodeDepth++;
	}
	
	public Direction getDirection(){
		if(parent == null )
			return Direction.SOUTH;
		int distanceX = posPoint.x - parent.posPoint.x;
		int distanceY = posPoint.y - parent.posPoint.y;

		
		// walk a step according to the direction
		if (distanceX == 0 && distanceY >= 0) {
			direction = Direction.SOUTH;
			return direction;
		}
		if (distanceX == 0 && distanceY < 0) {
			direction = Direction.NORTH;
			return direction;
		}
		double tanDest = distanceY / distanceX;
		// tangent value;
		// -22.5 ~ 22.5 -> -0.41 ~ 0.41 east
		// 22.5 ~ 67.5 -> 0.41 ~ 2.41 south east
		// 67.5 ~ 112.5 -> ~-2.41 && 2.41 ~ south
		// 112.5 ~ 157.5 -> -2.41 ~ -0.41 south west

		// 157.5 ~ 202.5 -> -0.41 ~ 0.41 west
		// 202.5 ~ 247.5 -> 0.41 ~ 2.41 north west
		// 247.5 ~ 292.5 -> && ~-2.41 && 2.41 ~ north
		// 292.5 ~ 337.5 -> -2.41 ~ -0.41 north east

		if (distanceX > 0) {
			if (tanDest > -0.41 && tanDest <= 0.41)
				direction = Direction.EAST;
			else if (tanDest > 0.41 && tanDest <= 2.41)
				direction = Direction.SOUTH_EAST;
			else if (tanDest > 2.41)
				direction = Direction.SOUTH;
			else if (tanDest < -0.41 && tanDest >= -2.41)
				direction = Direction.NORTH_EAST;
			else
				direction = Direction.NORTH;
		} else {
			if (tanDest > -0.41 && tanDest <= 0.41)
				direction = Direction.WEST;
			else if (tanDest > 0.41 && tanDest <= 2.41)
				direction = Direction.NORTH_WEST;
			else if (tanDest > 2.41)
				direction = Direction.NORTH;
			else if (tanDest < -0.41 && tanDest >= -2.41)
				direction = Direction.SOUTH_WEST;
			else
				direction = Direction.SOUTH;
		}
		return direction;
	}


}
