package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import DataManagement.DatabaseReader;
import Exceptions.DoesNotExistException;
import Shares.Shares;
import TempDriver.UsersDatabaseGenerator;
import Users.SharesDatabaseGenerator;
import Users.User;

public class ResetPasswordPageGUI extends MainProgram {

	private JFrame frame;
	private JPasswordField passwordField;
	private JPasswordField repeatPasswordField;

	private String PASSWORDS_DONOT_MATCH = "Passwords do not match";

	/**
	 * Launch the application.
	 * 
	 * @param username
	 */
	public static void resetPasswordPageGUI(String username) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ResetPasswordPageGUI window = new ResetPasswordPageGUI(
							username);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ResetPasswordPageGUI(String username) {

		initialize(username);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String username) {
		frame = new JFrame();
		frame.setBounds(100, 100, 755, 476);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		passwordField = new JPasswordField();
		passwordField.setBounds(218, 73, 218, 28);
		frame.getContentPane().add(passwordField);

		repeatPasswordField = new JPasswordField();
		repeatPasswordField.setBounds(218, 116, 218, 28);
		frame.getContentPane().add(repeatPasswordField);

		JLabel lblRepeatpassword = new JLabel("Repeat Password");
		lblRepeatpassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblRepeatpassword.setBounds(27, 114, 157, 28);
		frame.getContentPane().add(lblRepeatpassword);

		JLabel lblPwd = new JLabel("Password");
		lblPwd.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPwd.setBounds(27, 69, 103, 28);
		frame.getContentPane().add(lblPwd);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] userData = null;
				try {
					userData = DatabaseReader.getUserInfo(username);
				} catch (DoesNotExistException e2) {
					e2.printStackTrace();
				}

				if (!String.valueOf(passwordField.getPassword()).equals(
						String.valueOf(repeatPasswordField.getPassword()))) {

					JOptionPane.showMessageDialog(frame, PASSWORDS_DONOT_MATCH);

				} else {
					ArrayList<Shares> a = new ArrayList<Shares>();
					a = SharesDatabaseGenerator.returnSharesOfUser(username);
					User u = new User(username,
							String.valueOf(passwordField.getPassword()),
							userData[3], Integer.parseInt(userData[4]),
							Double.parseDouble(userData[5]),
							Double.parseDouble(userData[6]), a);
					UsersDatabaseGenerator.userDatabasePopulator(u);
					UserProfileGUI.UserProfileWindow(u);
					setLoggedIn(true);
					setLoggedInAsUser(u);
					frame.dispose();

				}

			}
		});
		btnSubmit.setBounds(171, 231, 137, 40);
		frame.getContentPane().add(btnSubmit);
	}
}
