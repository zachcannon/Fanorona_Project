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
	public int[][] game_board_array;
	int players_turn;
	
	
	//----------------------------MAIN_GAME----------------------------//	
	public Board(int number_type) { //Constructor
		game_board_array = new int[5][9]; //Row by col
		
		game_number_type = number_type;
		
		//Create players based on game_players_num
		
		setup_board();
		players_turn = 1;		
	}	
	
	public void setup_board() {
		for(int i = 0; i<2; i++) {
			for(int j = 0; j<9; j++) {				
				game_board_array[i][j] = BLACK;
			}
		}
		
		for(int i = 3; i<5; i++) {
			for(int j = 0; j<9; j++) {				
				game_board_array[i][j] = RED;
			}
		}
		
		game_board_array[2][0] = BLACK;
		game_board_array[2][1] = RED;
		game_board_array[2][2] = BLACK;
		game_board_array[2][3] = RED;
		game_board_array[2][4] = EMPTY;
		game_board_array[2][5] = BLACK;
		game_board_array[2][6] = RED;
		game_board_array[2][7] = BLACK;
		game_board_array[2][8] = RED;
	
	}
	
	public void game_main() {//Main logic
		
		
	}
	
	
	//---------------------------PLAYER_CLASS--------------------------//
	
	public abstract class Player {
		public int who_am_i; //Corresponds to board colors above in line ~10
		public int what_am_i; //Corresponds to human or comp. 0=human, 1=comp
		
		
		public abstract int players_turn(); //Return 1 for successful move, return 0 for invalid move choice
		
		public abstract int[] get_move();	//Returns four numbers in an array - 0=old_x, 1=old_y, 2=new_x, 3=new_y, 4=forward or backwards(0=back, 1=forward
		public abstract int check_valid_move(int[] array_of_moves); //Checks if moves in array_of_moves are valid, returns 1 if good, returns 0 is invalid
		public abstract void execute_move(int[] array_of_moves);
	}
	
	public class Human_Player extends Player{
		
		public Human_Player(int player_number, int player_type) {
			who_am_i = player_number;
			what_am_i = player_type;
		}

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

		
		
		
	}

	public class Computer_Player extends Player{

		public Computer_Player(int player_number, int player_type) {
			who_am_i = player_number;
			what_am_i = player_type;
		}

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
	
	public int[] get_move() {
		int [] move = {0,0};
		return move;
	}
	
	
	
}


