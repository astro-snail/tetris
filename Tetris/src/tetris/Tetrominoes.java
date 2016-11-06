package tetris;

import java.awt.Color;
import java.util.Random;

enum Tetrominoes {
	PALKA(new int[][]{{0, -1}, {0, 0}, {0, 1}, {0, 2}}, Color.BLUE),
	TREUGOLNIK(new int[][]{{-1, 0}, {0, 0}, {1, 0}, {0, 1}}, Color.RED),
	SAPOG_L(new int[][]{{-1, 0}, {0, 0}, {0, 1}, {0, 2}}, Color.MAGENTA),
	SAPOG_R(new int[][]{{-1, 0}, {0, 0}, {-1, 1}, {-1, 2}}, Color.CYAN),
	KVADRAT(new int[][]{{-1, 1}, {-1, 0}, {0, 1}, {0, 0}}, Color.YELLOW),
	ZAGOGULKA_L(new int[][]{{-1, 0}, {-1, -1}, {0, 0}, {0, 1}}, Color.PINK),
	ZAGOGULKA_R(new int[][]{{-1, 1}, {-1, 0}, {0, 0}, {0, -1}}, Color.GREEN);
	
	private int[][] coordinates;
	private Color colour;
	
	private Tetrominoes(int[][] coordinates, Color colour) {
		this.setCoordinates(coordinates);
		this.setColour(colour);
	}

	public int[][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(int[][] coordinates) {
		this.coordinates = coordinates;
	}

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}
	
	public static Tetrominoes getRandom() {
		Random random = new Random();
		int i = random.nextInt(Tetrominoes.values().length);
		return Tetrominoes.values()[i];
	}
}
