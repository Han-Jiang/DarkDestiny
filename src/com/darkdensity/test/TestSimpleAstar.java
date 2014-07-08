package com.darkdensity.test;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.darkdensity.core.GridMapManager;
import com.darkdensity.path.SimpleAStarNode;
import com.darkdensity.path.SimpleAStarSearch;
import com.darkdensity.path.SubPathManager;
import com.darkdensity.setting.Constant;
import com.darkdensity.util.Pair;

public class TestSimpleAstar {
	public static void main(String arg[]) throws IOException {
		//GameCore gameCore = new GameCore();
		GridMapManager gridMapManager = new GridMapManager(1000,1000,null);
		while(!GridMapManager.gridMap.isCompleted()){
		}
		Constant.initAStarNode();
		System.out.println("jsonReading"+ Constant.ASTAR_NODES.keys());
		SimpleAStarNode eNodeInvalid = new SimpleAStarNode(new Point(0,0));
		SubPathManager subPathManager = new SubPathManager();
//	
//		Point startPoint = new Point(2512,2416);
//		Point endPoint = new Point(2000,2928);
		
//		Point startPoint = new Point(2520,2433);
//		Point endPoint = new Point(1000,750);
		
//		Point startPoint = new Point(3490,1616);
//		Point endPoint = new Point(4407,1430);
		
		/**special case 1**/
		Point startPoint = new Point(3736,1495);
		Point endPoint = new Point(3654,1192);
		
		/**special case 2**/
//		Point startPoint = new Point(3488,1616);
//		Point endPoint = new Point(3381,1241);
		long beforetime = System.currentTimeMillis();
		SimpleAStarSearch aStarSearch = new SimpleAStarSearch(startPoint,endPoint);
		aStarSearch.Search();
		System.out.println("Time used:" + (System.currentTimeMillis() - beforetime));
		System.out.println("closelist"+aStarSearch.getCloseList().size());

		ArrayList<Point> simpleAStarPath = aStarSearch.getSolutionPath();
		Pair<Point, Point> pair = subPathManager.findPair(startPoint);
		for(int i = 0; i<simpleAStarPath.size()-1;i++){
			if(pair.fst.equals(simpleAStarPath.get(i))&&pair.snd.equals(simpleAStarPath.get(i+1))
					||
					pair.snd.equals(simpleAStarPath.get(i))&&pair.fst.equals(simpleAStarPath.get(i+1))){
				for(int j=0;j<=i;j++){
					simpleAStarPath.remove(i);
				}
				break;
			}
		}
		//System.out.println("key set num:"+GridMapManager.gridMap.getMap().keySet().size());
		JFrame jFrame= new JFrame("Test AStar Point");
		jFrame.setSize(400,400);
		System.out.println("get Solution List" +simpleAStarPath);
		TestPoint points = new TestPoint(simpleAStarPath);

		points.setBeginPoint(startPoint);
		points.setEndPoint(endPoint);
		Point neareastPoint = aStarSearch.findNearestPoint(startPoint, Constant.BEGIN_POINT);//GREEN
		Point neareastPoint2 = aStarSearch.findNearestPoint(endPoint, Constant.END_POINT);//BLUE
		System.out.println("nearest Point"+neareastPoint);
		System.out.println("nearest Point2"+neareastPoint2);
		points.setNearestPoint(neareastPoint);//BLACK
		points.setNearestPoint2(neareastPoint2);//WHITE
		
		jFrame.add(points);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		
		
	}
}
