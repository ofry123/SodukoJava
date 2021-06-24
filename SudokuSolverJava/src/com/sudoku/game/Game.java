package com.sudoku.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.sudoku.gfx.Display;

public class Game implements Runnable {
	
	private Display display;
	private Board board;
	
	private int width, height;
	
	private boolean running;
	private Thread thread;
	private BufferStrategy bs;
	private Graphics g;
	
	public Game(String title, int width, int height) {
		this.width = width;
		this.height = height;
		
		display = new Display(title, width, height);
		board = new Board(this, new int[][] {
			{5,3,0,0,7,0,0,0,0},
			{6,0,0,1,9,5,0,0,0},
			{0,9,8,0,0,0,0,6,0},
			{8,0,0,0,6,0,0,0,3},
			{4,0,0,8,0,3,0,0,1},
			{7,0,0,0,2,0,0,0,6},
			{0,6,0,0,0,0,2,8,0},
			{0,0,0,4,1,9,0,0,5},
			{0,0,0,0,8,0,0,7,9}
		});
	}
	
	@Override
	public void run() {
		
		board.start();
		
		while (running) {
			tick();
			render();
		}
		
	}
	
	private void tick() {
		board.tick();
	}
	
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
        if (bs == null){
            display.getCanvas().createBufferStrategy(2);
            return;
        }
        g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		
		board.render(g);
		
		bs.show();
		g.dispose();
	}
	
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	
	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
