package com.sudoku.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Board {
	
	static class SudokuSolver implements Runnable {
		
		private Board parent;
		
		public SudokuSolver(Board parent) {
			this.parent = parent;
		}
		
		@Override
		public void run() {
			solve();
		}
		
		private int[] emptyPlaces(int[][] board) {
			for (int row = 0; row < 9; row++)
				for (int col = 0; col < 9; col++)
					if (board[row][col] == 0) return new int[] {col, row};
			return new int[] {-1, -1};
		}
		
		private boolean solve() {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int[] empty = emptyPlaces(parent.board);
			int row = empty[1];
			int col = empty[0];
			parent.currentIndex = row * 9 + col;
			
			if (row == -1 || col == -1)
				return true;
			
			for (int i = 1; i <= 9; i++) {
				parent.board[row][col] = i;
				if (parent.isBoardLegal() && solve())
					return true;
				parent.board[row][col] = 0;
			}
			
			return false;
		}
		
		
	}
	
	private Game game;
	boolean[][] originalNumbers;
	volatile int[][] board;
	volatile int currentIndex = -1;
	
	Thread solverThread;
	private SudokuSolver solver;
	
	public Board(Game game, int[][] data) {
		this.game = game;
		
		board = new int[9][];
		originalNumbers = new boolean[9][];
		for (int i = 0; i < 9; i++) {
			board[i] = new int[9];
			originalNumbers[i] = new boolean[9];
		}		
		loadData(data);
		
		solver = new SudokuSolver(this);
		solverThread = new Thread(solver);
	}
	
	private void loadData(int[][] data) {
		for (int row = 0; row < 9; row++)
			for (int col = 0; col < 9; col++) {
				board[row][col] = data[row][col];
				originalNumbers[row][col] = data[row][col] != 0;
			}
	}

	private void clearBooleanArray(boolean arr[]) {
		for (int i = 0; i < arr.length; i++)
			arr[i] = false;
	}
	
	public boolean isBoardLegal() {
		boolean flags[] = new boolean[9];
		
		//Rows
		for (int row = 0; row < 9; row++) {
			clearBooleanArray(flags);
			for (int col = 0; col < 9; col++) {
				if (board[row][col] == 0)
					continue;
				if (flags[board[row][col] - 1])
					return false;
				flags[board[row][col] - 1] = true;
			}
		}
		
		//Cols
		for (int col = 0; col < 9; col++) {
			clearBooleanArray(flags);
			for (int row = 0; row < 9; row++) {
				if (board[row][col] == 0)
					continue;
				if (flags[board[row][col] - 1])
					return false;
				flags[board[row][col] - 1] = true;
			}
		}
		
		//Squares
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				clearBooleanArray(flags);
				for (int innerRow = 0; innerRow < 3; innerRow++) {
					for (int innerCol = 0; innerCol < 3; innerCol++) {
						if (board[row * 3 + innerRow][col * 3 + innerCol] == 0)
							continue;
						if (flags[board[row * 3 + innerRow][col * 3 + innerCol] - 1])
							return false;
						flags[board[row * 3 + innerRow][col * 3 + innerCol] - 1] = true;
					}
				}
			}
		}
		
		return true;
	}
	
	public void start() {
		solverThread.start();
	}
	
	public void tick() {
		
		
	}
	
	public void render(Graphics g) {
		Dimension windowSize = game.getSize();
		int tileSize = Math.min(windowSize.width / 9, windowSize.height / 9);
		
		Graphics2D g2d = (Graphics2D)g;
		
		for (int row = 1; row < 9; row++) {
			if (row % 3 == 0)
				g2d.setStroke(new BasicStroke(5));
			else
				g2d.setStroke(new BasicStroke(3));
			g2d.drawLine(0, row * tileSize, windowSize.width, row * tileSize);
		}
		
		for (int col = 1; col < 9; col++) {
			if (col % 3 == 0)
				g2d.setStroke(new BasicStroke(5));
			else
				g2d.setStroke(new BasicStroke(3));
			g.drawLine(col * tileSize, 0, col * tileSize, windowSize.height);
		}
		
		for (int row = 0; row < 9; row++)
			for (int col = 0; col < 9; col++) {
				if (board[row][col] != 0) {
					g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 24));
					
					if (currentIndex == row * 9 + col) {
						g2d.setColor(new Color(50,205,50));
					} else if (!originalNumbers[row][col]) {
						g2d.setColor(new Color(25,50,205));
					} else {
						g2d.setColor(Color.BLACK);
					}
					g2d.drawString("" + board[row][col], col * tileSize + tileSize / 2, row * tileSize + tileSize / 2);
				}
				
			}
		
	}
	
}
