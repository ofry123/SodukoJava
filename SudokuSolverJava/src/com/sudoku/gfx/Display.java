package com.sudoku.gfx;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Canvas canvas;
	
	public Display(String title, int width, int height) {
		super(title);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setFocusable(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		add(canvas);
		
		pack();
	}
	
	public Canvas getCanvas() { return canvas; }
	
	

}
