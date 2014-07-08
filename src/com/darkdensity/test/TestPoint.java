package com.darkdensity.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.JPanel;

import com.darkdensity.setting.Constant;

public class TestPoint extends JPanel {
	
	ArrayList<Point> path;
	Point beginPoint;
	Point endPoint;
	Point nearestPoint;
	Point nearestPoint2;
	
	TestPoint(ArrayList<Point> path){
		this.path = path;
	}
	
	void setBeginPoint(Point beginPoint){
		this.beginPoint = beginPoint;
	}
	
	void setEndPoint(Point endPoint){
		this.endPoint = endPoint;
	}

	void setNearestPoint(Point nearestPoint){
		this.nearestPoint = nearestPoint;
	}
	void setNearestPoint2(Point nearestPoint2){
		this.nearestPoint2 = nearestPoint2;
	}
	  public void paintComponent(Graphics g) {
		  
		  
	    super.paintComponent(g);

	    Graphics2D g2d = (Graphics2D) g;
	    
	    // get the path
	    Hashtable<Point, HashSet<Point>> nodeObjects =  Constant.ASTAR_NODES;
	    Enumeration<Point> keys = nodeObjects.keys();
		g2d.setColor(Color.ORANGE);
		while(keys.hasMoreElements()){
			Point key = keys.nextElement();
			HashSet<Point> neighbours = nodeObjects.get(key);
			for(Point neighbour: neighbours){
				g2d.drawLine(key.x/16, key.y/16, neighbour.x/16, neighbour.y/16);
			}
		}

	    for(int i = 0; i<path.size()-1; i++){
	   //   Point currentTile = (list.get(i)).getTile();
	    	g2d.setColor(Color.RED);
	        g2d.drawLine(path.get(i).x/16, path.get(i).y/16, path.get(i+1).x/16, path.get(i+1).y/16);
	        g2d.drawRect(path.get(i).x/16, path.get(i).y/16, 2, 2);
	       // System.out.println(i+ "th point: " + path.get(i) +" " + endPoint.distance(path.get(i)));
	      
	    }
	    
	    
	    g2d.drawRect(path.get(path.size()-1).x/16, path.get(path.size()-1).y/16, 2, 2);
	   // System.out.println("last point: " + path.get(path.size()-1) +" " + endPoint.distance(path.get(path.size()-1)));
	    g2d.setColor(Color.BLUE);
	   // g2d.drawRect(94,227, 4, 6);
	    g2d.drawRect(beginPoint.x/16, beginPoint.y/16, 4, 6);
	    
	    g2d.setColor(Color.BLUE);
	    g2d.drawRect(endPoint.x/16, endPoint.y/16, 4, 6);
	    //g2d.drawLine(beginPoint.x*2, beginPoint.y*2, beginPoint.x*2+4, beginPoint.y*2+4);
	    
	    g2d.setColor(Color.BLACK);
	    g2d.drawRect(nearestPoint.x/16, nearestPoint.y/16, 4, 4);
	    
	    g2d.setColor(Color.BLACK);
	    g2d.drawRect(nearestPoint2.x/16, nearestPoint2.y/16, 4, 4);
	    
	    if(nearestPoint.equals(nearestPoint2)){
	    	g2d.setColor(Color.RED);
	    	  g2d.drawRect(nearestPoint2.x/16, nearestPoint2.y/16, 4, 4);
	    }
	    //g2d.drawRect(endPoint.x, endPoint.y, 4, 6);
	   // g2d.drawLine(endPoint.x*2, endPoint.y*2, endPoint.x*2+4, endPoint.y*2+4);
	    

	  }
}