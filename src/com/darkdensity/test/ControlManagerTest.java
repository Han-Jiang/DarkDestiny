package com.darkdensity.test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.darkdensity.core.ControlManager;
import com.darkdensity.core.ControlManager.LeftButtonListener;
import com.darkdensity.core.ControlManager.LeftDraggedListener;
import com.darkdensity.core.ControlManager.MoveListener;
import com.darkdensity.core.ControlManager.RightButtonListener;

public class ControlManagerTest {

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setLayout(null);
		frame.setBackground(Color.blue);

		JPanel panel = new JPanel();
		panel.setSize(500, 500);
		panel.setLocation(5, 5);
		panel.setBackground(Color.yellow);

		frame.add(panel);
		frame.setVisible(true);

		ControlManager controlManager = new ControlManager(panel, frame);


		controlManager.addLeftButtonListener(new LeftButtonListener() {

			@Override
			public void press(int x, int y) {
				// TODO Auto-generated method stub
				System.out.println("LeftPress : " + x + "," + y);
			}
		});

		controlManager.addRightButtonListener(new RightButtonListener() {

			@Override
			public void press(int x, int y) {
				// TODO Auto-generated method stub

				System.out.println("RightPress : " + x + "," + y);

			}
		});
		
		controlManager.addMoveListener(new MoveListener() {
			
			@Override
			public void move(int x, int y) {
				// TODO Auto-generated method stub
//				System.out.println("Move : " + x + "," + y);
			}
		});
		
		controlManager.addLeftDraggedListener(new LeftDraggedListener() {
			
			@Override
			public void dragged(int sx, int sy, int ex, int ey) {
				// TODO Auto-generated method stub
				System.out.println("Left Drag");
				System.out.println("Start : " + sx + "," + sy);
				System.out.println("End   : " + ex + "," + ey);
			}
		});

	}

}
