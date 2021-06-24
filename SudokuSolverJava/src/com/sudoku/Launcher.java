package com.sudoku;

import com.sudoku.game.Game;

public class Launcher {
	
	public static void main(String[] args) {
		
		int size = 64;
		
		Game game = new Game("Sudoku", size * 9, size * 9);
		game.start();
		
	}
	
	
}
