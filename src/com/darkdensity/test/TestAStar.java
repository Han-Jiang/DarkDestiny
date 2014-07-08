package com.darkdensity.test;

import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;

import com.darkdensity.core.GridMapManager;
import com.darkdensity.maprender.GridMap;
import com.darkdensity.path.AStarNode;
import com.darkdensity.path.AStarSearch;

public class TestAStar {
	public static void main(String arg[]) throws IOException {

		GridMapManager gridMapManager = new GridMapManager(1000,1000,null);
		while(!GridMapManager.gridMap.isCompleted()){
		}
		GridMap map = gridMapManager.gridMap;
		//GridMap map = new GridMap("DDMAP.json");
		//System.out.print("map" + map.getMap());
		//AStarNode bNode = new AStarNode(3536, 1664);
		//AStarNode bNode = new AStarNode(218,101);
		
		//AStarNode eNode = new AStarNode(228 , 77 );
		//AStarNode eNode = new AStarNode(220,106 );
		//AStarNode eNode = new AStarNode(3495, 1623);
		//System.out.print("map get grid"+map.getGrids(299, 62));
		//System.out.print("map get grid"+map.getGrids(299, 62));
		
//		AStarNode bNode = new AStarNode(3664,1554);
//		AStarNode eNode = new AStarNode(3616,1596);
//		AStarNode bNode = new AStarNode(2520,2433);
//		AStarNode eNode = new AStarNode(1000,750);
		
		AStarNode eNode = new AStarNode(3488,1616);
		AStarNode bNode = new AStarNode(4407,1430);
		bNode.setMap(map);
		eNode.setMap(map);
		long beforetime = System.currentTimeMillis();
		AStarSearch aStarSearch = new AStarSearch(eNode, bNode,15000);
		aStarSearch.setMode(2);
		aStarSearch.setMap(map);
		aStarSearch.Search();
		System.out.println("Time used:" + (System.currentTimeMillis() - beforetime));
		System.out.println("solution"+aStarSearch.getSolutionPath());
		System.out.println("closeList"+aStarSearch.getCloseList().size());
		JFrame jFrame= new JFrame("Test AStar Point");
		jFrame.setSize(400,400);
		AStarNode eNode2 = new AStarNode(0,0);
		AStarSearch aStarSearch2 = new AStarSearch(new AStarNode(3488,1616), new AStarNode(0,0), 30000);
		aStarSearch2.Search();
		System.out.println("Time used:" + (System.currentTimeMillis() - beforetime));
		System.out.println("solution"+aStarSearch2.getSolutionPath());
		System.out.println("closeList"+aStarSearch2.getCloseList().size());
		Vector<AStarNode> closeList = aStarSearch2.getCloseList();
		TestAStarPoint points = new TestAStarPoint(aStarSearch.getSolutionPath());
		points.setAllPoint(closeList);
	
//		points.setBeginPoint(aStarSearch.getBeginPoint());
//		points.setEndPoint(aStarSearch.getEndPoint());
		jFrame.add(points);
		//jFrame.setLayeredPane(layeredPane);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		
	}
	

}
