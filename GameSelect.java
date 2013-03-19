package fanorona.com;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameSelect extends JFrame {
	
	GameRun thegame = new GameRun();
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameSelect frame = new GameSelect();
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
	public GameSelect() {
		
		// This window is to allow the user to choose which
		// game mode he wants to play
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 311, 274);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/// title
		JLabel lblSelectGameType = new JLabel("Select Game Type");
		lblSelectGameType.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectGameType.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSelectGameType.setBounds(10, 11, 275, 45);
		contentPane.add(lblSelectGameType);
		
		// Human vs Human button
		// button will call the functions in HumanVsHuman Class
		// also the window will close after the user makes the selection
		JButton btnHumanVsHuman = new JButton("Human vs Human");
		btnHumanVsHuman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Human vs Human Selected\n");
				thegame.run_game(0);				
				dispose();
			}
		});
		btnHumanVsHuman.setBounds(10, 71, 275, 45);
		contentPane.add(btnHumanVsHuman);
		
		// Human vs CPU
		JButton btnHumanVsCpu = new JButton("Human vs CPU");
		btnHumanVsCpu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Human vs CPU Selected\n");
				thegame.run_game(1);
				dispose();
			}
		});
		btnHumanVsCpu.setBounds(10, 127, 275, 45);
		contentPane.add(btnHumanVsCpu);
		
		// CPU vs CPU
		JButton btnCpuVsCpu = new JButton("CPU vs CPU");
		btnCpuVsCpu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("CPU vs CPU Selected\n");
				thegame.run_game(2);
				dispose();
			}
		});
		btnCpuVsCpu.setBounds(10, 183, 275, 45);
		contentPane.add(btnCpuVsCpu);
	}
}
