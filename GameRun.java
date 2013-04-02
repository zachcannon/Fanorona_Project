package fanorona.com;

public class GameRun {
	int temp;
	public GameRun() {
		temp = 0;
	}

	public void run_game(int choice, int cols, int rows, long time_limit, int diff) {
		Board game_board;
		
		if(choice == 0) { //Human vs Human
			game_board = new Board(0, cols, rows, time_limit, diff);
		} else if (choice == 1) {		
			game_board = new Board(1, cols, rows, time_limit, diff);
		} else if (choice == 2) {
			game_board = new Board(2, cols, rows, time_limit, diff);
		}		
	}
}
