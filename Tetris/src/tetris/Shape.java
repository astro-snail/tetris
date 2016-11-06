package tetris;

public class Shape {
	
	private Tetrominoes pieceShape;
	private int[][] coordinates;
	
	public Shape() {
		this(null);
	}

	public Shape(Tetrominoes shape) {
		coordinates = new int[4][2];
		pieceShape = shape;
		
		if (shape == null)
			return;
		
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 2; j++)
				coordinates[i][j] = shape.getCoordinates()[i][j];
	}
	
	public Tetrominoes getShape() {
		return pieceShape;
	}
	
	private void setX(int index, int x) {
		coordinates[index][0] = x;		
	}
	
	private void setY(int index, int y) {
		coordinates[index][1] = y;
	}
	
	public int x (int index) {
		return coordinates[index][0];
	}
	
	public int y (int index) {
		return coordinates[index][1];
	}
	
	public Shape rotateLeft() {
		if (pieceShape == Tetrominoes.KVADRAT)
			return this;
		
		Shape shape = new Shape(pieceShape);
		for (int i = 0; i < 4; i++) {
			shape.setX(i,-y(i));
			shape.setY(i, x(i));
		}
		return shape;
	}
	
	public Shape rotateRight() {
		if (pieceShape == Tetrominoes.KVADRAT)
			return this;
		
		Shape shape = new Shape(pieceShape);
		for (int i = 0; i < 4; i++) {
			shape.setX(i, y(i));
			shape.setY(i, -x(i));
		}
		return shape;
	}
}
