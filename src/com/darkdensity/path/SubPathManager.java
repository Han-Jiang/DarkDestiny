package com.darkdensity.path;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.tile.Barricade;
import com.darkdensity.tile.Tile;
import com.darkdensity.util.Pair;


/**
* @ClassName: SubPathManager
* @Description: manage the SubPath
* @author Team A1 - Yingjing Feng
*/

public class SubPathManager implements Observer {
	
	static Hashtable<Point, HashSet<Point>> crossesTable;

	static ExecutorService executor = Executors.newFixedThreadPool(Constant.NTHREDS);

	int accuracy = 16/(int)Constant.POINT_INTERVAL;
	static int renewingPath = 0;
	
	public static Hashtable<Pair<Point, Point>, SubPath> subPaths; // coordinate on path could be double; tile point
	
	public SubPathManager(){
		subPaths = new Hashtable<Pair<Point, Point>, SubPath>();
		crossesTable =  Constant.ASTAR_NODES;
	//	System.out.println("init SubPathManager");
		initSubPaths();
		System.out.println("sub path manager size"+subPaths.size());
		
	}
	static public Hashtable<Point, HashSet<Point>> getCrossesTable(){
		return crossesTable;
	}
	
	public void initSubPaths() {

		Enumeration<Point> keys = crossesTable.keys();
		while (keys.hasMoreElements()) {
			Point key = keys.nextElement();
			HashSet<Point> neighbourHashSet = crossesTable.get(key);
			for(Point neighPoint : neighbourHashSet){
				double vx, vy;
				if(neighPoint.x==key.x){
					vx = 0;
					if(Math.abs(neighPoint.y-key.y)>Constant.POINT_INTERVAL)
						vy = (neighPoint.y-key.y)>0?Constant.POINT_INTERVAL:((-1)*Constant.POINT_INTERVAL);
					else 
						vy = 0;
				}else{
					double atanA = Math.atan((double)(neighPoint.y-key.y)/(neighPoint.x-key.x));
//					System.out.println("atanA" + atanA + "vx"+Math.cos(atanA) * STEP_VELOCITY + "vy" + (Math.sin(atanA) * STEP_VELOCITY));
					vx = Math.abs(Math.cos(atanA) * Constant.POINT_INTERVAL);
					vy = Math.abs(Math.sin(atanA) * Constant.POINT_INTERVAL);
					vx = (neighPoint.x-key.x)>0? vx:(-1)*vx;
					vy = (neighPoint.y-key.y)>0? vy:(-1)*vy;
				}
				int intervalNum = (int) (key.distance(neighPoint) / Constant.POINT_INTERVAL);
				ArrayList<Point> subPathArrayList = new ArrayList<Point>();
				ArrayList<Point>reversedSubPathArrayList = new ArrayList<Point>();
				for(int i=0; i<=intervalNum; i++){
					subPathArrayList.add(new Point((int)(key.x+vx*i),(int)(key.y+vy*i)));
					reversedSubPathArrayList.add(0,new Point((int)(key.x+vx*i),(int)(key.y+vy*i)));
				}
				if(!subPathArrayList.get(subPathArrayList.size()-1).equals(neighPoint))
					subPathArrayList.add(neighPoint);
				if(!reversedSubPathArrayList.get(0).equals(neighPoint))
					reversedSubPathArrayList.add(0,neighPoint);
								
				if(subPaths.get(new Pair<Point,Point>(key,neighPoint))==null){
					SubPath subPath = new SubPath(subPathArrayList);
					subPaths.put(new Pair<Point,Point>(key,neighPoint), subPath);
				}
				
				if(subPaths.get(new Pair<Point,Point>(neighPoint,key))==null){
					SubPath reversedSubPath = new SubPath(reversedSubPathArrayList);
					subPaths.put(new Pair<Point,Point>(neighPoint,key), reversedSubPath);
				}
			}	
		}
		
	}

	@Override
	public void update(Observable o, final Object t) {
		if(t instanceof Tile){
			int x = ((Tile) t).getX();
			int y = ((Tile) t).getY();
			final ArrayList<Point>blockingArea =  ((Barricade) t).getBlockingArea();
//			System.out.println("in SubPathManager update tile:"+x+" "+ y+ " " + ((Barricade)t).getBlockingArea());
			/**to detect whether it is in the cross**/
			
			final Pair<Point, Point> pairPoints = findPair(new Point(x,y));
			final Pair<Point, Point> reversedPairPoints = new Pair<Point,Point> (pairPoints.snd, pairPoints.fst);
			final SubPath subPath = getPath(pairPoints);
			final SubPath reversedSubPath = getPath(reversedPairPoints);

				// add the blocking area to subpth
				Runnable r = new Runnable() {
					@Override
					public void run() {
						
							try{
							renewingPath ++ ;
							
							// astar search
							AStarSearch aStarSearch = new AStarSearch(new AStarNode(pairPoints.fst.x,pairPoints.fst.y), new AStarNode(pairPoints.snd.x,pairPoints.snd.y)
														,(int)pairPoints.fst.distance(pairPoints.snd)*2);

							aStarSearch.Search();
							System.out.println("a star in SubPathManager: max num"+ (int)pairPoints.fst.distance(pairPoints.snd)*2);
							System.out.println("a star in SubPathManager: close list"+aStarSearch.getCloseList().size());
							System.out.println("a star in SubPathManager: solution"+aStarSearch.getSolutionPath());

							
							// renew subpath according to the result of astar search
							ArrayList<Point> subPathArray = aStarSearch.getSolutionPath();
							ArrayList<Point> reversedSubPathArray = aStarSearch.getReversedSolutionPath();
							if(subPathArray== null || subPathArray.size()==0){
								subPath.setIsBlocked(true);
								reversedSubPath.setIsBlocked(true);
							}else{
								subPath.updateSubPath(subPathArray);
								reversedSubPath.updateSubPath(reversedSubPathArray);
							}
							
							System.out.println("renewingPath--");
							// decrease the renewing path counter
							renewingPath --;
							
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				};
			
				executor.execute(r);
				
			}
		
		
	}
	static public SubPath getPath(Pair<Point, Point> pair){
		if(pair == null)
			return null;
		return subPaths.get(pair);
	}
	
	public static Pair<Point, Point> findPair (Point middlePoint){
		//System.out.println(crossesTable==null?"crossestable null":"crossestable not null");
		Enumeration<Point> keys = crossesTable.keys();
		double nearestDis = 100;
		Pair<Point, Point> nearerPair = null;
		while (keys.hasMoreElements()) {
			Point key = keys.nextElement();
			if(key.distance(middlePoint)>500)
				continue;
			HashSet<Point> neighbourSet = crossesTable.get(key);
			for(Point neighbour:neighbourSet){
				double newDis = neighbour.distance(middlePoint)+key.distance(middlePoint)-neighbour.distance(key);
				if(newDis<nearestDis){
					nearestDis = newDis;
					nearerPair = new Pair<Point, Point>(key, neighbour);
				}
			}
		}
		if(middlePoint == null || nearerPair == null)
			return null;
		if(nearerPair.fst.distance(middlePoint)<nearerPair.snd.distance(middlePoint))
			return nearerPair;
		else {
			Pair<Point,Point> reversedPair = new Pair<Point,Point>(nearerPair.snd, nearerPair.fst);
			return reversedPair;
		}
	}
	
	public Hashtable<Pair<Point, Point>, SubPath> getSubPaths(){
		return subPaths;
	}
	
	public static Pair<Point, Point>findPairfromPair(Point p1, Point p2){
		Pair<Point, Point> pair = findPair(new Point((p2.x+p1.x*3)/4,(p2.y+p1.y*3)/4));
		if(pair.fst.distance(p1)>pair.fst.distance(p2))
			pair = new Pair<Point, Point>(pair.snd, pair.fst);
		return pair;
		
	}
	static public ConcurrentHashMap<Point, Point> findPathInSubPath(Point p1, Point p2){ // p1 is nearer to sprite
		double nearestDis1 = 10000;
		double nearestDis2 = 10000;
		Point nearestPoint1 = null;
		Point nearestPoint2 = null;
		if(p1 == null || p2 == null)
			return null;
		Pair<Point, Point>pair = findPairfromPair(p1,p2);
		SubPath subPath = getPath(pair);
		if(subPath==null || subPath.isBlocked())
			return null;
		
		Point iPoint = subPath.getFirst();
		int index1 = 0;
		int index2 = 0;
		int counter = 0;
		while(iPoint != null){
			if(iPoint.distance(p1)<nearestDis1){
				nearestDis1 = iPoint.distance(p1);
				nearestPoint1 = iPoint;
				index1 = counter;
			}
			if(iPoint.distance(p2)<nearestDis2){
				nearestDis2 = iPoint.distance(p2);
				nearestPoint2 = iPoint;
				index2 = counter;
			}
			iPoint = subPath.getNextPoint(iPoint);
			counter++;
		}
		if(nearestPoint1==null||nearestPoint2==null)
			return null;
		
		boolean isBetween = false;
		ConcurrentHashMap<Point, Point> littleSubPathHash = new ConcurrentHashMap<Point, Point>();
		if(index1<index2){
			
			Point jPoint = subPath.getFirst();
			while(jPoint != null){		
				if(jPoint == nearestPoint1 ){
					isBetween = true;
				}
				if(isBetween){
					littleSubPathHash.put(jPoint, subPath.getNextPoint(jPoint));
				}
				if(jPoint == nearestPoint1 ){
					isBetween = false;
				}
				jPoint = subPath.getNextPoint(jPoint);
			}
		}
		return littleSubPathHash;
		
	}
	
	static public ArrayList<Point> findPathArrayInSubPath(Point p1, Point p2){ // p1 is nearer to sprite
		double nearestDis1 = 10000;
		double nearestDis2 = 10000;
		Point nearestPoint1 = null;
		Point nearestPoint2 = null;
		if(p1 == null || p2 == null)
			return null;
		Pair<Point, Point>pair = findPairfromPair(p1,p2);
		SubPath subPath = getPath(pair);
		if(subPath==null || subPath.isBlocked())
			return null;
		
		Point iPoint = subPath.getFirst();
		int index1 = 0;
		int index2 = 0;
		int counter = 0;
		while(iPoint != null){
			if(iPoint.distance(p1)<nearestDis1){
				nearestDis1 = iPoint.distance(p1);
				nearestPoint1 = iPoint;
				index1 = counter;
			}
			if(iPoint.distance(p2)<nearestDis2){
				nearestDis2 = iPoint.distance(p2);
				nearestPoint2 = iPoint;
				index2 = counter;
			}
			iPoint = subPath.getNextPoint(iPoint);
			counter++;
		}
		if(nearestPoint1==null||nearestPoint2==null)
			return null;

		ArrayList<Point> newList= new ArrayList<Point>();
		
		boolean isBetween = false;
		if(index1<index2){
			
			Point jPoint = subPath.getFirst();
			while(jPoint != null){		
				if(jPoint == nearestPoint1 ){
					isBetween = true;
				}
				if(isBetween){
					newList.add(jPoint);
				}
				if(jPoint == nearestPoint1 ){
					isBetween = false;
				}
				jPoint = subPath.getNextPoint(jPoint);
			}
		}
		
		return newList;
		
	}
	
	public boolean isOnSubPath(Point point){
		Pair<Point,Point> pair = findPair(point);
		if(pair == null)
			return false;
			double disDiff = point.distance(pair.fst)+point.distance(pair.snd)-pair.fst.distance(pair.snd);
		if(disDiff>10)
			return false;
		return true;
	}
	
	static public boolean isRenewing() {
		return (renewingPath != 0);
	}
	
	public ExecutorService getThreadPool(){
		return executor;
	}
	
}
