package fanorona.com;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

public class Board extends JPanel{
	//----------------------------GLOBAL_VARS--------------------------//
	static final int
	EMPTY = 0,
	BLACK = 1,	//PLAYER 1 is black
	RED = 2,	//PLAYER 2 is red
	SACRIFICED = 3;

	JFrame window;
	String game_message;
	BoardGraphics graphics;
	
	int game_number_type;
	Player player_1;
	Player player_2;
	int columns;
	int rows;
	public int[][] game_board_array; //Set up in a [x][y] coordinate with [0][0] being the bottom left
	int players_turn;
	int difficulty_level;
	
	int TEMP_PASS_COUNT;
	
	int extra_turn_flag;
	
	long starting_time;
	long turn_time_limit;
	
	
	//----------------------------MAIN_GAME----------------------------//	
	public Board(int number_type, int cols, int rows_, long time_limit, int diff) { //Constructor
		columns = cols;
		rows = rows_;
		turn_time_limit = time_limit;
		
		game_board_array = new int[columns][rows]; //[x][y] so [0-8][0-4] possible choices
		
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
		graphics = new BoardGraphics();
		setup_board();
		create_window();
		extra_turn_flag = 0;
		players_turn = 1;
		difficulty_level = diff;
		TEMP_PASS_COUNT = 0;
	}	
	
	public void setup_board() {
		for(int i = 0; i<columns; i++) {
			for(int j = 0; j<(rows/2); j++) {				
				game_board_array[i][j] = BLACK;
			}
		}
		
		for(int i = 0; i<columns; i++) {
			for(int j = (rows/2 + 1); j<rows; j++) {				
				game_board_array[i][j] = RED;
			}
		}
		
		for (int i=0; i<columns; ++i) {
			if (i == (columns/2)) {
				game_board_array[i][(rows/2)] = EMPTY;
			}
			else if (i<(columns/2) && i%2 == 0 || i>(columns/2) && i%2 == 1) {
				game_board_array[i][(rows/2)] = BLACK;
			}
			else {
				game_board_array[i][(rows/2)] = RED;
			}
		}
		
	}
	
	public int check_end_of_game() { //Returns 0 if fail, 1 if game over red wins, 2 if game over black wins
		int black_num = 0;
		int red_num = 0;
		
		for(int i = 0; i<columns; i++) {
			for(int j = 0; j<rows; j++) {				
				if(game_board_array[i][j] == RED) red_num++;
				if(game_board_array[i][j] == BLACK) black_num++;
			}
		}
		
		if(black_num == 0) return 1;
		else if(red_num == 0) return 2;
		else return 0;
	}	
	
	public int get_direction(int old_x, int old_y, int new_x, int new_y) {
		if(old_x < new_x) { //East move		
			if(old_y < new_y) { //South
				return 4;
			} else if (old_y > new_y){
				return 2;
			} else {
				return 3;
			}
		} else if (old_x > new_x) { //West move
			if(old_y < new_y) { //NORTH
				return 6;
			} else if (old_y > new_y){ //South
				return 8;
			} else {
				return 7;
			}
		} else { //NORTH OR SOUTH
			if(old_y < new_y) { //NORTH
				return 5;
			} else {
				return 1;
			}
		}
	}
	//---------------------------PLAYER_CLASS--------------------------//
	
	public abstract class Player {
		public int who_am_i; //Corresponds to board colors above in line ~10
		public int what_am_i; //Corresponds to human or comp. 0=human, 1=comp
		
		public int get_who_i_am() {return who_am_i;}
		public int get_what_i_am() {return what_am_i;}
		
		public abstract void execute_move(int[] array_of_moves);
		public int check_valid_move(int[] array_of_moves) {	 //Checks if moves in array_of_moves are valid, returns 1 if good, returns 0 is invalid
			int old_x = array_of_moves[0];
			int old_y = array_of_moves[1];
			int new_x = array_of_moves[2];
			int new_y = array_of_moves[3];
			int direction = array_of_moves[4];
			
			if(game_board_array[old_x][old_y] != players_turn) return 0; //Position clicked does not hold a piece of the player in charge
			
			if(game_board_array[new_x][new_y] != 0) return 0; //If the position to move to is not empty
			
			// players can only move 1 space at a time
			if (Math.abs(old_x-new_x) > 1 || Math.abs(old_y-new_y) > 1) return 0;
			
			// a player can move left/right as long there is nothing in their path
			if (new_y == old_y) {
				if (new_x > old_x) {
					for (int i=old_x+1; i<=new_x; ++i) {
						if (game_board_array[i][old_y] != EMPTY) {
							return 0;
						}
					}
				}
				else {
					for (int i=new_x; i<old_x; ++i) {
						if (game_board_array[i][old_y] != EMPTY) {
							return 0;
						}
					}
				}
			}
			
			// a player can move up or down as long there is nothing in their path 
			if (new_x == old_x) {
				if (new_y > old_y) {
					for (int i=old_y+1; i<=new_y; ++i) {
						if (game_board_array[old_x][i] != EMPTY) {
							return 0;
						}
					}
				}
				else {
					for (int i=new_y; i<old_y; ++i) {
						if (game_board_array[old_x][i] != EMPTY) {
							return 0;
						}
					}
				}
			}
			
			// if the player moves diagonally then they must move in a 45 degree angle
			// and no pieces must be in their path
			if (new_x < old_x && new_y < old_y) { // moving NW
				// can only move 90 degrees
				if ((old_x-new_x) != (old_y-new_y)) {
					return 0;
				}
				// check along the diagonal
				for (int i=new_x; i<old_x; ++i) {
					int j = new_y+(i-new_x);
					if (game_board_array[i][j] != EMPTY) {
						return 0;
					}
				}
			}
			if (new_x > old_x && new_y < old_y) { // moving NE
				// can only move 90 degrees
				if ((new_x-old_x) != (old_y-new_y)) {
					return 0;
				}
				// check along diagonal
				for (int i=old_x+1; i<=new_x; ++i) {
					int j = old_y-(i-old_x);
					if (game_board_array[i][j] != EMPTY) {
						return 0;
					}
				}
			}
			if (new_x < old_x && new_y > old_y) { // moving SW
				// can only move 90 degrees
				if ((old_x-new_x) != (new_y-old_y)) {
					return 0;
				}
				// check diagonal
				for (int i=new_x; i<old_x; ++i) {
					int j = new_y-(i-new_x);
					if (game_board_array[i][j] != EMPTY) {
						return 0;
					}
				}
			}
			if (new_x > old_x && new_y > old_y) { // moving SE
				// can only move 90 degrees
				if ((new_x-old_x) != (new_y-old_y)) {
					return 0;
				}
				// check diagonal
				for (int i=old_x+1; i<=new_x; ++i) {
					int j = (old_y+1)+(i-(old_x+1));
					if (game_board_array[i][j] != EMPTY) {
						return 0;
					}
				}
			}
			//THE REST OF THE GAME MECHANICS
			
			// yay it's valid
			return 1;
		}
		public void erase_pieces(int new_x, int new_y, int compass_direction, int direction_to_take) {
			extra_turn_flag = 0;
			
			if (direction_to_take == 0) return;
			
			if (compass_direction == 1) { //NORTH --DONE!!!
				if (direction_to_take == 1) {//Forward take					
					for(int i = new_y-1; i>=0; i--) {
						if(i<0) break;
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn
								&& game_board_array[new_x][i] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_y+2; i < rows; i++) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn
								&& game_board_array[new_x][i] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				}
			} else if (compass_direction == 2) { //NORTHEAST
				if (direction_to_take == 1) {
					for(int i = 1; i<columns; i++) {
						if(new_x+i < columns && new_y-i >= 0) {
							if(game_board_array[new_x+i][new_y-i] != EMPTY && game_board_array[new_x+i][new_y-i] != players_turn
									&& game_board_array[new_x+i][new_y-i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x+i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i<columns; i++) {
						if(new_x-i >= 0 && new_y+i < rows) {
							if(game_board_array[new_x-i][new_y+i] != EMPTY && game_board_array[new_x-i][new_y+i] != players_turn
									&& game_board_array[new_x-i][new_y+i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x-i][new_y+i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
				
			} else if (compass_direction == 3) { //EAST--DONE!!!
				if (direction_to_take == 1) {//Forward take					
					for(int i = new_x+1; i<columns; i++) {
						if(i<0) break;
						if(game_board_array[i][new_y] != EMPTY && game_board_array[i][new_y] != players_turn
								&& game_board_array[i][new_y] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[i][new_y] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_x-2; i >=0; i--) {
						if(game_board_array[i][new_y] != EMPTY && game_board_array[i][new_y] != players_turn
								&& game_board_array[i][new_y] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[i][new_y] = EMPTY;
						} else {
							break;
						}
					}
				}
			} else if (compass_direction == 4) { //SOUTHEAST
				if (direction_to_take == 1) {
					for(int i = 1; i<columns; i++) {
						if(new_x+i < columns && new_y+i < rows) {
							if(game_board_array[new_x+i][new_y+i] != EMPTY && game_board_array[new_x+i][new_y+i] != players_turn
									&& game_board_array[new_x+i][new_y+i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x+i][new_y+i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i<columns; i++) {
						if(new_x-i >= 0 && new_y-i >= 0) {
							if(game_board_array[new_x-i][new_y-i] != EMPTY && game_board_array[new_x-i][new_y-i] != players_turn
									&& game_board_array[new_x-i][new_y-i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x-i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}	
			} else if (compass_direction == 5) { //SOUTH--DONE!!!
				if (direction_to_take == 1) {//Forward take					
					for(int i = new_y+1; i<rows; i++) {
						if(i<0) break;
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn
								&& game_board_array[new_x][i] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_y-2; i >=0; i--) {
						if(game_board_array[new_x][i] != EMPTY && game_board_array[new_x][i] != players_turn
								&& game_board_array[new_x][i] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[new_x][i] = EMPTY;
						} else {
							break;
						}
					}
				}				
			} else if (compass_direction == 6) { //SOUTHWEST
				if (direction_to_take == 1) {
					for(int i = 1; i<columns; i++) {
						if(new_x-i >= 0 && new_y+i < rows) {
							if(game_board_array[new_x-i][new_y+i] != EMPTY && game_board_array[new_x-i][new_y+i] != players_turn
									&& game_board_array[new_x-i][new_y+i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x-i][new_y+i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i<columns; i++) {
						if(new_x+i < columns && new_y-i >= 0) {
							if(game_board_array[new_x+i][new_y-i] != EMPTY && game_board_array[new_x+i][new_y-i] != players_turn
									&& game_board_array[new_x+i][new_y-i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x+i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}	
			} else if (compass_direction == 7) { //WEST--DONE!!!
				if (direction_to_take == 1) {//Forward take					
					for(int i = new_x-1; i>=0; i--) {
						if(i<0) break;
						if(game_board_array[i][new_y] != EMPTY && game_board_array[i][new_y] != players_turn
								&& game_board_array[i][new_y] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[i][new_y] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = new_x+2; i <columns; i++) {
						if(game_board_array[i][new_y] != EMPTY && game_board_array[i][new_y] != players_turn
								&& game_board_array[i][new_y] != SACRIFICED) {
							extra_turn_flag = 1;
							game_board_array[i][new_y] = EMPTY;
						} else {
							break;
						}
					}
				}		
			} else {//NORTHWEST
				if (direction_to_take == 1) {
					for(int i = 1; i<columns; i++) {
						if(new_x-i >= 0 && new_y-i >= 0) {
							if(game_board_array[new_x-i][new_y-i] != EMPTY && game_board_array[new_x-i][new_y-i] != players_turn
									&& game_board_array[new_x-i][new_y-i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x-i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else { 
					for(int i = 2; i<columns; i++) {
						if(new_x+i < columns && new_y+i < rows) {
							if(game_board_array[new_x+i][new_y+i] != EMPTY && game_board_array[new_x+i][new_y+i] != players_turn
									&& game_board_array[new_x+i][new_y+i] != SACRIFICED) {
								extra_turn_flag = 1;
								game_board_array[new_x+i][new_y+i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}	
			}
			
		}
	}
	
	public class Human_Player extends Player{
		
		public Human_Player(int player_number, int player_type) {
			who_am_i = player_number;
			what_am_i = player_type;
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
				if(old_y < new_y) { //South
					compass_direction = 4;
				} else if (old_y > new_y){ // NE move
					compass_direction = 2;
				} else { //E move
					compass_direction = 3;
				}
			} else if (old_x > new_x) { //West move
				if(old_y < new_y) { //NORTH
					compass_direction = 6; // SW move
				} else if (old_y > new_y){ //South
					compass_direction = 8; // NW move
				} else {
					compass_direction = 7; // W move
				}
			} else { //NORTH OR SOUTH
				if(old_y < new_y) { //NORTH
					compass_direction = 5; // South
				} else {
					compass_direction = 1; // North
				}
			}
			
			erase_pieces(new_x, new_y, compass_direction, direction_to_take);			
		}		
		
	}

	public class Computer_Player extends Player{

		public Computer_Player(int player_number, int player_type) {
			who_am_i = player_number;
			what_am_i = player_type;
		}

		private int board_state_evaluator(int[][] board_to_eval) { //Looks at the board and assigns it a value based on players_turn
			int red_pieces = 0;
			int black_pieces = 0;
			
			for( int i = 0; i < columns; i++ ) {
				for (int j = 0; j < rows; j++) {
					if (board_to_eval[i][j] == BLACK) black_pieces++;
					else if (board_to_eval[i][j] == RED) red_pieces++;
				}
			}
			if (players_turn == BLACK && black_pieces == 0) { //Loss for black
				return (-10000);
			} else if (players_turn == BLACK && red_pieces == 0) { //Win for black
				return (10000);
			} else if (players_turn == RED && black_pieces == 0) { //Win for red
				return (10000);
			} else if (players_turn == RED && red_pieces == 0) { //Loss for red
				return (-10000);
			}
			
			if (players_turn == BLACK) {
				return ((12*black_pieces)-(10*red_pieces));
			} else {
				return ((12*red_pieces)-(10*black_pieces));
			}
			
		}
		
		private int[][] execute_a_move(int[][] current_board, int[] move_list, int whose_turn) { //Create a new board based on a possible move occurring
			int[][] resulting_board = new int[columns][rows];

			for(int i = 0; i<columns; i++) {
				for(int j = 0; j<rows; j++) {
					resulting_board[i][j] = current_board[i][j];
				}
			}
			
			int old_x = move_list[0];
			int old_y = move_list[1];
			int new_x = move_list[2];
			int new_y = move_list[3];
			int direction = move_list[4]; //forward or back
			
			resulting_board[old_x][old_y] = EMPTY;
			resulting_board[new_x][new_y] = whose_turn;
			
			int direction_x = new_x-old_x;
			int direction_y = new_y-old_y;
			
			for(int i = 1; i < 10; i++) {
				if (direction == 1) { //forward
				if( (new_x+(direction_x*i)) >= 0 && (new_x+(direction_x*i)) < columns && (new_y+(direction_y*i)) >= 0 && (new_y+(direction_y*i)) < rows) { //still on board
					if ( resulting_board[new_x+(direction_x*i)][new_y+(direction_y*i)] != whose_turn && resulting_board[new_x+(direction_x*i)][new_y+(direction_y*i)] != EMPTY) {
						resulting_board[new_x+(direction_x*i)][new_y+(direction_y*i)] = EMPTY;
					} else {
						break;
					}
				} else {
					break;
				}
				} else { //backward
					if( (old_x-(direction_x*i)) >= 0 && (old_x-(direction_x*i)) < columns && (old_y-(direction_y*i)) >= 0 && (old_y-(direction_y*i)) < rows) { //still on board
						if ( resulting_board[old_x-((direction_x*i))][old_y-((direction_y*i))] != whose_turn && resulting_board[old_x-((direction_x*i))][old_y-((direction_y*i))] != EMPTY) {
							resulting_board[old_x-((direction_x*i))][old_y-((direction_y*i))] = EMPTY;
						} else {
							break;
						}
					} else {
						break;
					}				
				}
			}
			
			
			return resulting_board;
		}
		
		private int[] list_possible_moves(int[][] current_board, int whose_turn, int level_on_tree) {
			ArrayList<int[]> list_of_moves = new ArrayList<int[]>(); //Each "move" is 0=old_x, 1=old_y, 2=new_x, 3=new_y, 4=direction to take, 5=resulting board value
			ArrayList<int[][]> board_holder = new ArrayList<int[][]>();
					
			
			ArrayList<int[]> taking_moves = new ArrayList<int[]>(); //If a move can take a piece, it goes into this list as well.
			int taking_move_exists = 0; //If no taking move exists, then it stays 0, if one does exist, then 1
			for(int i = 0; i<columns; i++) { //Iterate through whole array
				for(int j = 0; j<rows; j++) {
					if(current_board[i][j] == whose_turn) { //Is this my piece? Yes
						
						//North South East and West moves
						
						if (j+1 < rows) {
						if (current_board[i+0][j+1] == EMPTY) { //South of location
							list_of_moves.add(new int[]{i,j,i+0,j+1,1,0});
							
							if(j+2 < rows && current_board[i+0][j+2] != whose_turn && current_board[i+0][j+2] != EMPTY) { //A taking move
								taking_moves.add(new int[]{i,j,i+0,j+1,1,0});
								taking_move_exists = 1;
							}
							
							if (j-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
								if (current_board[i+0][j-1] != EMPTY && current_board[i+0][j-1] != whose_turn) {
									taking_moves.add(new int[]{i,j,i+0,j+1,2,0});
									taking_move_exists = 1;
								}
							}
						}
						} 
						
						if (i+1 < columns) {
						if (current_board[i+1][j+0] == EMPTY) { //East of location
							list_of_moves.add(new int[]{i,j,i+1,j+0,1,0});
							
							if(i+2 < columns && current_board[i+2][j+0] != whose_turn && current_board[i+2][j+0] != EMPTY) { //A taking move
								taking_moves.add(new int[]{i,j,i+1,j+0,1,0});
								taking_move_exists = 1;
							}
							
							if (i-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
								if (current_board[i-1][j+0] != EMPTY && current_board[i-1][j+0] != whose_turn) {
									taking_moves.add(new int[]{i,j,i+1,j+0,2,0});
									taking_move_exists = 1;
								}
							}
						} 
						}
						
						if (j-1 >= 0) {
						if (current_board[i+0][j-1] == EMPTY) { //North of location
							list_of_moves.add(new int[]{i,j,i+0,j-1,1,0});
							
							if(j-2 >= 0 && current_board[i+0][j-2] != whose_turn && current_board[i+0][j-2] != EMPTY) { //A taking move
								taking_moves.add(new int[]{i,j,i+0,j-1,1,0});
								taking_move_exists = 1;
							}
							
							if (j+1 < rows){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
								if (current_board[i+0][j+1] != EMPTY && current_board[i+0][j+1] != whose_turn) {
									taking_moves.add(new int[]{i,j,i+0,j-1,2,0});
									taking_move_exists = 1;
								}
							}
						} 
						}
						if (i-1 >= 0) {
						if (current_board[i-1][j+0] == EMPTY) { //West of location
							list_of_moves.add(new int[]{i,j,i-1,j+0,1,0});
							
							if(i-2 >= 0 && current_board[i-2][j+0] != whose_turn && current_board[i-2][j+0] != EMPTY) { //A taking move
								taking_moves.add(new int[]{i,j,i-1,j+0,1,0});
								taking_move_exists = 1;
							}
							
							if (i+1 < columns){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
								if (current_board[i+1][j+0] != EMPTY && current_board[i+1][j+0] != whose_turn) {
									taking_moves.add(new int[]{i,j,i-1,j+0,2,0});
									taking_move_exists = 1;
								}
							}
						} 
						}
						
						
						if((i+j)%2 == 0) { //If piece can move 8 compass directions, fill in NE SE NW SW							
							if (i+1 < columns && j+1 < rows) {
							if (current_board[i+1][j+1] == EMPTY) { //Southeast of location
								list_of_moves.add(new int[]{i,j,i+1,j+1,1,0});
								
								if(j+2 < rows && i+2 < columns && current_board[i+2][j+2] != whose_turn && current_board[i+2][j+2] != EMPTY) { //A taking move
									taking_moves.add(new int[]{i,j,i+1,j+1,1,0});
									taking_move_exists = 1;
								}
								
								if (j-1 >= 0 && i-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (current_board[i-1][j-1] != EMPTY && current_board[i-1][j-1] != whose_turn) {
										taking_moves.add(new int[]{i,j,i+1,j+1,2,0});
										taking_move_exists = 1;
									}
								}
							} 
							}							
							if (i+1 < columns && j-1 >= 0) {
							if (current_board[i+1][j-1] == EMPTY) { //Northeast of location
								list_of_moves.add(new int[]{i,j,i+1,j-1,1,0});
								
								if(i+2 < columns && j-2 >= 0 && current_board[i+2][j-2] != whose_turn && current_board[i+2][j-2] != EMPTY) { //A taking move
									taking_moves.add(new int[]{i,j,i+1,j-1,1,0});
									taking_move_exists = 1;
								}
								
								if (i-1 >= 0 && j+1 < rows){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (current_board[i-1][j+1] != EMPTY && current_board[i-1][j+1] != whose_turn) {
										taking_moves.add(new int[]{i,j,i+1,j-1,2,0});
										taking_move_exists = 1;
									}
								}
							} 
							}							
							if (i-1 >= 0 && j-1 >= 0) {
							if (current_board[i-1][j-1] == EMPTY) { //Northwest of location				
								list_of_moves.add(new int[]{i,j,i-1,j-1,1,0});
								
								if(i-2 >= 0 && j-2 >= 0 && current_board[i-2][j-2] != whose_turn && current_board[i-2][j-2] != EMPTY) { //A taking move
									taking_moves.add(new int[]{i,j,i-1,j-1,1,0});
									taking_move_exists = 1;
								}
								
								if (i+1 < columns && j+1 < rows){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (current_board[i+1][j+1] != EMPTY && current_board[i+1][j+1] != whose_turn) {
										taking_moves.add(new int[]{i,j,i-1,j-1,2,0});
										taking_move_exists = 1;
									}
								}
							} 
							}							
							if (i-1 >= 0 && j+1 < rows) {
							if (current_board[i-1][j+1] == EMPTY) { //Southwest of location
								list_of_moves.add(new int[]{i,j,i-1,j+1,1,0});
								
								if(i-2 >= 0 &j+2 < rows && current_board[i-2][j+2] != whose_turn && current_board[i-2][j+2] != EMPTY) { //A taking move
									taking_moves.add(new int[]{i,j,i-1,j+1,1,0});
									taking_move_exists = 1;
								}
								
								if (i+1 < columns && j-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (current_board[i+1][j-1] != EMPTY && current_board[i+1][j-1] != whose_turn) {
										taking_moves.add(new int[]{i,j,i-1,j+1,2,0});
										taking_move_exists = 1;
									}
								}
							} 
							}
						} 
					}
				}
			}
						//-------------------FROM HERE UP WORKS FINE------------
			if(taking_move_exists == 1) {
				list_of_moves = taking_moves;
			}
			
			for(int i = 0; i<list_of_moves.size(); i++) {
				int[] current_move_info = new int[6];
				current_move_info = list_of_moves.get(i);
				int [][] resulting_board_ = new int[columns][rows];
				resulting_board_ = execute_a_move(current_board, current_move_info, whose_turn);
				board_holder.add(resulting_board_);
				TEMP_PASS_COUNT++;
				current_move_info[5] = board_state_evaluator(board_holder.get(i));
				if (current_move_info[5] > 2000 || current_move_info[5] < -2000) {
					return current_move_info; //Win condition or lose condition
				}
				list_of_moves.set(i, current_move_info);
			}
			
			
			
			if(level_on_tree < difficulty_level){ //Does the tree level match the difficulty level? 
				for(int i = 0; i<board_holder.size(); i++) {
					int[] result_of_deeper = new int[6];
					int[] current_best = new int[6];
					if(whose_turn == 1) {
						current_best = list_of_moves.get(i);
						result_of_deeper = list_possible_moves(board_holder.get(i), 2, level_on_tree+1);	
							
						current_best[5] = result_of_deeper[5];
						list_of_moves.set(i, current_best); //Switch the values!! not move information
						
					} else {
						current_best = list_of_moves.get(i);
						result_of_deeper = list_possible_moves(board_holder.get(i), 1, level_on_tree+1);
						
						current_best[5] = result_of_deeper[5];
						list_of_moves.set(i, current_best);
						
					}
				}
			}
			//Highest value passed back if players_turn = whose turn, else we want smallest move
			
			
			
			if (whose_turn == players_turn) {
				int highest = -10000;
				int place = 0;
				for(int i = 0; i<list_of_moves.size(); i++) {
					int[] current_move_info = list_of_moves.get(i);
					if (current_move_info[5] == 10000) return list_of_moves.get(i);
					if(current_move_info[5] >= highest) {
						highest = current_move_info[5];
						place = i;
					}
				}
				return list_of_moves.get(place);
			} else {
				int lowest = 10000;
				int place = 0;
				for(int i = 0; i<list_of_moves.size(); i++) {
					int[] current_move_info = list_of_moves.get(i);
					if (current_move_info[5] == -10000) return list_of_moves.get(i);
					if(current_move_info[5] <= lowest) {
						lowest = current_move_info[5];
						place = i;
					}
				}				
				return list_of_moves.get(place);
			}			
		}
		
				@Override
		public void execute_move(int[] array_of_moves) {
			TEMP_PASS_COUNT = 0;

			int[] results_of_search = new int[5];
			
			results_of_search = list_possible_moves(game_board_array, this.get_who_i_am(), 1); //Looks at current gameboard, passes it computers color, and starts at tree level 1
									
			int old_x = results_of_search[0];
			int old_y = results_of_search[1];
			int new_x = results_of_search[2];
			int new_y = results_of_search[3];
			int direction_to_take = results_of_search[4];
			
			game_board_array[old_x][old_y] = EMPTY;
			game_board_array[new_x][new_y] = players_turn;
			
			int compass_direction; //corresponds to 1-8 for direction of move with 1=north, 2=northeast, 3=east...
			if(old_x < new_x) { //East move		
				if(old_y < new_y) { //South
					compass_direction = 4;
				} else if (old_y > new_y){
					compass_direction = 2;
				} else {
					compass_direction = 3;
				}
			} else if (old_x > new_x) { //West move
				if(old_y < new_y) { //NORTH
					compass_direction = 6;
				} else if (old_y > new_y){ //South
					compass_direction = 8;
				} else {
					compass_direction = 7;
				}
			} else { //NORTH OR SOUTH
				if(old_y < new_y) { //NORTH
					compass_direction = 5;
				} else {
					compass_direction = 1;
				}
			}
			erase_pieces(new_x, new_y, compass_direction, direction_to_take);	
		}

	}	
	
	//-------------------------------GUI by Blake--------------------------------//
		class BoardGraphics extends JPanel implements ActionListener, MouseListener {
			int width = 950;
			int height = 850;
			int piece_diameter = 30;
			int x_offset = piece_diameter;
			int y_offset = piece_diameter;
			int click_counter;
			int move_counter;
			int minutes;
			int seconds;
			boolean is_game_over;
			
			// is it the players first time moving a piece
			// on their turn
			boolean is_first_move;
			
			long begining_of_move;
			
			// 0=old_x, 1=old_y, 2=new_x, 3=new_y, 4=forward or backwards(2=back, 1=forward, 0 if no take)
			int[] move;
			
			// 0=flag (1 yes this player has a sacrificed piece on  the board, 0 otherwise)
			// 1=what the move counter will need to be in order to remove the piece
			// 2=x-cord on the board where the piece is
			// 3=y-cord on the board where the piece is
			int[] player_1_sac_move;
			int[] player_2_sac_move;
			
			// here we will store the previous locations that a piece has been
			// to within the same turn (cannot be at the same location twice)
			// also we cannot move in the same direction twice
			// 0=no prev direction, 1 = N, 2 = NE, 3 = W, 4 = SW, etc..
			ArrayList<Integer> prev_locations;
			int prev_direction;
			
			
			public BoardGraphics() {
				if (player_1.get_what_i_am() == 1) {
					game_message = "Welcome to Fanorona! CPU Player One's turn, click anywhere to execute it's move";
				}
				else {
					game_message = "Welcome to Fanorona! Player 1 (Black) will go first, please select a piece to move";
				}
				click_counter = 0;
				move_counter = 0;
				move = new int[5];
				player_1_sac_move = new int[4];
				player_2_sac_move = new int[4];
				player_1_sac_move[0] = 0;
				player_2_sac_move[0] = 0;
				setSize(height,width);
				setBackground(Color.white);
				is_game_over = false;
				is_first_move = true;
				begining_of_move = new Date().getTime();
				prev_locations = new ArrayList<Integer>();
				prev_direction = -1;
			}	
			
			// this function will check to see if there is a valid taking move
			// on the board for a player
			public boolean is_taking_move_on_board() {
				// if it's not the players first move in their turn then
				// this function doesn't even matter
				if (!is_first_move) {
					return false;
				}
				
				int whose_turn = players_turn;
				// now we check the game board array to see
				// if the player has a valid move
				for(int i = 0; i<columns; i++) { //Iterate through whole array
					for(int j = 0; j<rows; j++) {
						if(game_board_array[i][j] == whose_turn) { //Is this my piece? Yes
							
							//North South East and West moves
							
							if (j+1 < rows) {
							if (game_board_array[i+0][j+1] == EMPTY) { //South of location
								if(j+2 < rows && game_board_array[i+0][j+2] != whose_turn && game_board_array[i+0][j+2] != EMPTY) { //A taking move
									return true;
								}
								
								if (j-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (game_board_array[i+0][j-1] != EMPTY && game_board_array[i+0][j-1] != whose_turn) {
										return true;
									}
								}
							}
							} 
							
							if (i+1 < columns) {
							if (game_board_array[i+1][j+0] == EMPTY) { //East of location
								if(i+2 < columns && game_board_array[i+2][j+0] != whose_turn && game_board_array[i+2][j+0] != EMPTY) { //A taking move
									return true;
								}
								
								if (i-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (game_board_array[i-1][j+0] != EMPTY && game_board_array[i-1][j+0] != whose_turn) {
										return true;
									}
								}
							} 
							}
							
							if (j-1 >= 0) {
							if (game_board_array[i+0][j-1] == EMPTY) { //North of location
								if(j-2 >= 0 && game_board_array[i+0][j-2] != whose_turn && game_board_array[i+0][j-2] != EMPTY) { //A taking move
									return true;
								}
								
								if (j+1 < rows){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (game_board_array[i+0][j+1] != EMPTY && game_board_array[i+0][j+1] != whose_turn) {
										return true;
									}
								}
							} 
							}
							if (i-1 >= 0) {
							if (game_board_array[i-1][j+0] == EMPTY) { //West of location
								if(i-2 >= 0 && game_board_array[i-2][j+0] != whose_turn && game_board_array[i-2][j+0] != EMPTY) { //A taking move
									return true;
								}
								
								if (i+1 < columns){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
									if (game_board_array[i+1][j+0] != EMPTY && game_board_array[i+1][j+0] != whose_turn) {
										return true;
									}
								}
							} 
							}
							
							
							if((i+j)%2 == 0) { //If piece can move 8 compass directions, fill in NE SE NW SW							
								if (i+1 < columns && j+1 < rows) {
								if (game_board_array[i+1][j+1] == EMPTY) { //Southeast of location
									if(j+2 < rows && i+2 < columns && game_board_array[i+2][j+2] != whose_turn && game_board_array[i+2][j+2] != EMPTY) { //A taking move
										return true;
									}
									
									if (j-1 >= 0 && i-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
										if (game_board_array[i-1][j-1] != EMPTY && game_board_array[i-1][j-1] != whose_turn) {
											return true;
										}
									}
								} 
								}							
								if (i+1 < columns && j-1 >= 0) {
								if (game_board_array[i+1][j-1] == EMPTY) { //Northeast of location								
									if(i+2 < columns && j-2 >= 0 && game_board_array[i+2][j-2] != whose_turn && game_board_array[i+2][j-2] != EMPTY) { //A taking move
										return true;
									}
									
									if (i-1 >= 0 && j+1 < rows){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
										if (game_board_array[i-1][j+1] != EMPTY && game_board_array[i-1][j+1] != whose_turn) {
											return true;
										}
									}
								} 
								}							
								if (i-1 >= 0 && j-1 >= 0) {
								if (game_board_array[i-1][j-1] == EMPTY) { //Northwest of location								
									if(i-2 >= 0 && j-2 >= 0 && game_board_array[i-2][j-2] != whose_turn && game_board_array[i-2][j-2] != EMPTY) { //A taking move
										return true;
									}
									
									if (i+1 < columns && j+1 < rows){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
										if (game_board_array[i+1][j+1] != EMPTY && game_board_array[i+1][j+1] != whose_turn) {
											return true;
										}
									}
								} 
								}							
								if (i-1 >= 0 && j+1 < rows) {
								if (game_board_array[i-1][j+1] == EMPTY) { //Southwest of location
									if(i-2 >= 0 &j+2 < rows && game_board_array[i-2][j+2] != whose_turn && game_board_array[i-2][j+2] != EMPTY) { //A taking move
										return true;
									}
									
									if (i+1 < columns && j-1 >= 0){ //Checks if a back take will even take a piece. If not, then it is not necessary to add
										if (game_board_array[i+1][j-1] != EMPTY && game_board_array[i+1][j-1] != whose_turn) {
											return true;
										}
									}
								} 
							}
							}
						}
					}
				}
				return false;
			}
			
			public void processClick(int x, int y) {
				
				// if the game is over or the screen is frozen, the click does nothing
				if (is_game_over || click_counter == -1) {
					return;
				}
				
				// check move count, if we are over then the game is a draw
				if (move_counter > (10*rows)) {
					is_game_over = true;
					game_message = "Reached max number of moves, the game is a draw!";
					repaint();
					return;
				}
				
				// if the game is over we only want to display the message
				// and then exit the function
				if (check_end_of_game() != 0) {
					if (check_end_of_game() == 1) {
						// player 1 has won the game yay
						game_message = "Red is the winner! This game lasted "+minutes+" minutes and "+seconds+" seconds";
					}
					else {
						//player 2 won yay
						game_message = "Black has won the game! This game lasted "+minutes+" minutes and "+seconds+" seconds";
					}
					is_game_over = true;
					repaint();
					return;
				}
				
				// if its a CPU's turn we need to execute its move
				if (players_turn == 1 && player_1.get_what_i_am() == 1 && click_counter == 0) {
					// "freeze" screen while CPU is making a move
					click_counter = -1;
					
					// this message was originally going to tell the user the CPU is processing a move
					// but it would always execute the move before repainting the board
					// unless we had a Java exception thrown. So basically this message will be displayed
					// when something bad happens (hopefully it won't)
					game_message = "Something bad has happened! Please close the application.";
					repaint();
					
					int[] passing = new int[4];
					passing[0] = players_turn;
					player_1.execute_move(passing);
					move_counter++;
					extra_turn_flag = 0;
					players_turn = 2;
					if (player_2.get_what_i_am() == 1) {
						game_message = "CPU Player Two's turn, click anywhere to execute it's move";
					}
					else {
						game_message = "Player 2 (Red) please select a piece to move";
					}
					begining_of_move = new Date().getTime();
					click_counter = 0;
					is_first_move = true;
					repaint();
					return;
				}
				if (players_turn == 2 && player_2.get_what_i_am() == 1 && click_counter == 0) {
					// "freeze" screen while CPU is making a move
					click_counter = -1;
					
					// this message was originally going to tell the user the CPU is processing a move
					// but it would always execute the move before repainting the board
					// unless we had a Java exception thrown. So basically this message will be displayed
					// when something bad happens (hopefully it won't)
					game_message = "Something bad has happened! Please close the application.";
					repaint();
					
					int[] passing = new int[4];
					passing[0] = players_turn;
					player_2.execute_move(passing);
					move_counter++;
					extra_turn_flag = 0;
					players_turn = 1;
					if (player_1.get_what_i_am() == 1) {
						game_message = "CPU Player One's turn, click anywhere to execute it's move";
					}
					else {
						game_message = "Player 1 (Black) please select a piece to move";
					}
					begining_of_move = new Date().getTime();
					click_counter = 0;
					is_first_move = true;
					repaint();
					return;
				}
				
				
				// if they click the pass button change players
				// bounds off pass button: (x_offset+columns*piece_diameter*2, (rows*piece_diameter), 75, 25)
				if (click_counter == 1 && extra_turn_flag == 1 &&
					x > x_offset+columns*piece_diameter*2 && x < x_offset+columns*piece_diameter*2+75
					&& y > rows*piece_diameter && y < rows*piece_diameter+25) {
					if (players_turn == 1) {
						players_turn = 2;
						extra_turn_flag = 0;
						if (player_2.get_what_i_am() == 1) {
							game_message = "CPU Player Two's turn, click anywhere to execute it's move";
						}
						else {
							game_message = "Player 2 (Red) select a piece to move";
						}
					}
					else {
						players_turn = 1;
						extra_turn_flag = 0;
						if (player_2.get_what_i_am() == 1) {
							game_message = "CPU Player One's turn, click anywhere to execute it's move";
						}
						else {
							game_message = "Player 1 (Black) select a piece to move";
						}
					}
					prev_locations.clear();
					prev_direction = -1;
					click_counter = 0;
					is_first_move = true;
					move_counter++;
					begining_of_move = new Date().getTime();
					repaint();
					return;
				}
				
				// if the sacrifice button is pressed we want to sacrifice the selected piece
				// button bounds: x_offset+columns*piece_diameter*2, (rows*piece_diameter+piece_diameter*2), 75, 25
				if (click_counter ==  1 && extra_turn_flag == 0
						&& x > x_offset+columns*piece_diameter*2 && x < x_offset+columns*piece_diameter*2+75
						&& y > rows*piece_diameter && y < rows*piece_diameter+25) {
					game_board_array[move[0]][move[1]] = SACRIFICED;
					if (players_turn == 1) {
						player_1_sac_move[0] = 1;
						player_1_sac_move[1] = move_counter+2;
						player_1_sac_move[2] = move[0];
						player_1_sac_move[3] = move[1];
						players_turn = 2;
						extra_turn_flag = 0;
						if (player_2.get_what_i_am() == 1) {
							game_message = "CPU Player Two's turn, click anywhere to execute it's move";
						}
						else {
							game_message = "Player 2 (Red) select a piece to move";
						}
					}
					else {
						player_2_sac_move[0] = 1;
						player_2_sac_move[1] = move_counter+2;
						player_2_sac_move[2] = move[0];
						player_2_sac_move[3] = move[1];
						players_turn = 1;
						extra_turn_flag = 0;
						if (player_2.get_what_i_am() == 1) {
							game_message = "CPU Player One's turn, click anywhere to execute it's move";
						}
						else {
							game_message = "Player 1 (Black) select a piece to move";
						}
					}
					prev_locations.clear();
					click_counter = 0;
					is_first_move = true;
					move_counter++;
					begining_of_move = new Date().getTime();
					repaint();
					return;
					
				}
				
				// click counter == 2 implies the user has select which direction to take
				// if this is a direction selection click we only want to
				// take a click inside the "buttons"
				if (click_counter == 2) {
					// first lets make sure they haven't gone over the time limit
					if ((new Date().getTime() - begining_of_move ) > turn_time_limit) {
						if (players_turn == 1) {
							game_message = "Player One's turn took too much time! Player 2 Wins!";
						}
						else {
							game_message = "Player Two's turn took too much time! Player 1 Wins!";
						}
						is_game_over = true;
						repaint();
						return;
					}
					
					// next lets make sure the player is not trying to move in the same direction if
					// they took pieces previously
					if ((get_direction(move[0], move[1], move[2], move[3]) == prev_direction)
							|| (get_direction(move[0], move[1], move[2], move[3]) == prev_direction%4)
							|| (get_direction(move[0], move[1], move[2], move[3]) == 0 && prev_direction == 4)
							|| (get_direction(move[0], move[1], move[2], move[3]) == 4 && prev_direction == 0)) {
						game_message = "You cannot move in the same direction! Please select a different direction or pass";
						click_counter = 1;
						repaint();
						return;
					}
					
					// we also need to make sure they are not visiting a spot previously visited
					for (int i=0; i<prev_locations.size()-1; ++i) {
						if (i%2 == 0 && move[2] == prev_locations.get(i)) {
							if (move[3] == prev_locations.get(i+1)) {
								game_message = "You've already visited ("+move[2]+","+move[3]+")! Please select another location to move to";
								click_counter = 1;
								repaint();
								return;
							}
						}
					}
					
					/* Now we find out which button they clicked (forward, backward, no take)
					 * bounds for buttons:
					 * g.drawRect(x_offset+columns*piece_diameter*2, y_offset, 75, 25);
		        	 * g.drawRect(x_offset+columns*piece_diameter*2, y_offset+piece_diameter*2, 75, 25);
		        	 * g.drawRect(x_offset+columns*piece_diameter*2, y_offset+piece_diameter*4, 75, 25);
		        	 */
					if (x > x_offset+columns*piece_diameter*2 && x < x_offset+columns*piece_diameter*2+75
						&& y > y_offset && y < y_offset+25) { //forward
						move[4] = 1;
						click_counter = 0;
					}
					else if (x > x_offset+columns*piece_diameter*2 && x < x_offset+columns*piece_diameter*2+75
							&& y > y_offset+piece_diameter*2 && y < y_offset+piece_diameter*2+25) { // backwards
						move[4] = 2;
						click_counter = 0;
					}
					else if (x > x_offset+columns*piece_diameter*2 && x < x_offset+columns*piece_diameter*2+75
							&& y > y_offset+piece_diameter*4 && y < y_offset+piece_diameter*4+25) { // no take
						move[4] = 0;
						click_counter = 0;
					}
					else {
						game_message = "Please select a direction to take.";
						repaint();
						return;
					}
					
					// now we take a snapshot of the current board
					// because we made need to restore it after
					// executing the move
					int[][] prev_board_state = new int[columns][rows];
					for (int i=0; i<columns; i++) {
						for (int j=0; j<rows; ++j) {
							prev_board_state[i][j] = game_board_array[i][j];
						}
					}
					
					// check is the player had a taking move on the current board
					// (before executing the move)
					boolean taking_move_on_board = is_taking_move_on_board();
					
					// execute the move for either player 1 or player 2
					if (players_turn == 1) {
						if (player_1.check_valid_move(move) == 1) {
							player_1.execute_move(move);
							// check if the player gets an extra turn
							if (extra_turn_flag == 1) {
								// add location to location array
								prev_locations.add(move[2]);
								prev_locations.add(move[3]);
								// set previous direction
								prev_direction = get_direction(move[0], move[1], move[2], move[3]);
								// the player took some pieces they get to go again
								game_message = "Player 1 (Black) select where to move ("+move[0]+","+move[1]+") or pass";
								move[0] = move[2];
								move[1] = move[3];
								click_counter = 1;
								is_first_move = false;
							}
							else {
								// if there was a taking move on board and it was the players first
								// move in their turn (this is also checked in is_taking_move_on_board()
								// and they chose not to take any pieces we
								// yell at them and restore the board and tell them to start over
								if (taking_move_on_board) {
									// restore board
									for (int i=0; i<columns; i++) {
										for (int j=0; j<rows; ++j) {
											game_board_array[i][j] = prev_board_state[i][j];
										}
									}
									game_message = "Player 1 must make a taking move! Please select a piece to move";
									click_counter = 0;
									repaint();
									return;
								}
								else {
									// else change players
									prev_locations.clear();
									prev_direction = -1;
									players_turn = 2;
									//check if player 2 is a CPU
									if (player_2.get_what_i_am() == 1) {
										game_message = "CPU Player Two's turn, click anywhere to execute it's move";
									}
									else {
										game_message = "Player 2 (Red) select a piece to move";
									}
									extra_turn_flag = 0;
									is_first_move = true;
									begining_of_move = new Date().getTime();
									move_counter++;
								}
							}
							repaint();
							return;
						}
						else {
							if (extra_turn_flag == 1) {
								// the player took some pieces but then made invalid move
								game_message = "Invalid move! Player 1 (Black) select where to move ("+move[0]+","+move[1]+") or pass";
								click_counter = 1;
							}
							else {
								game_message = "That is not a valid move! Player 1 (Black) select a peice to move";
							}
							repaint();
							return;
						}
					}
					else {
						if (player_2.check_valid_move(move) == 1) {
							player_2.execute_move(move);
							// else its player one's turn
							if (extra_turn_flag == 1) {
								// add location to location array
								prev_locations.add(move[2]);
								prev_locations.add(move[3]);
								//set previous direction
								prev_direction = get_direction(move[0], move[1], move[2], move[3]);
								// the player took some pieces they get to go again
								game_message = "Player 2 (Red) select where to move ("+move[2]+","+move[3]+") or pass";
								move[0] = move[2];
								move[1] = move[3];
								click_counter = 1;
								is_first_move = false;
							}
							else {
								// if there was a taking move on board and it was the players first
								// move in their turn (this is also checked in is_taking_move_on_board()
								// and they chose not to take any pieces we
								// yell at them and restore the board and tell them to start over
								if (taking_move_on_board) {
									// restore board
									for (int i=0; i<columns; i++) {
										for (int j=0; j<rows; ++j) {
											game_board_array[i][j] = prev_board_state[i][j];
										}
									}
									game_message = "Player 2 must make a taking move! Please select a piece to move";
									click_counter = 0;
									repaint();
									return;
								}
								else {
									// else change players
									prev_locations.clear();
									prev_direction = -1;
									players_turn = 1;
									is_first_move = true;
									extra_turn_flag = 0;
									game_message = "Player 1 (Black) select a piece to move";
									begining_of_move = new Date().getTime();
									move_counter++;
								}
							}
							repaint();
							return;
						}
						else {
							if (extra_turn_flag == 1) {
								// the player took some pieces but then made invalid move
								game_message = "Invalid move! Player 2 (Red) select where to move ("+move[0]+","+move[1]+") or pass";
								click_counter = 1;
							}
							else {
								game_message = "That is not a valid move! Player 2 (red) select a peice to move";
							}
							repaint();
							return;
						}
					}
				} // end of click == 2
				
				// If the user is not selecting direction or pressing a special button then
				// we find what space on the board they clicked
				if (x<(piece_diameter/2) || x>(piece_diameter*2*columns+piece_diameter/2)
					|| y<(piece_diameter/2) || y>(piece_diameter*2*rows+piece_diameter/2)) {
					game_message = "That's not a valid point on the board! Try again.";
					repaint();
					return;
				}
				int x_with_error = x-(piece_diameter/2);
				int row = (int)(x_with_error / (piece_diameter*2));
				int y_with_error = y-(piece_diameter/2);
				int col = (int)(y_with_error / (piece_diameter*2));
				
				// now we need to figure out where in the game we are at
				// if it's the first click in a players move then we need
				// to store "old x" and "old y" in move array
				if (click_counter == 0) {
					// some error checking first
					if ((players_turn == 1 && game_board_array[row][col] == BLACK)
						|| (players_turn == 2 && game_board_array[row][col] == RED)) {
						move[0] = row;
						move[1] = col;
						prev_locations.add(move[0]);
						prev_locations.add(move[1]);
						click_counter++;
						game_message = "You have selected to move ("+row+","+col+"), please select where to move. Click this piece again to select another piece to move";
						repaint();
					}
					// if it doesn't meet the requirement then we can let them try again
					// without updating the counter
					else {
						game_message = "("+row+","+col+") is a not valid peice for you!";
						repaint();
					}
				}
				// is this is click number two, this is where they want to move
				// so we store "new x" and "new y"
				else if (click_counter == 1) {
					// if the user clicks the same piece we want to start over (unless they just took pieces)
					if (row == move[0] && col == move[1] && extra_turn_flag == 0) {
						click_counter = 0;
						if (players_turn == 1) {
							game_message = "Player 1 (Black) Select a piece to move";
							repaint();
							return;
						}
						else {
							game_message = "Player 2 (Red) Select a piece to move";
							repaint();
							return;
						}
					}
					// basic error checking
					if (game_board_array[row][col] == EMPTY) {
						// complicated error checking
						// if the point we are sums up to be odd we can only move N, S, E, W
						// for example (0, 1) would be only be able to move N, W, E, W
						// since 0+1 = 1 and 1 is odd
						if ((move[0]+move[1])%2 == 1) {
							//since we can only move in the same col or row
							//if BOTH row and col change then its an invalid move
							if (move[0] != row && move[1] != col) {
								game_message = "No line to move along, please select a valid empty spot to move ("+move[0]+","+move[1]+")";
								repaint();
								return;
							}
						}
						move[2] = row;
						move[3] = col;
						click_counter++;
						// we need to update the game_message and let the user select a direction
						game_message = "moving ("+move[0]+","+move[1]+") to ("+row+","+col+") Please select a direction to take.";
						repaint();
						
					}
					// else its not a valid move
					else {
						game_message = "("+row+","+col+") is not empty!, select an empty place";
						repaint();
					}
				} // end of click == 1
				
			}// end of processClick
			
			public void paintComponent(Graphics g) {//Display board
				// this has nothing to do with graphics but it
				// seemed like a good place to check whether or not
				// we need to remove a sacrificed piece from the board
				if (player_1_sac_move[0] == 1 && player_1_sac_move[1] == move_counter) {
					game_board_array[player_1_sac_move[2]][player_1_sac_move[3]] = EMPTY;
					player_1_sac_move[0] = 0;
				}
				if (player_2_sac_move[0] == 1 && player_2_sac_move[1] == move_counter) {
					game_board_array[player_2_sac_move[2]][player_2_sac_move[3]] = EMPTY;
					player_2_sac_move[0] = 0;
				}
				
				// 'erase' previous message, and put in new message
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				g.setColor(Color.black);
				g.setFont(new  Font("Serif", Font.BOLD, 14));
				g.drawString(game_message, x_offset, 20+rows*piece_diameter*2);
				
				// calculate time elapsed and print it
				int total_seconds = (int) (((new Date()).getTime()-starting_time)/1000);
				minutes = total_seconds/60;
				seconds = total_seconds-minutes*60;
				g.drawString(minutes+":"+seconds, columns*piece_diameter*2, 20);
				
				// print number of moves
				g.drawString("Turn Count: "+move_counter, x_offset, 20);
				
				// draw the vertical lines
		        g.setColor(Color.black);
		        for (int i=0; i<columns; ++i) {
		        	g.drawLine(x_offset+piece_diameter/2+piece_diameter*2*i, y_offset+piece_diameter/2,
		        			x_offset+piece_diameter/2+piece_diameter*2*i,
		        			y_offset-piece_diameter-piece_diameter/2+piece_diameter*2*rows);
		        }
		        // draw horizontal lines
		        for (int i=0; i<rows; ++i) {
		        	g.drawLine(x_offset+piece_diameter/2, y_offset+piece_diameter/2+piece_diameter*2*i,
		        			x_offset-piece_diameter-piece_diameter/2+piece_diameter*2*columns,
		        			y_offset+piece_diameter/2+piece_diameter*2*i);
		        }
				// draw slanted (top left to bottom right) lines
		        for (int i=0; i<(rows-1); ++i) {
		        	if (i%2 == 0) {
		        		for (int j=0; j<(columns/2); ++j) {
		        			g.drawLine(x_offset+(piece_diameter/2)+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2*i,
		        					x_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*2*i);
		        		}
		        	}
		        	else {
		        		for (int j = 0; j<(columns/2); ++j) {
		        			g.drawLine(x_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2*i,
		        					x_offset+(piece_diameter/2)+piece_diameter*4+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*2*i);
		        		}
		        	}
		        }
		        // draw more slanted lines
		        for (int i=0; i<(rows-1); ++i) {
		        	if (i%2 == 0) {
		        		for (int j=0; j<(columns/2); ++j) {
		        			g.drawLine(x_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*2*i,
		        					x_offset+(piece_diameter/2)+piece_diameter*4+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2*i);
		        		}
		        	}
		        	else {
		        		for (int j = 0; j<(columns/2); ++j) {
		        			g.drawLine(x_offset+(piece_diameter/2)+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*2*i,
		        					x_offset+(piece_diameter/2)+piece_diameter*2+piece_diameter*4*j,
		        					y_offset+(piece_diameter/2)+piece_diameter*2*i);
		        		}
		        	}
		        }
		        
				// draw the game pieces
		        for(int i = 0; i<rows; i++) {
		        	for(int j = 0; j<columns; j++) {
		        		if(game_board_array[j][i] == RED) {
		        			g.setColor(Color.red);
		        			g.fillOval(x_offset+piece_diameter*2*j, y_offset+piece_diameter*2*i, piece_diameter, piece_diameter);
		        		} else if(game_board_array[j][i] == BLACK) {
		        			g.setColor(Color.black);
		        			g.fillOval(x_offset+piece_diameter*2*j, y_offset+piece_diameter*2*i, piece_diameter, piece_diameter);
		        		} else if(game_board_array[j][i] == EMPTY) {
		        			// draw nothing
		        		} else if(game_board_array[j][i] == SACRIFICED) {
		        			g.setColor(Color.gray);
		        			g.fillOval(x_offset+piece_diameter*2*j, y_offset+piece_diameter*2*i, piece_diameter, piece_diameter);
		        		}
		        	}	        		        
		        }
		        
		        // draw pass button
		        if (click_counter == 1 && extra_turn_flag == 1) {
		        	g.setColor(Color.black);
		        	g.drawRect(x_offset+columns*piece_diameter*2, (rows*piece_diameter), 75, 25);
		        	g.drawString("Pass", x_offset+columns*piece_diameter*2+7, rows*piece_diameter+17);
		        }
		        // draw sacrifice button
		        if (click_counter == 1 && extra_turn_flag == 0) {
		        	g.setColor(Color.black);
		        	g.drawRect(x_offset+columns*piece_diameter*2, (rows*piece_diameter), 75, 25);
		        	g.drawString("Sacrifice", x_offset+columns*piece_diameter*2+7, rows*piece_diameter+17);
		        }
		        // draw take options
		        if (click_counter == 2) {
		        	g.setColor(Color.black);
		        	g.drawRect(x_offset+columns*piece_diameter*2, y_offset, 75, 25);
		        	g.drawRect(x_offset+columns*piece_diameter*2, y_offset+piece_diameter*2, 75, 25);
		        	g.drawRect(x_offset+columns*piece_diameter*2, y_offset+piece_diameter*4, 75, 25);
		        	g.drawString("Forward", x_offset+columns*piece_diameter*2+7, y_offset+17);
		        	g.drawString("Backward", x_offset+columns*piece_diameter*2+7, y_offset+17+piece_diameter*2);
		        	g.drawString("No Take", x_offset+columns*piece_diameter*2+7, y_offset+17+piece_diameter*4);
		        }
			}

			public void mousePressed(MouseEvent e) {
				processClick(e.getX(), e.getY());
			}
			
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void actionPerformed(ActionEvent e) {}
		}
		
		public void create_window() {
			window = new JFrame("Fanorona");
			window.setTitle("Fanorona Game");
			window.setBounds(400, 0, 950, 850);
			window.setResizable(true);
			window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
			window.setVisible(true);
			starting_time = (new Date()).getTime();
			window.add(graphics);
			graphics.addMouseListener(graphics);
		}
	}
