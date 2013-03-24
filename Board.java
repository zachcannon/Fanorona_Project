package fanorona.com;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class Board extends JPanel{
	//----------------------------GLOBAL_VARS--------------------------//
	static final int
	EMPTY = 0,
	BLACK = 1,	//PLAYER 1 is black
	RED = 2;	//PLAYER 2 is red

	JFrame window;
	String game_message;
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
		create_window();
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
	
	public int check_end_of_game() { //Returns 0 if fail, 1 if game over red wins, 2 if game over black wins
		int black_num = 0;
		int red_num = 0;
		
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<5; j++) {				
				if(game_board_array[i][j] == RED) red_num++;
				if(game_board_array[i][j] == BLACK) black_num++;
			}
		}
		
		if(black_num == 0) return 1;
		else if(red_num == 0) return 2;
		else return 0;
	}
	
	/*public void game_main() {//Main logic
		
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
	}*/
	
	
	//---------------------------PLAYER_CLASS--------------------------//
	
	public abstract class Player {
		public int who_am_i; //Corresponds to board colors above in line ~10
		public int what_am_i; //Corresponds to human or comp. 0=human, 1=comp
		public abstract int get_who_i_am();
		public abstract int get_what_i_am();
		
		//public abstract int players_turn(); //Return 1 for successful move, return 0 for invalid move choice
		
		//public abstract int[] get_move();	//Returns four numbers in an array - 0=old_x, 1=old_y, 2=new_x, 3=new_y, 4=forward or backwards(2=back, 1=forward, 0 if no take)
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

		/*@Override
		public int players_turn() {
			int move_success = 0;
			int[] move_array = get_move();
			
			if(check_valid_move(move_array) == 1) {
				execute_move(move_array);
				move_success = 1;
			} else {
				//!!!!!!!!!!!!!!!PRINT OUT NOT A VALID MOVE
				move_success = 0;
			}			
			return move_success;
		}*/

		/*@Override
		public int[] get_move() {
			// TODO Auto-generated method stub
			return null;
		}*/

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
				if(old_y < new_y) { //South
					System.out.println("SOUTHEAST move!!!!!!!!");
					compass_direction = 4;
				} else if (old_y > new_y){
					System.out.println("NORTHEAST move!!!!!!!!");
					compass_direction = 2;
				} else {
					System.out.println("EAST move!!!!!!!!");
					compass_direction = 3;
				}
			} else if (old_x > new_x) { //West move
				if(old_y < new_y) { //NORTH
					System.out.println("SOUTHWEST move!!!!!!!!");
					compass_direction = 6;
				} else if (old_y > new_y){ //South
					System.out.println("NORTHWEST move!!!!!!!!");
					compass_direction = 8;
				} else {
					System.out.println("WEST move!!!!!!!!");
					compass_direction = 7;
				}
			} else { //NORTH OR SOUTH
				if(old_y < new_y) { //NORTH
					System.out.println("SOUTH move!!!!!!!!");
					compass_direction = 5;
				} else {
					System.out.println("NORTH move!!!!!!!!");
					compass_direction = 1;
				}
			}
			
			erase_pieces(new_x, new_y, compass_direction, direction_to_take);			
		}

		@Override
		public void erase_pieces(int new_x, int new_y, int compass_direction, int direction_to_take) {
			if (direction_to_take == 0) return;
			
			if (compass_direction == 1) { //NORTH
				if (direction_to_take == 1) {//Forward take
					for(int i = 1; i<5; i++) {
						if(game_board_array[new_x][new_y-i] != EMPTY && game_board_array[new_x][new_y-i] != players_turn) {
							game_board_array[new_x][new_y-i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i < 5; i++) {
						if(game_board_array[new_x][new_y+i] != EMPTY && game_board_array[new_x][new_y+i] != players_turn) {
							game_board_array[new_x][new_y+i] = EMPTY;
						} else {
							break;
						}
					}
				}
			} else if (compass_direction == 2) { //NORTHEAST
				if (direction_to_take == 1) {
					for(int i = 1; i<5; i++) {
						if(new_x+i < 9 && new_y-i > 0) {
							if(game_board_array[new_x+i][new_y-i] != EMPTY && game_board_array[new_x+i][new_y-i] != players_turn) {
								game_board_array[new_x+i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i<5; i++) {
						if(new_x-i >= 0 && new_y+i < 5) {
							if(game_board_array[new_x-i][new_y+i] != EMPTY && game_board_array[new_x-i][new_y+i] != players_turn) {
								game_board_array[new_x-i][new_y+i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
				
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
				if (direction_to_take == 1) {
					for(int i = 1; i<5; i++) {
						if(new_x+i < 9 && new_y+i < 5) {
							if(game_board_array[new_x+i][new_y+i] != EMPTY && game_board_array[new_x+i][new_y+i] != players_turn) {
								game_board_array[new_x+i][new_y+i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i<5; i++) {
						if(new_x-i >= 0 && new_y-i >= 0) {
							if(game_board_array[new_x-i][new_y-i] != EMPTY && game_board_array[new_x-i][new_y-i] != players_turn) {
								game_board_array[new_x-i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}	
			} else if (compass_direction == 5) { //SOUTH
				if (direction_to_take == 1) {//Forward take
					for(int i = 1; i<5-new_y; i++) {
						if(game_board_array[new_x][new_y+i] != EMPTY && game_board_array[new_x][new_y+i] != players_turn) {
							game_board_array[new_x][new_y+i] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i < 5; i++) {
						if(game_board_array[new_x][new_y-i] != EMPTY && game_board_array[new_x][new_y-i] != players_turn) {
							game_board_array[new_x][new_y-i] = EMPTY;
						} else {
							break;
						}
					}
				}				
			} else if (compass_direction == 6) { //SOUTHWEST
				if (direction_to_take == 1) {
					for(int i = 1; i<5; i++) {
						if(new_x-i >= 0 && new_y+i < 5) {
							if(game_board_array[new_x-i][new_y+i] != EMPTY && game_board_array[new_x-i][new_y+i] != players_turn) {
								game_board_array[new_x-i][new_y+i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i<5; i++) {
						if(new_x+i < 9 && new_y-i >= 0) {
							if(game_board_array[new_x+i][new_y-i] != EMPTY && game_board_array[new_x+i][new_y-i] != players_turn) {
								game_board_array[new_x+i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}	
			} else if (compass_direction == 7) { //WEST
				if (direction_to_take == 1) {//Forward take
					for(int i = 1; i < 9; i++) {
						if(game_board_array[new_x-i][new_y] != EMPTY && game_board_array[new_x-i][new_y] != players_turn) {
							game_board_array[new_x-i][new_y] = EMPTY;
						} else {
							break;
						}
					}
				} else {
					for(int i = 2; i < 9; i++) {
						if(game_board_array[new_x+i][new_y] != EMPTY && game_board_array[new_x+i][new_y] != players_turn) {
							game_board_array[new_x+i][new_y] = EMPTY;
						} else {
							break;
						}
					}
				}	
			} else {//NORTHWEST
				if (direction_to_take == 1) {
					for(int i = 1; i<5; i++) {
						if(new_x-i >= 0 && new_y-i >= 0) {
							if(game_board_array[new_x-i][new_y-i] != EMPTY && game_board_array[new_x-i][new_y-i] != players_turn) {
								game_board_array[new_x-i][new_y-i] = EMPTY;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				} else { 
					for(int i = 2; i<5; i++) {
						if(new_x+i < 9 && new_y+i < 5) {
							if(game_board_array[new_x+i][new_y+i] != EMPTY && game_board_array[new_x+i][new_y+i] != players_turn) {
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

	public class Computer_Player extends Player{

		public Computer_Player(int player_number, int player_type) {
			who_am_i = player_number;
			what_am_i = player_type;
		}
		
		public int get_who_i_am() {return who_am_i;}
		public int get_what_i_am() {return what_am_i;}

		/*@Override
		public int players_turn() {
			// TODO Auto-generated method stub
			return 0;
		}*/

		/*@Override
		public int[] get_move() {
			// TODO Auto-generated method stub
			return null;
		}*/

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
	class BoardGraphics extends JPanel implements ActionListener, MouseListener {
		int width = 900;
		int height = 600;
		int click_counter;
		// 0=old_x, 1=old_y, 2=new_x, 3=new_y, 4=forward or backwards(2=back, 1=forward, 0 if no take)
		int[] move;
		public BoardGraphics() {
			game_message = "Welcome to Fanorona! Player 1 (Black) will go first, please select a piece to move";
			click_counter = 0;
			move = new int[5];
			setSize(height,width);
			setBackground(Color.white);
		}	
		
		
		public void processClick(int x, int y) {
			
			if (click_counter == 2) {
				click_counter = 0;
				/* g.drawRect(425, 500, 100, 25);
	        	g.drawRect(550, 500, 100, 25);
	        	g.drawRect(675, 500, 100, 25);
	        	*/
				if (x > 425 && x < 525 && y > 500 && y < 525) {
					move[4] = 1;
				}
				else if (x > 550 && x < 650 && y > 500 && y < 525) {
					move[4] = 2;
				}
				else if (x > 675 && x < 775 && y > 500 && y < 525) {
					move[4] = 0;
				}
				else {
					game_message = "Please select a direction to take";
					repaint();
					return;
				}
				if (players_turn == 1) {
					if (player_1.check_valid_move(move) == 1) {
						player_1.execute_move(move);
						players_turn = 2;
						game_message = "Player 2 (Red) select a piece to move";
						repaint();
						return;
					}
					else {
						game_message = "That is not a valid move! Player 1 (black) select a peice to move";
						repaint();
						return;
					}
				}
				else {
					if (player_2.check_valid_move(move) == 1) {
						player_2.execute_move(move);
						players_turn = 1;
						game_message = "Player 1 (Black) select a piece to move";
						repaint();
						return;
					}
					else {
						game_message = "That is not a valid move! Player 2 (red) select a peice to move";
						repaint();
						return;
					}
				}
			} // end of click == 2
			
			// first we get point on the board the user clicked
			System.out.println("X: "+x+" Y: "+y);
			if (x<70 || x>790 || y <55 || y>455) {
				System.out.println("invalid!");
				game_message = "not valid! try again";
				repaint();
				return;
			}
			int x_no_offset = x-70;
			int row = (int)(x_no_offset / 80);
			int y_no_offset = y-55;
			int col = (int)(y_no_offset / 80);
			System.out.println("Row: "+row+" Col: "+col);
			
			// now we need to figure out where in the game we are at
			// if it's the first click in a players move then we need
			// to store "old x" and "old y" in move array
			if (click_counter == 0) {
				// some error checking first
				if ((players_turn == 1 && game_board_array[row][col] == BLACK)
					|| (players_turn == 2 && game_board_array[row][col] == RED)) {
					move[0] = row;
					move[1] = col;
					click_counter++;
					game_message = "you have selected to move ("+row+","+col+"), please select where to move";
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
				// basic error checking
				if (game_board_array[row][col] == EMPTY) {
					move[2] = row;
					move[3] = col;
					click_counter++;
					// we need to update the game_message and let the user select a direction
					// if we moving in the same row, we can either go up or down
					game_message = "moving ("+move[0]+","+move[1]+") to ("+row+","+col+") Please select a direction";
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
			// 'erase' previous message, and put in new message
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			g.setColor(Color.black);
			g.setFont(new  Font("Serif", Font.BOLD, 14));
			g.drawString(game_message, 40, 500);
			
			// draw the vertical lines
	        g.setColor(Color.black);
	        for (int i=0; i<9; ++i) {
	        	g.drawLine(110+80*i, 95, 110+80*i, 415);
	        }
	        // draw horizontal lines
	        for (int i=0; i<5; ++i) {
	        	g.drawLine(110, 95+80*i, 750, 95+80*i);
	        }
			// draw slanted (top left to bottom right) lines
	        for (int i=0; i<4; ++i) {
	        	if (i == 0 || i == 2) {
	        		for (int j=0; j<4; ++j) {
	        			g.drawLine(110+160*j, 95+80*i, 190+160*j, 175+80*i);
	        		}
	        	}
	        	else {
	        		for (int j = 0; j<4; ++j) {
	        			g.drawLine(190+160*j, 95+80*i, 270+160*j, 175+80*i);
	        		}
	        	}
	        }
	        // draw more slanted lines
	        for (int i=0; i<4; ++i) {
	        	if (i == 0 || i == 2) {
	        		for (int j=0; j<4; ++j) {
	        			g.drawLine(190+160*j, 175+80*i, 270+160*j, 95+80*i);
	        		}
	        	}
	        	else {
	        		for (int j = 0; j<4; ++j) {
	        			g.drawLine(110+160*j, 175+80*i, 190+160*j, 95+80*i);
	        		}
	        	}
	        }
	        
			// draw the game pieces
	        for(int i = 0; i<5; i++) {
	        	for(int j = 0; j<9; j++) {
	        		if(game_board_array[j][i] == RED) {
	        			g.setColor(Color.red);
	        			g.fillOval(90+80*j, 75+80*i, 40, 40);
	        		} else if(game_board_array[j][i] == BLACK) {
	        			g.setColor(Color.black);
	        			g.fillOval(90+80*j, 75+80*i, 40, 40);
	        		} else if(game_board_array[j][i] == EMPTY) {
	        			// draw nothing
	        		}
	        	}	        		        
	        }
	        
	        if (click_counter == 2) {
	        	g.setColor(Color.black);
	        	g.drawRect(425, 500, 100, 25);
	        	g.drawRect(550, 500, 100, 25);
	        	g.drawRect(675, 500, 100, 25);
	        	g.drawString("Forward", 450, 515);
	        	g.drawString("Backward", 575, 515);
	        	g.drawString("No Take", 700, 515);
	        }
		}

		public void mousePressed(MouseEvent e) {
			System.out.println("mouse pressed");
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
		window.setBounds(100, 100, 900, 600);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		window.setVisible(true);
		window.add(graphics);
		graphics.addMouseListener(graphics);
	}
}

