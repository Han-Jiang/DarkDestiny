package com.darkdensity.path;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import com.darkdensity.core.GameWorld;
import com.darkdensity.core.GridMapManager;
import com.darkdensity.setting.Constant;
import com.darkdensity.setting.Constant.Direction;
import com.darkdensity.util.Pair;

/**
* @ClassName: SimpleAStarSearch
* @Description: Advanced Astar Search that uses to find the order of crosses
* @author Team A1 - Yingjing Feng
*/

public class SimpleAStarSearch {
	Point beginPoint;
	Point endPoint;
	SimpleAStarNode beginNode;
	SimpleAStarNode endNode;
	SimpleAStarNode currentNode;
	private Vector<SimpleAStarNode> openList;
	private Vector<SimpleAStarNode> closeList;
	private ArrayList<Point> solutionPath;
	private Hashtable<Point, HashSet<Point>> crossesTable;
	private boolean isCompleted;
	private int searchedNodesNum;
	private int searchMode = 1;
	private Direction direction;
	SubPathManager subPathManager= GameWorld.getSubPathManager();

	// set the search mode: 1 - search for the neighbours in 4 isometric
	// directions
	// 2 - search for the neighbours in 4 directions
	// 3 - search for the neighbours in 8 directions

	public SimpleAStarSearch(Point beginPoint, Point endPoint)
			throws FileNotFoundException, IOException {
		this.beginPoint = beginPoint;
		this.endPoint = endPoint;

		Point bNearPoint = findNearestPoint(beginPoint, Constant.BEGIN_POINT);
		Point eNearPoint = findNearestPoint(endPoint, Constant.END_POINT);
		
		if( bNearPoint==null ||eNearPoint == null){
			isCompleted = true;
			this.solutionPath = null;
			return;
		}
		this.beginNode = new SimpleAStarNode(bNearPoint);
		this.endNode = new SimpleAStarNode(eNearPoint);
		
		this.currentNode = beginNode;
		this.openList = new Vector<SimpleAStarNode>();
		this.closeList = new Vector<SimpleAStarNode>();
		this.solutionPath = null;
		this.isCompleted = false;
		this.searchedNodesNum = 0;
		

		// jsonReading.
		crossesTable =  Constant.ASTAR_NODES;
		direction = getDirection(beginPoint, endPoint);

	}


	/** 
	* @Title: Search 
	* @Description: Astar Search
	* @param @return
	* @param @throws IOException
	* @return boolean    
	* @throws 
	*/ 
	
	public boolean Search() throws IOException {
		
		if(isCompleted){
			return false;
		}
		
		// write the procdure of searching into ASearchDialog.txt

		String filePath = "ASearchDialog.txt";
		PrintWriter pw = new PrintWriter(new FileWriter(filePath));

		// If the nodes to visited is more than 15000, then set it to fail
		int maxNodesNum = 100;

		// to store the neighbouring nodes of the current node
		Vector<SimpleAStarNode> followNodes = new Vector<SimpleAStarNode>();

		// set the isCompleted flag default to false
		isCompleted = false;

		// (1)put the first node into the openList
		this.sortedInsertOpenList(this.beginNode);

		// (2)if the open list is empty, or the number of the searched nodes is
		// bigger than maxNodesNum,
		// then it fails
		while (this.openList.isEmpty() != true
				&& searchedNodesNum <= maxNodesNum) {

			// if the tilePoint attribute of current node is the destination
			// then set the isCompleted value true, exit the loop
			this.currentNode = this.openList.elementAt(0);
			if (currentNode.getPos().x == endNode.getPos().x
					&& currentNode.getPos().y == endNode.getPos().y) {

				isCompleted = true;

				this.calSolutionPath();

				break;
			}


			this.openList.removeElementAt(0);
			this.closeList.addElement(this.currentNode);
			searchedNodesNum++;


			followNodes = this.findFollowaNodes(this.currentNode);
			// System.out.print("currentNode"+currentNode.getTile()+"follow"+followNodes.size());
			while (!followNodes.isEmpty()) {
				this.sortedInsertOpenList((SimpleAStarNode) followNodes.elementAt(0));
				followNodes.removeElementAt(0);
			}


		}

		return isCompleted;
	}

	// calculate the solutionList by adding the currentNode to the solutionPath
	// and setting the parent of the currentNode as the currentNode repeatedly
	// until the parent of the currentNode is null.
	private boolean calSolutionPath() {

		if (!this.isCompleted()) {
			return false;
		} else {
			SimpleAStarNode aNode = this.currentNode;
			solutionPath = new ArrayList<Point>();
			int i = 0;
			while (aNode.parent != null) {
				solutionPath.add(0, aNode.getPos());
				aNode = aNode.getParent();
			}
			solutionPath.add(0, aNode.getPos());
			return true;
		}

	}

	// return the solution path
	public ArrayList<Point> getSolutionPath() {
		return solutionPath;
	}

	// get the string of the solution path
	public String getSolutionPathString() {
		if (solutionPath == null)
			return "no solution!";
		String str = new String();
		str += "solucation path length" + solutionPath.size();
		str += " Begin->";
		if (this.isCompleted) {
			// for (int i = solutionPath.size() - 1; i >= 1; i--) {
			for (int i = 0; i <= solutionPath.size() - 1; i++) {
				str += (solutionPath.get(i)).toString() + "->";
			}
			str += "End";
		} else
			str = "Jigsaw Not Completed.";
		return str;
	}

	// return the isCompleted flag
	private boolean isCompleted() {
		return isCompleted;
	}

	// print the result to the file
	public void printResult(PrintWriter pw) throws IOException {
		boolean flag = false;
		if (pw == null) {
			pw = new PrintWriter(new FileWriter("Result.txt"));
			flag = true;
		}

		// close the file
		if (flag)
			pw.close();

	}

	private Vector<SimpleAStarNode> findFollowaNodes(SimpleAStarNode aNode) {
		Vector<SimpleAStarNode> followNodes = new Vector<SimpleAStarNode>();
		HashSet<Point> neighbourPoints = crossesTable.get(aNode.getPos());
		for (Point point : neighbourPoints) {

			SimpleAStarNode tempNode = new SimpleAStarNode(point);
			if (!elem(closeList, tempNode)) {
				tempNode.setParent(aNode);
				followNodes.add(tempNode);
			}
		}
		return followNodes;
	}

	boolean elem(Vector<SimpleAStarNode> vector, SimpleAStarNode node) {
		for (Object n : vector) {
			if (((SimpleAStarNode) n).getPos().x == node.getPos().x
					&& ((SimpleAStarNode) n).getPos().y == node.getPos().y)
				return true;
		}

		return false;
	}

	// set the current node as their parent node
	// sort them according to the estimated value (from small to big) and append
	// them to the openList.
	private void sortedInsertOpenList(SimpleAStarNode aNode) {

		this.estimateValue(aNode);
		for (int i = 0; i < this.openList.size(); i++) {
			if (aNode.getEstimatedValue() <= ((SimpleAStarNode) this.openList
					.elementAt(i)).getEstimatedValue()) {
				this.openList.insertElementAt(aNode, i);
				return;
			}
		}
		this.openList.addElement(aNode);
	}

	private void estimateValue(SimpleAStarNode aNode) {
		int s1 = f1(aNode);
		int s2 = f2(aNode);
		int s3 = f3(aNode);
		int s4 = f4(aNode);
		aNode.setEstimatedValue(s1 + s2 == 0 ? 0 : (s1 + s3 + s4) + s3 + s4); // the
																				// cost
																				// of
																				// changing
																				// direction
																				// is
																				// the
																				// most
																				// expensive
	}

	/** 
	* @Title: f1 
	* @Description: Manhattan distance
	* @param @param aNode
	* @param @return
	* @return int    
	* @throws 
	*/ 
	
	private int f1(SimpleAStarNode aNode) { 

		int distance = (int) (aNode.posPoint.distance(beginNode.posPoint));
		int distance2 = (int) (aNode.posPoint.distance(endNode.posPoint));
		return (distance + distance2);

	}

	/** 
	* @Title: f2 
	* @Description: cost to change direction
	* @param @param aNode
	* @param @return
	* @return int    
	* @throws 
	*/ 
	
	private int f2(SimpleAStarNode aNode) { 
		if (aNode.getDirection() == direction)
			return 0;

		return 5;

	}

	/** 
	* @Title: f3 
	* @Description: step cost
	* @param @param aNode
	* @param @return
	* @return int    
	* @throws 
	*/ 
	
	private int f3(SimpleAStarNode aNode) { 

		return aNode.getNodeDepth();

	}

	/** 
	* @Title: f4 
	* @Description: step from parent
	* @param @param aNode
	* @param @return
	* @return int    
	* @throws 
	*/ 
	
	private int f4(SimpleAStarNode aNode) { 
		if (aNode.getParent() == null)
			return 0;
		return (int) (aNode.getPos()).distance(aNode.getParent().posPoint);

	}

	public Vector<SimpleAStarNode> getCloseList() {
		return closeList;
	}

	public Point getBeginPoint() {
		return beginPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setMode(int i) {
		searchMode = i;

	}

	public Direction getDirection(Point bPoint, Point ePoint) {

		int distanceX = ePoint.x - bPoint.x;
		int distanceY = ePoint.y - bPoint.y;
//		// walk a step according to the direction
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

	// for finding the starting point and the end point of the AStar search
	public Point findNearestPoint(Point point, int startPtOrEndPt) {
		Hashtable<Point, HashSet<Point>> tmp = Constant.ASTAR_NODES;
		Point nearestPoint = null;
		/**select a building which dx, dy is unwalkable**/
		if (GridMapManager.gridMap.getGrid(point.x / 16, point.y / 16) == null) {
			Enumeration<Point> keys = Constant.ASTAR_NODES.keys();
			double nearestDis = 100000;
			while (keys.hasMoreElements()) {
				Point key = keys.nextElement();
				if (nearestDis > key.distance(point) && couldBePoint(key,startPtOrEndPt)) {
					nearestDis = key.distance(point);
					nearestPoint = key;
				}
			}
			return nearestPoint;
		} else {
		/**it is walkable**/
			Enumeration<Point> keys = Constant.ASTAR_NODES.keys();
			double nearestDis = 100000;

			while (keys.hasMoreElements()) {
				Point key = keys.nextElement();
				Pair<Point, Point> pair = subPathManager.findPair(point);
				if (nearestDis > key.distance(point)
						&& couldBePoint(key,startPtOrEndPt)
						&& 
						(!GridMapManager.gridMap.blockInBetween(point, key, (int)point.distance(key))
								||
						(pair!=null && subPathManager.getPath(pair).isBlocked==false))){
					
					nearestDis = key.distance(point);
					nearestPoint = key;
				}
			}
			
			if(nearestPoint!=null)
				return nearestPoint;
			else{ // find the point behind
				Enumeration<Point> keys3 =  Constant.ASTAR_NODES.keys();
				double nearestDis3 = 100000;

				while (keys.hasMoreElements()) {
					Point key3 = keys3.nextElement();
					Pair<Point, Point> pair = subPathManager.findPair(point);
					if (nearestDis > key3.distance(point) &&
							(!GridMapManager.gridMap.blockInBetween(point, key3, (int)point.distance(key3))
									||
							(pair!=null && subPathManager.getPath(pair).isBlocked==false))){
						nearestDis = key3.distance(point);
						nearestPoint = key3;
					}
				}
				return null;
			
			}
		}
	}

	/**between the destination and beginning**/
	public boolean couldBePoint(Point point , int startPtOrEndPt) {
		double disB = point.distance(getBeginPoint());
		double disC = point.distance(getEndPoint());
		double disA = getBeginPoint().distance(getEndPoint());
		double cosA = 0;
		if(startPtOrEndPt == Constant.BEGIN_POINT)
			cosA = (disB * disB + disA * disA - disC * disC)/ (2 * disA * disB); // cosA = (b^2+a^2-c^2)/2ab
		else 
			cosA = (disA * disA + disC * disC - disB * disB)/ (2 * disA * disC); // cosA = (a^2+c^2-b^2)/2ac
		if (cosA>0){ // most be obtuse angle
			return true;
		}
		return false;
	}

}
