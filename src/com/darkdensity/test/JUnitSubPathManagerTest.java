package com.darkdensity.test;

import static org.junit.Assert.assertEquals;

import java.awt.Point;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.darkdensity.path.SubPath;
import com.darkdensity.path.SubPathManager;
import com.darkdensity.setting.Constant;
import com.darkdensity.util.Pair;


public class JUnitSubPathManagerTest {
	SubPathManager subPathManager;

	@Before
	public void setUp() throws Exception {
		Constant.initAStarNode();
		subPathManager= new SubPathManager();
		//subPathManager.initSubPaths();
	}
	@Test
	public void testInitSubPath() {
		System.out.println("subPath size : " +subPathManager.subPaths.size() +'\n' ); 
		Point point2 = new Point(3696,1520);
		Point point1 = new Point(3536,1664);
		//System.out.println("for pair(" + point1 + "," + point2+ ") the path is: " + subPathManager.subPaths.get(new Pair<Point,Point> (point1, point2 )));
	}
	
	@Test
	public void testFindPath() {
		subPathManager.initSubPaths();
		System.out.println("subPath size : " +subPathManager.subPaths.size() +'\n' ); 
		Point point2 = new Point(3696,1520);
		Point point1 = new Point(3536,1664);
		Point pointMid = new Point((point1.x+point2.x)/2,(point1.y+point2.y)/2);
		System.out.println("point1 "+point1);
		System.out.println("point2 "+point2);
		System.out.println("pointMid "+pointMid);
		System.out.println("findPair "+subPathManager.findPair(pointMid));
		System.out.println("the path is: " + subPathManager.subPaths.get(subPathManager.findPair(pointMid)));
	}

	@Test
	public void testFindPair() {

		int testTime = 120;
		while(testTime -- >0){
			Random r = new Random(92);
			int number = r.nextInt();
			Hashtable<Point, HashSet<Point>>crossesTable =  Constant.ASTAR_NODES;
			Enumeration<Point> keys = crossesTable.keys();
			while(number-->0){
				keys.nextElement();
			}
			Point key = keys.nextElement();
			HashSet<Point> neighbourHashSet =  crossesTable.get(key);
			for(Point neighPoint: neighbourHashSet){
				Point middlePoint = new Point((neighPoint.x+3*key.x)/4+2,(neighPoint.y+3*key.y)/4+2);
				//System.out.println("find pair of :" + middlePoint + "and " + "its pair is" + subPathManager.findPair(middlePoint));
				//System.out.println("it should be in the pair of("+key + " " +neighPoint + ")");
				assertEquals(new Pair<Point, Point>(key,neighPoint), subPathManager.findPair(middlePoint));
			}
		}
		Point point1 = new Point(3477,1638);
		Point point2 = new Point(3536,1664);
		Point middlePoint = new Point((point1.x+point2.x)/2,(point1.y+point2.y)/2);
		Pair<Point, Point> pair = subPathManager.findPair(middlePoint);
		System.out.println("subPathManager.findpair"+pair.fst+" " + pair.snd);
		System.out.println("subPathManager.findpath"+subPathManager.getPath(pair).getPath());
	}
	
	@Test
	public void testFindPathInSubPath(){
		int testTime = 1;
		while(testTime -- >0){
			Random r = new Random(92);
			int number = r.nextInt();
			Hashtable<Point, HashSet<Point>>crossesTable = Constant.ASTAR_NODES;
			Enumeration<Point> keys = crossesTable.keys();
			while(--number>0){
				keys.nextElement();
			}
			Point key = keys.nextElement();
			HashSet<Point> neighbourHashSet =  crossesTable.get(key);
			for(Point neighPoint: neighbourHashSet){
				key = new Point(3536,1664);
				neighPoint = new Point (3488,1616);
				Point middlePoint = new Point((neighPoint.x+3*key.x)/4+2,(neighPoint.y+3*key.y)/4+2);//near neighPoint
				Pair<Point,Point> pair = new Pair<Point,Point>(key, neighPoint);
				SubPath subPath = subPathManager.subPaths.get(pair);
				System.out.println("middle point"+middlePoint);
				System.out.println("pair"+pair);
//			    System.out.println("subsubpath1:"+subPathManager.findPathInSubPath(pair.fst, middlePoint));
//				System.out.println("subsubpath2:"+subPathManager.findPathInSubPath(middlePoint, pair.snd));
				//System.out.println("subsubpath2:"+subPathManager.findPathInSubPath(key,neighPoint));
				System.out.println("subsubpath2:"+subPathManager.findPathInSubPath(neighPoint,key));
			}
		}		
		
	}


}
