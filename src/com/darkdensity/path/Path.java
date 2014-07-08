package com.darkdensity.path;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.darkdensity.core.GameWorld;
import com.darkdensity.setting.Constant;
import com.darkdensity.util.Pair;


/**
* @ClassName: Path
* @Description: 
* @author Team A1 - Yingjing Feng
*/
public class Path {
	Point currentPoint, beginPoint, endPoint;
	LinkedList<SubPath> subPaths;
	SubPath currentSubPath;
	SubPathManager subPathManager;
	boolean isEnd;
	
	public Path(){
		isEnd = true;
	}


	
	/**
	* <p>Title: Path </p>
	* <p>Description: constructor</p>
	* @param bPoint
	* @param ePoint
	* @param simpleAstarArrayList
	* @throws IOException
	*/
	public Path(final Point bPoint, final Point ePoint, ArrayList<Point> simpleAstarArrayList) throws IOException {
//		System.out.println("---simple astar list---"+simpleAstarArrayList);
		
		subPaths = new LinkedList<SubPath>();
		subPathManager = GameWorld.getSubPathManager();
		if(simpleAstarArrayList == null || simpleAstarArrayList.size() == 0){
			ArrayList<Point> subArrayList = new ArrayList<Point>();
			double atanA = Math.atan((double)(ePoint.y-bPoint.y)/(ePoint.x-bPoint.x));
			double vx, vy;
			vx = Math.abs(Math.cos(atanA) * Constant.POINT_INTERVAL);
			vy = Math.abs(Math.sin(atanA) * Constant.POINT_INTERVAL);
			vx = (ePoint.x-bPoint.x)>0? vx:(-1)*vx;
			vy = (ePoint.y-bPoint.y)>0? vy:(-1)*vy;
		
		int intervalNum = (int) (ePoint.distance(bPoint) / Constant.POINT_INTERVAL);
		for(int i=0; i<=intervalNum; i++){
			subArrayList.add(new Point((int)(bPoint.x+vx*i),(int)(bPoint.y+vy*i)));
		}				
		if(!subArrayList.get(subArrayList.size()-1).equals(ePoint))
			subArrayList.add(ePoint);
		SubPath endSubPath = new SubPath(subArrayList);
		subPaths.add(endSubPath);
		
			SubPath subPath = new SubPath(subArrayList);
			subPaths.add(subPath);
			currentSubPath=subPath;
			currentPoint = bPoint;
			endPoint = ePoint;
		}else{
			final Point firstCross = simpleAstarArrayList.get(0);
			final Point lastCross = simpleAstarArrayList.get(simpleAstarArrayList.size()-1);
			
			/**begin**/
			Pair<Point,Point> beginPair = subPathManager.findPair(new Point((bPoint.x+firstCross.x)/2, (bPoint.y+firstCross.y)/2));
			if(beginPair==null ){
				/**do the astar from begin to set**/

				Runnable r = new Runnable() {
					@Override
					public void run() {
						AStarSearch aStarSearch = new AStarSearch(new AStarNode(bPoint.x, bPoint.y), new AStarNode(firstCross.x, firstCross.y), 500);
						try {
							aStarSearch.Search();
						} catch (IOException e) {
							e.printStackTrace();
						}
						SubPath beginPath = null;
						beginPath = new SubPath(aStarSearch.getSolutionPath());
						subPaths.add(beginPath);
						beginPoint = beginPath.getFirst();
						currentPoint = beginPoint;
						
					}
				};
			    subPathManager.getThreadPool().execute(r);
		    }else{
		    	/**on the path**/
		    	SubPath beginPath = null;
				beginPath = subPathManager.getPath(new Pair<Point, Point>(beginPair.snd, beginPair.fst));
				double nearestDistance = 10000;
				Point nearestPoint = null;
				for(Point iPoint:beginPath.getPoints()){
					if(nearestDistance>iPoint.distance(bPoint)){
						nearestDistance = iPoint.distance(bPoint);
						nearestPoint = iPoint;
					}
				}
				beginPoint = nearestPoint;
				currentPoint = beginPoint;
				subPaths.add(beginPath);
			}
			
			/**add each path**/
			Point prevCrossPoint = null;
			for(Point crossPoint:simpleAstarArrayList){

				if(prevCrossPoint!=null){
					SubPath tempSubPath = subPathManager.getPath(new Pair<Point, Point>(prevCrossPoint, crossPoint));
					if(!subPaths.contains(tempSubPath))
						subPaths.add(tempSubPath);
				}
				prevCrossPoint = crossPoint;
			}
			
			/**end**/
			Pair<Point,Point> endPair = subPathManager.findPair(new Point((ePoint.x+lastCross.x)/2, (ePoint.y+lastCross.y)/2));
			if(endPair==null){
				/**not on the street, go there directly**/
				ArrayList<Point> closePointsList = new ArrayList<Point>();
				double vx, vy;
				if(ePoint.x==lastCross.x){
					vx = 0;
					if(Math.abs(ePoint.y-lastCross.y)>Constant.POINT_INTERVAL)
						vy = (ePoint.y-lastCross.y)>0?Constant.POINT_INTERVAL:((-1)*Constant.POINT_INTERVAL);
					else 
						vy = 0;
				}else{
					double atanA = Math.atan((double)(ePoint.y-lastCross.y)/(ePoint.x-lastCross.x));
//					System.out.println("atanA" + atanA + "vx"+Math.cos(atanA) * STEP_VELOCITY + "vy" + (Math.sin(atanA) * STEP_VELOCITY));
					vx = Math.abs(Math.cos(atanA) * Constant.POINT_INTERVAL);
					vy = Math.abs(Math.sin(atanA) * Constant.POINT_INTERVAL);
					vx = (ePoint.x-lastCross.x)>0? vx:(-1)*vx;
					vy = (ePoint.y-lastCross.y)>0? vy:(-1)*vy;
				}
				int intervalNum = (int) (ePoint.distance(lastCross) / Constant.POINT_INTERVAL);
				for(int i=0; i<=intervalNum; i++){
					closePointsList.add(new Point((int)(lastCross.x+vx*i),(int)(lastCross.y+vy*i)));
				}				
				if(!closePointsList.get(closePointsList.size()-1).equals(ePoint))
					closePointsList.add(ePoint);
				SubPath endSubPath = new SubPath(closePointsList);
				subPaths.add(endSubPath);

			}else{
				SubPath endPath = subPathManager.getPath(endPair);
				double nearestDistance = 10000;
				if(endPath!=null){
					for(Point iPoint:endPath.getPoints()){
						if(nearestDistance>iPoint.distance(ePoint)){
							nearestDistance = iPoint.distance(ePoint);
							endPoint = iPoint;
						}
					}
					subPaths.add(endPath);
				}else{
					System.out.println("endpath should not be null!!");
				}
			}
			currentSubPath = subPaths.get(0);
			isEnd = false;
		}

	}


	/** 
	* @Title: spriteNextPoint 
	* @Description: this method is called when the sprite reach (distance within 1 grid) the next point
	* @param @param thisPoint
	* @param @return
	* @param @throws FileNotFoundException
	* @param @throws IOException
	* @return Point    
	* @throws IOException FileNotFoundException
	*/ 
	public Point spriteNextPoint(final Point thisPoint) throws FileNotFoundException, IOException {
		//System.out.println("ask for next point");
		
		Point nextPoint = null;
		
		if(currentPoint!=null)
			nextPoint = currentSubPath.getNextPoint(currentPoint);
		
		System.out.println(" thisPoint " + thisPoint + " currentPoint "+currentPoint + " nextPoint "+nextPoint);
		/**the route is changed**/
		/**NOT IMPLEMENTED**/
		if(nextPoint == null && thisPoint!=null){
			System.out.println("The route is change !!!");
			Pair<Point,Point> pair = subPathManager.findPair(thisPoint);
			if(pair!=null && subPathManager.getPath(pair).isBlocked==true){
				isEnd = true;
			}else{
				isEnd = true;
				System.out.println("stop for looking for new road");
				Runnable r = new Runnable() {
					@Override
					public void run() {
						SubPath nextSubPath;
						Point nextCross = null;
						if(subPaths.indexOf(currentSubPath) < subPaths.size() -1){
							nextSubPath = subPaths.get(subPaths.indexOf(currentSubPath) + 1);
							nextCross = nextSubPath.getFirst();
						}else{
							nextCross = endPoint;
						}
						try {
							SimpleAStarSearch simpleAStarSearch = new SimpleAStarSearch(new Point(thisPoint.x, thisPoint.y), endPoint);
							simpleAStarSearch.Search();
							Path newPath = new Path(new Point(thisPoint.x, thisPoint.y), endPoint, simpleAStarSearch.getSolutionPath());
							subPaths = newPath.subPaths;
							beginPoint = newPath.beginPoint;
							currentPoint = newPath.currentPoint;
							endPoint = newPath.endPoint;
							currentSubPath = newPath.currentSubPath;
							subPathManager = newPath.subPathManager;
							isEnd = false;
							System.out.println("restart");
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				};
			    subPathManager.getThreadPool().execute(r);
			    return thisPoint;
			}
		}
		
		if(currentPoint!=null && currentPoint.equals(endPoint))
			isEnd = true;
		
		if(isEnd)
			return null;
		
		boolean firstPoint = false;

		
		/** update subpath if needed**/
		// if next point is null
		if (nextPoint == null || nextPoint.equals(currentPoint)){
			SubPath nextSubPath = null;
			if(subPaths.indexOf(currentSubPath) < subPaths.size() -1){
	//			System.out.println("udpate sub path" + nextSubPath);
				nextSubPath = subPaths.get(subPaths.indexOf(currentSubPath) + 1);
	//			System.out.println("[[[udpate sub path]]]" + nextSubPath.getPath());
			}
			// there are more point(s), then update subpath
			if(nextSubPath != null){
				currentSubPath = nextSubPath;
				nextPoint = currentSubPath.getFirst();
				if(nextPoint != null)
					nextPoint = currentSubPath.getNextPoint(nextPoint);
				firstPoint = true;
			}else{
				firstPoint = false;
			}
		}
		
		currentPoint = nextPoint;
		//System.out.println("currentSubPath"+currentSubPath.getPath());
	//	System.out.println("currentPoint"+currentPoint);
		
		return currentPoint;
		
	}
	
	/** 
	* @Title: isEnd 
	* @Description: TODO(What the method do) 
	* @param @return
	* @return boolean    
	* @throws 
	*/ 
	
	public boolean isEnd(){
		return isEnd;
	}
	
	/** 
	* @Title: getPath 
	* @Description: TODO(What the method do) 
	* @param @return
	* @return ArrayList<Point>    
	* @throws 
	*/ 
	
	public ArrayList<Point> getPath(){
		
		ArrayList<Point> pathArrayList = new ArrayList<Point>();
		SubPath beginSubPath = subPaths.get(0); 
		
		/**begin**/
		Point iPoint = beginPoint;
		Point lastPoint = null;
		System.out.println("---begin point---"+beginPoint);
		System.out.println("---end point---"+endPoint);
		System.out.println("---begin---");
		while(iPoint!=null && !iPoint.equals(lastPoint)){
			pathArrayList.add(iPoint);
			System.out.println(iPoint + " " + beginSubPath.getNextPoint(iPoint));
			lastPoint = iPoint;
			iPoint = beginSubPath.getNextPoint(iPoint);
		}
		
		/**middle**/
		System.out.println("---middle---");
		lastPoint = null;
		for(int i = 1; i<subPaths.size()-1 ; i++){
			SubPath currentSubPath = subPaths.get(i);
			iPoint = currentSubPath.getFirst();
			System.out.println("---middle" + i + "---");
			while(iPoint!=null && !iPoint.equals(lastPoint)){
				pathArrayList.add(iPoint);
				System.out.println(iPoint + " " + currentSubPath.getNextPoint(iPoint));
				lastPoint = iPoint;
				iPoint = currentSubPath.getNextPoint(iPoint);
			}
		}
		
		/**end**/
		
		System.out.println("---end---");
		SubPath endSubPath = subPaths.get(subPaths.size()-1);
		iPoint = endSubPath.getFirst();
		lastPoint = null;
		while(iPoint!=null && !iPoint.equals(endPoint) && !iPoint.equals(lastPoint)){
			pathArrayList.add(iPoint);
			System.out.println(iPoint + " " + endSubPath.getNextPoint(iPoint));
			lastPoint = iPoint;
			iPoint = endSubPath.getNextPoint(iPoint);
		}
		pathArrayList.add(endPoint);
		return pathArrayList;
	}
}
