package fanorona.com;

public abstract class Player {
	public int[][] current_pieces;
	
	public abstract void make_move(int old_x, int old_y, int new_x, int new_y);
}
