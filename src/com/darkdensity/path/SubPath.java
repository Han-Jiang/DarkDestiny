package com.darkdensity.path;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import sun.dc.path.PathError;

/**
* @ClassName: SubPath
* @Description: one class corresponding to the road between two crosses
* @author Team A1 - Yingjing Feng
*/

public class SubPath {
//	ConcurrentHashMap<Point, Point> path;
	
	/**path: store the points of subpath**/
	
	/**using hash in HashMap to indicate the relation**/
	HashMap<Point, Point> path;
	
	/**path: store the first point of subpath **/
	Point firstPointOfPath;
	
	/** whether the road is blocked**/
	boolean isBlocked;
	
	/**
	* <p>Title: constructor</p>
	* <p>Description: get subPath</p>
	* @param pointList
	*/
	
	public SubPath(ArrayList<Point> pointList){
		path = new HashMap<Point, Point>();
		if(pointList==null || pointList.size()==0)
			return;
		updateSubPath(pointList);

	}
	
	/** 
	* @Title: updateSubPath 
	* @Description: TODO(What the method do) 
	* @param @param pointList
	* @return void    
	* @throws 
	*/ 
	
	public void updateSubPath(ArrayList<Point> pointList){
//		System.out.println("pointList size"+pointList.size());
		path.clear();
		if(pointList.size()>1){
			for(int i = 1; i < pointList.size(); i++){
				path.put(pointList.get(i-1), pointList.get(i));	
			}
			path.put(pointList.get(pointList.size()-1), pointList.get(pointList.size()-1));
			firstPointOfPath = pointList.get(0);
		}

		//System.out.println("==update sub path==");
//		Point iPoint = firstPointOfPath;
//		while(iPoint!=null){
//			System.out.println(iPoint + " " + path.get(iPoint));
//			iPoint = getNextPoint(iPoint);
//		}
	}
	
	/** 
	* @Title: getNextPoint 
	* @Description: get next point
	* @param @param p
	* @param @return
	* @return Point    
	* @throws 
	*/ 
	
	public Point getNextPoint(Point p){
		return path.get(p);
	}
	
//	public ConcurrentHashMap<Point, Point> getPath(){
	/** 
	* @Title: getPath 
	* @Description: get the path
	* @param @return
	* @return HashMap<Point,Point>    
	* @throws 
	*/ 
	
	public HashMap<Point, Point> getPath(){
		return path;
	}
	
	/** 
	* @Title: isBlocked 
	* @Description: return the road is walkable or not
	* @param @return
	* @return boolean    
	* @throws 
	*/ 
	
	public boolean isBlocked(){
		return isBlocked;
	}
	
	/** 
	* @Title: getFirst 
	* @Description: get the first point
	* @param @return
	* @return Point    
	* @throws 
	*/ 
	
	public Point getFirst(){
		return firstPointOfPath;
	}
	
	/** 
	* @Title: clearPath 
	* @Description: clear the hash table
	* @param 
	* @return void    
	* @throws 
	*/ 
	
	public void clearPath(){
		path.clear();
	}
	
	/** 
	* @Title: setIsBlocked 
	* @Description: set isBlocked
	* @param @param isBlocked
	* @return void    
	* @throws 
	*/ 
	
	public void setIsBlocked(boolean isBlocked){
		this.isBlocked = isBlocked;
	}
	
	/** 
	* @Title: getIsBlocked 
	* @Description: get isBlocked 
	* @param @return
	* @return boolean    
	* @throws 
	*/ 
	
	public boolean getIsBlocked(){
		return isBlocked;
	}
	
	/** 
	* @Title: containsKey 
	* @Description: contains the key or not
	* @param @param p
	* @param @return
	* @return boolean    
	* @throws 
	*/ 
	
	public boolean containsKey(Point p){
		return path.containsKey(p);
	}
	
	/** 
	* @Title: getPoints 
	* @Description: get all the points in the set
	* @param @return
	* @return Set<Point>    
	* @throws 
	*/ 
	
	public Set<Point> getPoints(){
		return path.keySet();
	}
}
