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
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class GameSelect extends JFrame {
	
	GameRun thegame = new GameRun();
	private JPanel contentPane;
	private JTextField txtTimeLimitms;
	
	int cols_;
	int rows_;
	String cols;
	String rows;
	
	int diff_; // 1 for easy, 3 for hard
	String diff;
	
	String time_limit;
	long time_limit_;

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
		setBounds(100, 100, 311, 311);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/// title
		JLabel lblSelectGameType = new JLabel("Select Game Type");
		lblSelectGameType.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectGameType.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSelectGameType.setBounds(10, 0, 275, 45);
		contentPane.add(lblSelectGameType);
		
		// Boxes and text fields
		txtTimeLimitms = new JTextField();
		txtTimeLimitms.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtTimeLimitms.setText("Time Limit (ms) per turn");
		txtTimeLimitms.setBounds(65, 73, 169, 20);
		contentPane.add(txtTimeLimitms);
		txtTimeLimitms.setColumns(10);
		
		final JComboBox colBox = new JComboBox();
		colBox.setModel(new DefaultComboBoxModel(new String[] {"Columns", "3", "5", "7", "9", "11", "13"}));
		colBox.setBounds(10, 42, 86, 20);
		contentPane.add(colBox);
		
		final JComboBox rowBox = new JComboBox();
		rowBox.setModel(new DefaultComboBoxModel(new String[] {"Rows", "3", "5", "7", "9", "11", "13"}));
		rowBox.setBounds(106, 42, 86, 20);
		contentPane.add(rowBox);
		
		final JComboBox diffBox = new JComboBox();
		diffBox.setModel(new DefaultComboBoxModel(new String[] {"Difficulty", "Easy", "Hard"}));
		diffBox.setBounds(202, 42, 83, 20);
		contentPane.add(diffBox);
		
		// Human vs Human button
		// button will call the functions in HumanVsHuman Class
		// also the window will close after the user makes the selection
		JButton btnHumanVsHuman = new JButton("Human vs Human");
		btnHumanVsHuman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Human vs Human Selected\n");
				cols = (String) colBox.getSelectedItem();
				rows = (String) rowBox.getSelectedItem();
				diff = (String) diffBox.getSelectedItem();
				if (cols.equals("Columns")) {
					cols_ = 9;
				}
				else {
					cols_ = Integer.parseInt(cols);
				}
				if (rows.equals("Rows")) {
					rows_ = 5;
				}
				else {
					rows_ = Integer.parseInt(rows);
				}
				if (diff.equals("Difficulty") || diff.equals("Easy")) {
					diff_ = 1;
				}
				else {
					diff_ = 2;
				}
				time_limit = txtTimeLimitms.getText();
				if (time_limit.equals("Time Limit (ms) per turn") || time_limit.equals("")) {
					// default to ten minutes
					time_limit_ = 600000;
				}
				else {
					time_limit_ = Long.parseLong(time_limit);
				}
				thegame.run_game(0, cols_, rows_, time_limit_, diff_);				
				dispose();
			}
		});
		btnHumanVsHuman.setBounds(10, 104, 275, 45);
		contentPane.add(btnHumanVsHuman);
		
		// Human vs CPU
		JButton btnHumanVsCpu = new JButton("Human vs CPU");
		btnHumanVsCpu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("Human vs CPU Selected\n");
				cols = (String) colBox.getSelectedItem();
				rows = (String) rowBox.getSelectedItem();
				diff = (String) diffBox.getSelectedItem();
				if (cols.equals("Columns")) {
					cols_ = 9;
				}
				else {
					cols_ = Integer.parseInt(cols);
				}
				if (rows.equals("Rows")) {
					rows_ = 5;
				}
				else {
					rows_ = Integer.parseInt(rows);
				}
				if (diff.equals("Difficulty") || diff.equals("Easy")) {
					diff_ = 1;
				}
				else {
					diff_ = 3;
				}
				time_limit = txtTimeLimitms.getText();
				if (time_limit.equals("Time Limit (ms) per turn") || time_limit.equals("")) {
					// default to ten minutes
					time_limit_ = 600000;
				}
				else {
					time_limit_ = Long.parseLong(time_limit);
				}
				thegame.run_game(1, cols_, rows_, time_limit_, diff_);				
				dispose();
			}
		});
		btnHumanVsCpu.setBounds(10, 160, 275, 45);
		contentPane.add(btnHumanVsCpu);
		
		// CPU vs CPU
		JButton btnCpuVsCpu = new JButton("CPU vs CPU");
		btnCpuVsCpu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("CPU vs CPU Selected\n");
				cols = (String) colBox.getSelectedItem();
				rows = (String) rowBox.getSelectedItem();
				diff = (String) diffBox.getSelectedItem();
				if (cols.equals("Columns")) {
					cols_ = 9;
				}
				else {
					cols_ = Integer.parseInt(cols);
				}
				if (rows.equals("Rows")) {
					rows_ = 5;
				}
				else {
					rows_ = Integer.parseInt(rows);
				}
				if (diff.equals("Difficulty") || diff.equals("Easy")) {
					diff_ = 1;
				}
				else {
					diff_ = 3;
				}
				time_limit = txtTimeLimitms.getText();
				if (time_limit.equals("Time Limit (ms) per turn") || time_limit.equals("")) {
					// default to ten minutes
					time_limit_ = 600000;
				}
				else {
					time_limit_ = Long.parseLong(time_limit);
				}
				thegame.run_game(2, cols_, rows_, time_limit_, diff_);				
				dispose();
			}
		});
		btnCpuVsCpu.setBounds(10, 216, 275, 45);
		contentPane.add(btnCpuVsCpu);
	}
}
