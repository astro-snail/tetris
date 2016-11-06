package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener{
	
	private static final int BOARD_WIDTH = 20;
	private static final int BOARD_HEIGHT = 30;
	
	private static final long serialVersionUID = 1L;
	
	private boolean isStarted = false;
	private boolean isPaused = false;
	private boolean isFallingFinished = false; 
	private int curX = 0;
	private int curY = 0;
	private int numLinesRemoved = 0;
	private Shape curPiece;
	private Timer timer;
	private Tetrominoes[] board;
	private JLabel statusBar;

	public GameBoard() {
	    setFocusable(true);
	    timer = new Timer(400, this);
	    board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
	    statusBar = new JLabel("Lines removed: " + numLinesRemoved);
	    add(statusBar);
	    addKeyListener(new TetrisKeyListener());
    }
	
	private void clearBoard() {
		for (int i = 0; i < BOARD_WIDTH * BOARD_HEIGHT - 1; i++) {
			board[i] = null;
		}
	}

	private int getSquareSide() {
		int sideX = (int) getSize().getWidth() / BOARD_WIDTH;
		int sideY = (int) getSize().getHeight() / BOARD_HEIGHT;
		
		return Math.min(sideX, sideY);		
	}
	
	private void newPiece() {
		curPiece = new Shape(Tetrominoes.getRandom());
		curX = BOARD_WIDTH / 2;
		curY = BOARD_HEIGHT - 3;
		
		if (!tryMove(curPiece, curX, curY)) {
			curPiece = null;
			timer.stop();
			isStarted = false;
			statusBar.setText("Game over");
		}
	}
	
	private Tetrominoes shapeAt(int x, int y) {
		return board[y * BOARD_WIDTH + x];
	}
	
	public void start() {
		if (isPaused)
			return;
		
		isStarted = true;
		isFallingFinished = false;
		clearBoard();
		newPiece();
		timer.start();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
				
	    Dimension size = getSize();
	    int boardTop = (int) size.getHeight() - BOARD_HEIGHT * getSquareSide();
	    int marginX = (int) (size.getWidth() - BOARD_WIDTH * getSquareSide()) / 2;

	    for (int i = 0; i < BOARD_HEIGHT; i++) {
	      for (int j = 0; j < BOARD_WIDTH; j++) {
	        Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);
	 
	        if (shape != null) {
	          drawSquare(g, marginX + j * getSquareSide(), boardTop + i * getSquareSide(), shape.getColour());
	        }
	      }
	    }
	 
	    if (curPiece != null) {
	      for (int i = 0; i < 4; i++) {
	        int x = curX + curPiece.x(i);
	        int y = curY - curPiece.y(i);
	        drawSquare(g, marginX + x * getSquareSide(), boardTop + (BOARD_HEIGHT - y - 1) * getSquareSide(), curPiece.getShape().getColour());
	      }
	    }
	}
	
	private void drawSquare(Graphics g, int x, int y, Color colour) {
		int side = getSquareSide();
	    g.setColor(colour);
	    g.fillRect(x + 1, y + 1, side - 2, side - 2);
	    
	    g.setColor(Color.WHITE);
	    g.drawLine(x, y, x, y + side - 1);
	    g.drawLine(x, y, x + side - 1, y);
	    
	    g.setColor(colour.darker());
	    g.drawLine(x + 1, y + side - 1, x + side - 1, y + side - 1);
	    g.drawLine(x + side - 1, y + side - 1, x + side - 1, y + 1);
	  }

	private boolean tryMove(Shape newPiece, int newX, int newY) {
		for (int i = 0; i < 4; i++) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			
			if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
				return false;
			}
			
			if (shapeAt(x, y) != null) {
				return false;
			}
		}
		
		curPiece = newPiece;
		curX = newX;
		curY = newY;
		repaint();
		
		return true;
	}
	
	private void removeFullLines() {
		int numFullLines = 0;
		
		for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
			boolean isFullLine = true;
			
			for (int x = 0; x < BOARD_WIDTH; x++) {
				if (shapeAt(x, y) == null) {
					isFullLine = false;
					break;
				}
			}
			
			if (isFullLine) {
				numFullLines++;
				
				for (int newY = y; newY < BOARD_HEIGHT - 1; newY++) {
					for (int newX = 0; newX < BOARD_WIDTH; newX++) {
						board[newY * BOARD_WIDTH + newX] = shapeAt(newX, newY + 1);
					}
				}
			}
		}
		if (numFullLines > 0) {
			numLinesRemoved += numFullLines;
			statusBar.setText("Lines removed: " + numLinesRemoved);
			repaint();
		}
	}
	
	private void pieceDropped() {
		for (int i = 0; i < 4; i++) {
			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[y * BOARD_WIDTH + x] = curPiece.getShape();
		}
		
		removeFullLines();
		
		isFallingFinished = true;
		curPiece = null;
	}

	private void oneLineDown() {
		if (!tryMove(curPiece, curX, curY - 1)) {
			pieceDropped();
		}
	}
	
	private void dropDown() {
		int y = curY;
		
		while (y > 0) {
			if (!tryMove(curPiece, curX, y - 1)) {
				break;
			}
			y--;
		}
		pieceDropped();
	}
	
	private void pause() {
		if (!isStarted) return;
		
		isPaused = !isPaused;
		
		if(isPaused) {
			timer.stop();
			statusBar.setText("Game paused");
		} else {
			timer.start();
			statusBar.setText("Lines removed: " + numLinesRemoved);
		}
		repaint();	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isFallingFinished) {
			oneLineDown();
		} else {
			isFallingFinished = false;
			newPiece();
		}
	}
	
	class TetrisKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent event) {
			super.keyPressed(event);
			
			if (!isStarted || curPiece == null)
				return;
			
			int keyCode = event.getKeyCode();
			 
		    if (keyCode == KeyEvent.VK_CONTROL)
		        pause();
		 
		    if (isPaused)
		        return;
			
			switch (keyCode) {
				case KeyEvent.VK_LEFT:
					tryMove(curPiece, curX - 1, curY);
					break;
				
				case KeyEvent.VK_RIGHT:
					tryMove(curPiece, curX + 1, curY);
					break;
				
				case KeyEvent.VK_UP:
					tryMove(curPiece.rotateLeft(), curX, curY);
					break;
					
				case KeyEvent.VK_DOWN:
					tryMove(curPiece.rotateRight(), curX, curY);
					break;
				
				case KeyEvent.VK_SPACE:
					dropDown();
					break;
			}
		}
	}

}
