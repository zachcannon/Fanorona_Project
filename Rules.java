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
		setBounds(100, 100, 778, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// picture will only s
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("C:\\eclipse\\Rules.JPG"));
		label.setBounds(10, 19, 748, 372);
		contentPane.add(label);
		
		JLabel lblRulesAccordingTo = new JLabel("Rules According to Wikipedia:");
		lblRulesAccordingTo.setBounds(10, 11, 194, 14);
		contentPane.add(lblRulesAccordingTo);
	}
}
