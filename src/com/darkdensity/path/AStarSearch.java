package com.darkdensity.path;

import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import com.darkdensity.core.GridMapManager;
import com.darkdensity.maprender.Grid;
import com.darkdensity.maprender.GridMap;
import com.darkdensity.setting.Constant.Direction;

/**
* @ClassName: AStarSearch
* @Description: Astar Search to search every grids from start point to destination
* @author Team A1 - Yingjing Feng
*/

public class AStarSearch {
	AStarNode beginNode; 
	AStarNode endNode; 
	AStarNode currentNode; 
	private Vector<AStarNode> openList; 
	private Vector<AStarNode> closeList; 
	private ArrayList<Point> solutionPath;
	private ArrayList<Point> reversedSolutionPath;
	private boolean isCompleted; 
	private int searchedNodesNum;
	private GridMap map;

	private int availableRange = 1;
	int maxNodesNum = 100;
	
	private int searchMode = 2;
	/**
	/*set the search mode: 1 - search for the neighbours in 4 isometric directions
	//					   2 - search for the neighbours in 4 directions
	//					   3 - search for the neighbours in 8 directions
	
	
	/** 
	* <p>Title: Constructor </p>
	* <p>Description: to construct an Astar Search </p>
	* @param bNode
	* @param eNode
	* @param maxNodesNum
	 */
	public AStarSearch(AStarNode bNode, AStarNode eNode, int maxNodesNum) {
		this.beginNode = new AStarNode(bNode);
		this.endNode = new AStarNode(eNode);
		this.currentNode = new AStarNode(bNode);
		this.openList = new Vector<AStarNode>();
		this.closeList = new Vector<AStarNode>();
		this.solutionPath = null;
		this.reversedSolutionPath = null;
		this.isCompleted = false;
		this.searchedNodesNum = 0;
		this.map = GridMapManager.gridMap;
		this.maxNodesNum = maxNodesNum;
		//System.out.println("map==null?"+(map ==null));
		bNode.setMap(this.map);
		eNode.setMap(this.map);
	}

	public void setMap(GridMap map) {
		this.map = map;
	}

	// Astar Search
	public boolean Search() throws IOException {
		// write the procdure of searching into ASearchDialog.txt
		
		String filePath = "ASearchDialog.txt";
		PrintWriter pw = new PrintWriter(new FileWriter(filePath));

		// If the nodes to visited is more than 15000, then set it to fail


		// to store the neighbouring nodes of the current node
		Vector<AStarNode> followNodes = new Vector<AStarNode>();

		// set the isCompleted flag default to false
		isCompleted = false;

		// (1)put the first node into the openList
		this.sortedInsertOpenList(this.beginNode);

		// (2)if the open list is empty, or the number of the searched nodes is bigger than maxNodesNum, 
		//	   then it fails
		while (this.openList.isEmpty() != true
				&& searchedNodesNum <= maxNodesNum) {

			// if the tilePoint attribute of current node is the destination
			// then set the isCompleted value true, exit the loop
			this.currentNode = this.openList.elementAt(0);
			if (currentNode.getPos().equals(endNode.getPos())) {

				isCompleted = true;
				// calculate the solutionList 
				this.calSolutionPath();
				
				break;
			}

 			// set the current node as their parent node
			// sort them according to the estimated value (from small to big) 
			// append them to the openList.
			
			this.openList.removeElementAt(0);
			this.closeList.addElement(this.currentNode);
			searchedNodesNum++;

			followNodes = this.findFollowaNodes(this.currentNode);
			//System.out.print("currentNode"+currentNode.getTile()+"follow"+followNodes.size());
			while (!followNodes.isEmpty()) {
				this.sortedInsertOpenList((AStarNode) followNodes.elementAt(0));
				followNodes.removeElementAt(0);
			}


		}

		return isCompleted;
	}

	//calculate the solutionList by adding the currentNode to the solutionPath
	// and setting the parent of the currentNode as the currentNode repeatedly
	// until the parent of the currentNode is null.
	private boolean calSolutionPath() {

		if (!this.isCompleted()) {
			return false;
		} else {
			AStarNode aNode = this.currentNode;
			solutionPath = new ArrayList<Point>();
			
			int i = 0;
			while (aNode.parent != null) {
				solutionPath.add(0, new Point(aNode.getPos().x*(16/aNode.accuracy),aNode.getPos().y*(16/aNode.accuracy)));
				aNode = aNode.getParent();
			}

			solutionPath.add(0, new Point(aNode.getPos().x*(16/aNode.accuracy),aNode.getPos().y*(16/aNode.accuracy)));
			smoothPath();
			smoothPath2(6);
			reversedSolutionPath = new ArrayList<Point>();
			for(Point point:solutionPath){
				reversedSolutionPath.add(0,point);
			}
			return true;
		}

	}

	// return the solution path
	public ArrayList<Point> getSolutionPath() {
		return solutionPath;
	}
	
	public ArrayList<Point> getReversedSolutionPath() {
		return reversedSolutionPath;
	}

	// get the string of the solution path
	public String getSolutionPathString() {
		String str = new String();
		str += "solucation path length" + solutionPath.size();
		str += " Begin->";
		if (this.isCompleted) {
			//for (int i = solutionPath.size() - 1; i >= 1; i--) {
			for(int i = 0; i <= solutionPath.size() - 1; i++ ){
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

	//print the result to the file
	public void printResult(PrintWriter pw) throws IOException {
		boolean flag = false;
		if (pw == null) {
			pw = new PrintWriter(new FileWriter("Result.txt"));
			flag = true;
		}
		if (flag)
			pw.close();

	}

	private Vector<AStarNode> findFollowaNodes(AStarNode aNode) {

		Vector<AStarNode> followNodes = new Vector<AStarNode>();
		AStarNode tempNode;
		if(searchMode == 1){
			for (int i = 0; i < 4; i++) {
				tempNode = new AStarNode(aNode);
				tempNode.addDepth();
				if (tempNode.move(i) && !elem(closeList, tempNode)
						&& !elem(openList, tempNode)) {
					followNodes.addElement(tempNode);
	
				}
			}
		}else if(searchMode == 2){
			for (int i = 4; i < 8; i++) {
				tempNode = new AStarNode(aNode);
				tempNode.addDepth();
				if (tempNode.move(i) && !elem(closeList, tempNode)
						&& !elem(openList, tempNode)) {
					followNodes.addElement(tempNode);
	
				}
			}
		}else{
			for (int i = 0; i < 8; i++) {
				tempNode = new AStarNode(aNode);
				tempNode.addDepth();
				if (tempNode.move(i) && !elem(closeList, tempNode)
						&& !elem(openList, tempNode)) {
					followNodes.addElement(tempNode);
	
				}
			}
		}
		return followNodes;
	}

	boolean elem(Vector<AStarNode> vector, AStarNode node) {
		for (Object n : vector) {
			if (((AStarNode) n).getPos().x == node.getPos().x
					&& ((AStarNode) n).getPos().y == node.getPos().y)
				return true;
		}

		return false;
	}

	
	//	set the current node as their parent node
	//  sort them according to the estimated value (from small to big) and append them to the openList.
	private void sortedInsertOpenList(AStarNode aNode) {
		
		this.estimateValue(aNode);
		for (int i = 0; i < this.openList.size(); i++) {
			if (aNode.getEstimatedValue() <= ((AStarNode) this.openList
					.elementAt(i)).getEstimatedValue()) {
				this.openList.insertElementAt(aNode, i);
				return;
			}
		}
		this.openList.addElement(aNode);
	}

	private void estimateValue(AStarNode aNode) {
		int s1 = f1(aNode);
		int s2 = f2(aNode);
		int s3 = f3(aNode);
		aNode.setEstimatedValue( s1 + s3); // the cost of changing direction is the most expensive
	}

	private int f1(AStarNode aNode) { // Manhattan distance
		Point tile = endNode.getPos();
		return (int)(aNode.getPos().distance(this.beginNode.getPos()) + aNode.getPos().distance(this.endNode.getPos()));

	}

	private int f2(AStarNode aNode) { // cost to change direction 
	    if(aNode.getParent()==null)
	    	return 0;
		Direction parentDirection = aNode.getParent().getDirection();
		if(aNode.getDirection() == parentDirection)
			return 0;
		
		return 5;

	}

	private int f3(AStarNode aNode) { // step cost

		return aNode.getNodeDepth();

	}

	public Vector<AStarNode> getCloseList() {
		return closeList;
	}

	public Point getBeginPoint() {
		return beginNode.getPos();
	}

	public Point getEndPoint() {
		return endNode.getPos();
	}

	public void setMode(int i) {
		searchMode = i;
		
	}
	
	public void smoothPath2(int sectorLength){
		if(solutionPath == null || solutionPath.size()==0)
			return;
		ArrayList<Point> smoothPath = new ArrayList<Point>();
		int sectorNum = solutionPath.size()/sectorLength;
		for(int i = 0; i < sectorNum; i++){
			int startCounter = i * sectorLength;
			int endCounter = startCounter + sectorLength - 1;
			int midCounter = startCounter + sectorLength/2 - 1;
			smoothPath.add(solutionPath.get(startCounter));
			Point startPoint = solutionPath.get(startCounter);
			Point endPoint = solutionPath.get(endCounter);
			if(map.blockInBetween(startPoint, endPoint, sectorLength)){
				
				if(!map.blockInBetween(solutionPath.get(startCounter), solutionPath.get(midCounter), sectorLength/2)){
					smoothPath.addAll(solutionPath.subList(startCounter,midCounter));
				}
				
				smoothPath.add(solutionPath.get(midCounter));
				
				if(!map.blockInBetween(solutionPath.get(midCounter+1), solutionPath.get(endCounter), sectorLength/2)){
					smoothPath.addAll(solutionPath.subList(midCounter,endCounter-1));
				}
			}	
			smoothPath.add(solutionPath.get(endCounter));	
		}
		
			int startCounter = sectorNum * sectorLength;
		if(startCounter <= (solutionPath.size()-1)){
			int remainLength = solutionPath.size() - 1 - startCounter;
			int endCounter = startCounter + remainLength;
			int midCounter = startCounter + remainLength/2 - 1;
			if(map.blockInBetween(solutionPath.get(startCounter), solutionPath.get(endCounter), sectorLength)){
				
				if(!map.blockInBetween(solutionPath.get(startCounter), solutionPath.get(midCounter), sectorLength/2)){
					smoothPath.addAll(solutionPath.subList(startCounter,midCounter));
				}
				
				smoothPath.add(solutionPath.get(midCounter));
				
				if(!map.blockInBetween(solutionPath.get(midCounter+1), solutionPath.get(endCounter), sectorLength/2)){
					smoothPath.addAll(solutionPath.subList(midCounter,endCounter-1));
				}
			}	
			smoothPath.add(solutionPath.get(endCounter));	
			// remainder points... 
			// to do 
		}
		solutionPath = smoothPath;
	}

	public void smoothPath(){
		ArrayList<Point> smoothPath = new ArrayList<Point>();
		smoothPath.add(solutionPath.get(0));
		for(int i = 1; i < solutionPath.size() -1 ; i++){
			if(solutionPath.get(i).x == solutionPath.get(i+1).x || solutionPath.get(i).y == solutionPath.get(i+1).y){
				i++;
			}
			smoothPath.add(solutionPath.get(i));
		}
		// get rid of diagonal
		smoothPath.add(solutionPath.get(solutionPath.size()-1));
		solutionPath = smoothPath;
	}

}
