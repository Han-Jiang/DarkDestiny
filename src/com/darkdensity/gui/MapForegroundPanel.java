package com.darkdensity.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.core.GameWorld;
import com.darkdensity.path.SubPath;
import com.darkdensity.path.SubPathManager;
import com.darkdensity.setting.Config;
import com.darkdensity.setting.Constant;
import com.darkdensity.util.ImageLoader;
import com.darkdensity.util.Pair;


/**
 * @ClassName: MapForeGroundPanel
 * @Description: Shows the map foreground to player
 * @author Team A1 - Han Jiang
 * @date 24 Mar 2014 23:01:48
 */
public class MapForegroundPanel extends JPanel {
	private JFrame frame;
	private Image mapForeground;

	public MapForegroundPanel(JFrame frame) {
		this.frame = frame;
		setLayout(null);
		setSize(frame.getWidth(), frame.getHeight());
		mapForeground = ImageLoader.loadImage(Config.MAP_FOREGROUND_PATH);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(mapForeground, 0 - GameWorld.iRenderX,
				0 - GameWorld.iRenderY, this);
		if (Config.DEBUGMODE) {
			Hashtable<Point, HashSet<Point>> nodeObjects = Constant.ASTAR_NODES;
			Enumeration<Point> keys = nodeObjects.keys();
			g2d.setColor(Color.BLUE);
			while (keys.hasMoreElements()) {
				Point key = keys.nextElement();
				HashSet<Point> neighbours = nodeObjects.get(key);
				for (Point neighbour : neighbours) {
					SubPath subPath = GameWorld.getSubPathManager().getPath(
							new Pair<Point, Point>(key, neighbour));
					if (subPath.isBlocked() == false
							&& !SubPathManager.isRenewing()) {
						Point iPoint = subPath.getFirst();
						Point iNextPoint = subPath.getNextPoint(iPoint);
						while (iPoint != null && iNextPoint != null
								&& !iPoint.equals(iNextPoint)) {
							g2d.drawLine(iPoint.x - GameWorld.iRenderX,
									iPoint.y - GameWorld.iRenderY, iNextPoint.x
											- GameWorld.iRenderX, iNextPoint.y
											- GameWorld.iRenderY);
							iPoint = iNextPoint;
							iNextPoint = subPath.getNextPoint(iPoint);
						}
					}
				}
			}
		}
	}
}
