package com.darkdensity.test;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.darkdensity.path.SubPath;

public class JUnitSubPathTest {
	
	SubPath subPath;
	Point currentPoint;

	@Before
	public void setUp() throws Exception {
		ArrayList<Point> pointList = new ArrayList<Point>();
		pointList.add(new Point(0,0));
		pointList.add(new Point(0,1));
		pointList.add(new Point(0,2));
		subPath = new SubPath(pointList);
		currentPoint = subPath.getFirst();
	}

	@Test
	public void test() {
		System.out.println(subPath.getPath());
		System.out.println("currentPoint"+subPath.getNextPoint(currentPoint));
		subPath.clearPath();
		System.out.println(subPath.getPath());
		System.out.println("currentPoint"+subPath.getNextPoint(currentPoint));
	}

}
