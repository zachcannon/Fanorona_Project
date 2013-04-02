package fanorona.com;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import java.awt.Font;

public class Rules extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Rules frame = new Rules();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Rules() {
		setTitle("Rules");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 646, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea txtrRulesOfThe = new JTextArea();
		txtrRulesOfThe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtrRulesOfThe.setText("Rules of the game Fanorona:\r\n\r\n - In our version Player 1, or Black, will go first.\r\n\r\n - A player moves by clicking a piece to move and then clicking where to move the piece.\r\n - A player can only move along the gridlines and can only move one spot at a time.\r\n\r\n - After selecting where to move the player can choose to \"take\" pieces or not \"take\" pieces.\r\n - If they want to \"take\" the pieces in front of where the piece is being moved to (forward) or\r\n    if they want to \"take\" the pieces behind where the piece is moving from (backwards).\r\n\r\n - If a player takes a piece then they can move that piece once again (much like checkers) but the player\r\n    cannot move in the same direction as the previous move and cannot move to space on the board they\r\n    had been to previously in that turn.\r\n\r\n - After taking pieces the player can choose to pass even if there is the option to take more pieces.\r\n\r\n - A player can also choose to sacrifice a piece. Sacrificing a piece will make the piece non-capturable for the\r\n   next player's turn. The piece will be removed from the board after the next turn is completed.");
		txtrRulesOfThe.setBounds(10, 11, 607, 308);
		contentPane.add(txtrRulesOfThe);
	}
}
