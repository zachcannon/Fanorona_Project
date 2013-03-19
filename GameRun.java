package fanorona.com;

public class GameRun {
	int temp;
	public GameRun() {
		temp = 0;
	}

	public void run_game(int choice) {
		System.out.println("Game started\n");
		Board game_board = new Board();
		
		if(choice == 0) { //Human vs Human
			System.out.println("Human vs Human Selected\n");
			Human_Player guy_one = new Human_Player(1);
			Human_Player guy_two = new Human_Player(2);
		} else if (choice == 1) {			
			System.out.println("Human vs Computer Selected\n");
			Human_Player guy_one = new Human_Player(1);
			Computer_Player guy_two = new Computer_Player(2);
		} else if (choice == 2) {
			System.out.println("Computer vs Computer Selected\n");
			Computer_Player guy_one = new Computer_Player(1);
			Computer_Player guy_two = new Computer_Player(2);
		}
		
		
		
	}
}
