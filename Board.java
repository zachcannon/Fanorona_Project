package fanorona.com;


import java.awt.*;
import javax.swing.*;

public class Board extends JPanel{
	
	class BoardGraphics extends Canvas {
		public BoardGraphics() {
			setSize(600,900);
			setBackground(Color.white);
		}	
		
		public void paint(Graphics g) {//Display board
			g.setColor(Color.gray);
	    	
	        for(int i = 0; i<5; i++) {
	        	for(int j = 0; j<9; j++) {
	        		if(board[i][j] == RED) {
	        			g.setColor(Color.red);
	        			g.fillOval(125+75*j, 100+75*i, 40, 40);
	        		} else if(board[i][j] == BLACK) {
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
	
	static final int
			EMPTY = 0,
			BLACK = 1,
			RED = 2;
	
	public int[][] board;
	JFrame window;
	Canvas board_to_display;
	BoardGraphics graphics = new BoardGraphics();
	
	public Board() {
		board = new int[5][9]; //Row by col
		setup_board();
		create_window();
	}
	
	public void setup_board() {
		for(int i = 0; i<2; i++) {
			for(int j = 0; j<9; j++) {				
				board[i][j] = BLACK;
			}
		}
		
		for(int i = 3; i<5; i++) {
			for(int j = 0; j<9; j++) {				
				board[i][j] = RED;
			}
		}
		
		board[2][0] = BLACK;
		board[2][1] = RED;
		board[2][2] = BLACK;
		board[2][3] = RED;
		board[2][4] = EMPTY;
		board[2][5] = BLACK;
		board[2][6] = RED;
		board[2][7] = BLACK;
		board[2][8] = RED;
	
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
	
	public void update_board(int old_x, int old_y, int new_x, int new_y) {
		
	}
	
	public int[] get_move() {
		int [] move = {0,0};
		return move;
	}
	
	
	
}


