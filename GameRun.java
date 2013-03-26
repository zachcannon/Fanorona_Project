package fanorona.com;

public class GameRun {
	int temp;
	public GameRun() {
		temp = 0;
	}

	public void run_game(int choice, int cols, int rows) {
		System.out.println("Game started\n");
		Board game_board;
		
		if(choice == 0) { //Human vs Human
			System.out.println("Human vs Human Selected\n");
			game_board = new Board(0, cols, rows);
		} else if (choice == 1) {		
			System.out.println("Human vs Computer Selected\n");
			game_board = new Board(1, cols, rows);
		} else if (choice == 2) {
			System.out.println("Computer vs Computer Selected\n");
			game_board = new Board(2, cols, rows);
		}		
	}
}
