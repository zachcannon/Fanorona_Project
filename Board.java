package fanorona.com;


import java.awt.*;
import javax.swing.*;

public class Board extends JPanel{
	//----------------------------GLOBAL_VARS--------------------------//
	static final int
	EMPTY = 0,
	BLACK = 1,	//PLAYER 1 is black
	RED = 2;	//PLAYER 2 is red

	JFrame window;
	Canvas board_to_display;
	BoardGraphics graphics = new BoardGraphics();
	
	int game_number_type;
	Player player_1;
	Player player_2;
	public int[][] game_board_array; //Set up in a [x][y] coordinate with [0][0] being the bottom left
	int players_turn;
	
	
	//----------------------------MAIN_GAME----------------------------//	
	public Board(int number_type) { //Constructor
		game_board_array = new int[9][5]; //[x][y] so [0-8][0-4] possible choices
		
		game_number_type = number_type;
		
		if (number_type == 0) {
			player_1 = new Human_Player(1,0); //Player 1, human black
			player_2 = new Human_Player(2,0); //Player 2, human red
		} else if (number_type == 1) {
			player_1 = new Human_Player(1,0); //Player 1, human black
			player_2 = new Computer_Player(2,1); //Player 2, human red
		} else {
			player_1 = new Computer_Player(1,1); //Player 1, human black
			player_2 = new Computer_Player(2,1); //Player 2, human red
		}
		
		setup_board();
		players_turn = 1;		
	}	
	
	public void setup_board() {
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<2; j++) {				
				game_board_array[i][j] = BLACK;
			}
		}
		
		for(int i = 0; i<9; i++) {
			for(int j = 3; j<5; j++) {				
				game_board_array[i][j] = RED;
			}
		}
		
		game_board_array[0][2] = BLACK;
		game_board_array[1][2] = RED;
		game_board_array[2][2] = BLACK;
		game_board_array[3][2] = RED;
		game_board_array[4][2] = EMPTY;
		game_board_array[5][2] = BLACK;
		game_board_array[6][2] = RED;
		game_board_array[7][2] = BLACK;
		game_board_array[8][2] = RED;
	
	}
	
	public int check_end_of_game() { //Returns 0 if fail, 1 if game over
		int black_num = 0;
		int red_num = 0;
		
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<5; j++) {				
				if(game_board_array[i][j] == RED) red_num++;
				if(game_board_array[i][j] == BLACK) black_num++;
			}
		}
		
		if(red_num == 0 || black_num == 0) return 1;
		else return 0;
	}
	
	public void game_main() {//Main logic
		
		//!!!!!!!!!!!!!!!!!!!! SHOW BOARD GUI
		
		while (true) { //Repeats until the window is closed or a break is called (in check game over)
			if (players_turn == 1) {	//BLACK'S TURN			
				int move_success = player_1.players_turn();
				if (move_success == 1) {
					players_turn = 2;
				}
				//!!!!!!!!!!!!!!!!!!!!!!! REPRINT BOARD
			}

			if (check_end_of_game() == 1) break;
			
			if (players_turn == 2) {	//RED'S TURN
				int move_success = player_2.players_turn();
				if (move_success == 1) {
					players_turn = 1;
				}
				//!!!!!!!!!!!!!!!!!!!!!!! REPRINT BOARD
			}
			
			if (check_end_of_game() == 1) break;
		}
		
		//!!!!!!!!!!!!!!!!!!!!!!!!! POP UP WITH WHO WON
	}
	
	
	//---------------------------PLAYER_CLASS--------------------------//
	
	public abstract class Player {
		public int who_am_i; //Corresponds to board colors above in line ~10
		public int what_am_i; //Corresponds to human or comp. 0=human, 1=comp
		public abstract int get_who_i_am();
		public abstract int get_what_i_am();
		
		public abstract int players_turn(); //Return 1 for successful move, return 0 for invalid move choice
		
		public abstract int[] get_move();	//Returns four numbers in an array - 0=old_x, 1=old_y, 2=new_x, 3=new_y, 4=forward or backwards(2=back, 1=forward, 0 if no take)
		public abstract int check_valid_move(int[] array_of_moves); //Checks if moves in array_of_moves are valid, returns 1 if good, returns 0 is invalid
		public abstract void execute_move(int[] array_of_moves);
		public abstract void erase_pieces(int new_x, int new_y, int compass_direction, int direction_to_take);
	}
	
	public class Human_Player extends Player{
		
		public Human_Player(int player_number, int player_type) {
			who_am_i = player_number;
			what_am_i = player_type;
		}
		
		public int get_who_i_am() {return who_am_i;}
		public int get_what_i_am() {return what_am_i;}

		@Override
		public int players_turn() {
			int move_success;
			int[] move_array = get_move();
			
			if(check_valid_move(move_array) == 1) {
				execute_move(move_array);
				move_success = 1;
			} else {
				//!!!!!!!!!!!!!!!PRINT OUT NOT A VALID MOVE
				move_success = 0;
			}			
			return move_success;
		}

		@Override
		public int[] get_move() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int check_valid_move(int[] array_of_moves) {	
			int old_x = array_of_moves[0];
			int old_y = array_of_moves[1];
			int new_x = array_of_moves[2];
			int new_y = array_of_moves[3];
			int direction = array_of_moves[4];
			
			if(game_board_array[old_x][old_y] != players_turn) return 0; //Position clicked does not hold a piece of the player in charge
			
			if(game_board_array[new_x][new_y] != 0) return 0; //If the position to move to is not empty
			
			//THE REST OF THE GAME MECHANICS
			
			return 1;
		}

		@Override
		public void execute_move(int[] array_of_moves) {
			int old_x = array_of_moves[0];
			int old_y = array_of_moves[1];
			int new_x = array_of_moves[2];
			int new_y = array_of_moves[3];
			int direction_to_take = array_of_moves[4];
			
			game_board_array[old_x][old_y] = EMPTY;
			game_board_array[new_x][new_y] = players_turn;
			
			int compass_direction; //corresponds to 1-8 for direction of move with 1=north, 2=northeast, 3=east...
			if(old_x < new_x) { //East move		
				if(old_y < new_y) { //NORTH
					compass_direction = 2;
				} else if (old_y > new_y){
					compass_direction = 4;
				} else {
					compass_direction = 3;
				}
			} else if (old_x > new_x) { //West move
				if(old_y < new_y) { //NORTH
					compass_direction = 8;
				} else if (old_y > new_y){ //South
					compass_direction = 6;
				} else {
					compass_direction = 7;
				}
			} else { //NORTH OR SOUTH
				if(old_y < new_y) { //NORTH
					compass_direction = 1;
				} else {
					compass_direction = 5;
				}
			}
			
			erase_pieces(new_x, new_y, compass_direction, direction_to_take);			
		}

		@Override
		public void erase_pieces(int new_x, int new_y, int compass_direction, int direction_to_take) {
			if (direction_to_take == 0) return;
			
			if (compass_direction == 1) { //NORTH
				if (direction_to_take == 1) {//Forward take
					for(int i = new_y+1; i<5; i++) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_y-2; i>=0; i--) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				}
			} else if (compass_direction == 2) { //NORTHEAST
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NORTHEAST MOVE				
			} else if (compass_direction == 3) { //EAST
				if (direction_to_take == 1) {//Forward take
					for(int i = new_x+1; i<9; i++) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_x-2; i>=0; i--) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				}
			} else if (compass_direction == 4) { //SOUTHEAST
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! SOUTHEAST MOVE	
			} else if (compass_direction == 5) { //SOUTH
				if (direction_to_take == 1) {//Forward take
					for(int i = new_y-1; i>=0; i--) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_y+2; i<5; i++) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				}				
			} else if (compass_direction == 6) { //SOUTHWEST
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! SOUTHWEST MOVE	
			} else if (compass_direction == 7) { //WEST
				if (direction_to_take == 1) {//Forward take
					for(int i = new_x-1; i>=0; i--) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_x+2; i<9; i++) { //NORTHWEST
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn) {
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				}	
			} else {
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NORTHWEST MOVE	
			}
			
		}

		
		
		
	}

	public class Computer_Player extends Player{

		public Computer_Player(int player_number, int player_type) {
			who_am_i = player_number;
			what_am_i = player_type;
		}
		
		public int get_who_i_am() {return who_am_i;}
		public int get_what_i_am() {return what_am_i;}

		@Override
		public int players_turn() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int[] get_move() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int check_valid_move(int[] array_of_moves) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void execute_move(int[] array_of_moves) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void erase_pieces(int new_x, int new_y, int compass_direction,
				int direction_to_take) {
			// TODO Auto-generated method stub
			
		}

		
				

	}
	
	
	//-------------------------------GUI--------------------------------//
	class BoardGraphics extends Canvas {
		public BoardGraphics() {
			setSize(600,900);
			setBackground(Color.white);
		}	
		
		public void paint(Graphics g) {//Display board
			g.setColor(Color.gray);
	    	
	        for(int i = 0; i<5; i++) {
	        	for(int j = 0; j<9; j++) {
	        		if(game_board_array[i][j] == RED) {
	        			g.setColor(Color.red);
	        			g.fillOval(125+75*j, 100+75*i, 40, 40);
	        		} else if(game_board_array[i][j] == BLACK) {
	        			g.setColor(Color.black);
	        			g.fillOval(125+75*j, 100+75*i, 40, 40);
	        		} else {
	        			g.setColor(Color.gray);
	        			g.fillOval(125+75*j, 100+75*i, 40, 40);
	        		}	        		
	        	}	        		        
	        }	        
		}
	}
	
	public void create_window() {
		window = new JFrame("Fanorona"); 
	    
		window.setTitle("Fanorona Game");
		window.setBounds(100, 100, 900, 600);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		window.setVisible(true);
		window.add(graphics);
	
	}
		
}

