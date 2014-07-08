package com.darkdensity.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import com.darkdensity.path.AStarNode;

public class TestAStarPoint extends JPanel {

	ArrayList<Point> solutionList;
	Vector<AStarNode> allPoints;
	public TestAStarPoint(ArrayList<Point> solutionList) {
		this.solutionList = solutionList;
	}
	
	public void setAllPoint(Vector<AStarNode> allPoints){
		this.allPoints = allPoints;
	}
	
	  public void paintComponent(Graphics g) {
		  
		  
		    super.paintComponent(g);

		    Graphics2D g2d = (Graphics2D) g;

	    	g2d.setColor(Color.ORANGE);
		    // draw all the points
		    for(int i = 0; i<allPoints.size(); i++){
		        g2d.drawLine(allPoints.get(i).getPos().x, allPoints.get(i).getPos().y, allPoints.get(i).getPos().x, allPoints.get(i).getPos().y);	     
			}
		    
	    	g2d.setColor(Color.RED);
		    for(int i = 0; i<solutionList.size()-1; i++){
		        g2d.drawLine(solutionList.get(i).x/16, solutionList.get(i).y/16, solutionList.get(i+1).x/16, solutionList.get(i+1).y/16);
		        g2d.drawRect(solutionList.get(i).x/16, solutionList.get(i).y/16, 2, 2);
		    }
		    
		    
		  }
}
