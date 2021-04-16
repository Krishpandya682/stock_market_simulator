package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Shares.Shares;
import TempDriver.UsersDatabaseGenerator;
import Users.SharesDatabaseGenerator;
import Users.User;

public class SignupPageGUI extends MainProgram {

	JFrame frame;
	private JTextField usernameTextField;
	private JTextField ageTextField;
	String USERNAME_ALREADY_EXIST = "Username already exists";
	String PASSWORDS_DONOT_MATCH = "Passwords do not match";

	/**
	 * Launch the application.
	 */
	public static void signupPageGUI() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					SignupPageGUI window = new SignupPageGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection con;
	private JPasswordField passwordField;
	private JPasswordField repeatPasswordField;
	private JTextField EmailIDTextField;

	/**
	 * Create the application.
	 */
	public SignupPageGUI() {
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 510, 555);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUsername.setBounds(33, 84, 103, 28);
		frame.getContentPane().add(lblUsername);

		usernameTextField = new JTextField();
		usernameTextField.setBounds(224, 84, 215, 28);
		frame.getContentPane().add(usernameTextField);
		usernameTextField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(224, 157, 218, 28);
		frame.getContentPane().add(passwordField);

		JLabel lblPwd = new JLabel("Password");
		lblPwd.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPwd.setBounds(33, 153, 103, 28);
		frame.getContentPane().add(lblPwd);

		ageTextField = new JTextField();
		ageTextField.setColumns(10);
		ageTextField.setBounds(224, 237, 215, 28);
		frame.getContentPane().add(ageTextField);

		JLabel lblAge = new JLabel("Age");
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAge.setBounds(33, 237, 103, 28);
		frame.getContentPane().add(lblAge);

		JButton btnSignup = new JButton("Signup");
		btnSignup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sql = "SELECT * from Users where NAME = ?";
				try {
					String username = usernameTextField.getText();

					PreparedStatement smnt = con.prepareStatement(sql);
					smnt.setString(1, username);
					ResultSet r = smnt.executeQuery();
					if (!r.isClosed()) {
						JOptionPane.showMessageDialog(frame,
								USERNAME_ALREADY_EXIST);
						r.close();
					} else {
						if (!String.valueOf(passwordField.getPassword())
								.equals(String.valueOf(
										repeatPasswordField.getPassword()))) {

							JOptionPane.showMessageDialog(frame,
									PASSWORDS_DONOT_MATCH);

						} else {
							ArrayList<Shares> a = new ArrayList<Shares>();
							a = SharesDatabaseGenerator.returnSharesOfUser(
									usernameTextField.getText());
							User u = new User(usernameTextField.getText(),
									String.valueOf(passwordField.getPassword()),
									EmailIDTextField.getText(),
									Integer.parseInt(ageTextField.getText()),
									a);

							UsersDatabaseGenerator.userDatabasePopulator(u);
							UserProfileGUI.UserProfileWindow(u);
							setLoggedIn(true);
							setLoggedInAsUser(u);
							frame.dispose();

						}

					}

				} catch (SQLException e1) {

					e1.printStackTrace();
				}
			}
		});
		btnSignup.setBounds(162, 310, 137, 40);
		frame.getContentPane().add(btnSignup);

		JLabel lblRepeatpassword = new JLabel("Repeat Password");
		lblRepeatpassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblRepeatpassword.setBounds(33, 198, 157, 28);
		frame.getContentPane().add(lblRepeatpassword);

		repeatPasswordField = new JPasswordField();
		repeatPasswordField.setBounds(224, 200, 218, 28);
		frame.getContentPane().add(repeatPasswordField);

		EmailIDTextField = new JTextField();
		EmailIDTextField.setColumns(10);
		EmailIDTextField.setBounds(224, 123, 215, 28);
		frame.getContentPane().add(EmailIDTextField);

		JLabel lblEmail = new JLabel("Email ID");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblEmail.setBounds(33, 123, 103, 28);
		frame.getContentPane().add(lblEmail);

	}
}
