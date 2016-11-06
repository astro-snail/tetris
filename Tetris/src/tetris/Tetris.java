package tetris;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Tetris extends JFrame {

	/**
	 * Source: http://www.ssaurel.com/blog/learn-to-create-a-tetris-game-in-java-with-swing/
	 */
	private static final long serialVersionUID = 1L;

	public Tetris() throws HeadlessException {
		super();
		GameBoard board = new GameBoard();
		add(board);
		setSize(400, 600);
		board.start();
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private static void startGame() {
		try {
			Tetris tetris = new Tetris();
			tetris.setLocationByPlatform(true);
			tetris.setVisible(true);
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGame();				
			}
		});
	}

}
