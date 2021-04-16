package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import DataManagement.DatabaseReader;
import Exceptions.DoesNotExistException;
import Users.SharesDatabaseGenerator;
import Users.User;

public class LoginPageGUI extends MainProgram {

	JFrame frame;
	private JTextField usernameTextField;
	String USERNAME_DOESNOT_MATCH_PASSWORD = "Username or Password is incorrect";
	String USERNAME_DOESNOT_EXIST = "Username does not exist";

	/**
	 * Launch the application.
	 */
	public static void LoginPage() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LoginPageGUI window = new LoginPageGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPasswordField passwordField;

	/**
	 * Create the application.
	 */
	public LoginPageGUI() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 448, 652);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblUserName = new JLabel("Username");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblUserName.setBounds(46, 79, 120, 40);
		frame.getContentPane().add(lblUserName);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblPassword.setBounds(46, 153, 120, 40);
		frame.getContentPane().add(lblPassword);

		usernameTextField = new JTextField();
		usernameTextField.setBounds(176, 79, 172, 40);
		frame.getContentPane().add(usernameTextField);
		usernameTextField.setColumns(10);
		usernameTextField.setEditable(true);

		passwordField = new JPasswordField();
		passwordField.setBounds(176, 156, 172, 37);
		frame.getContentPane().add(passwordField);

		JButton btnForgotPassword = new JButton("Forgot Password?");
		btnForgotPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ForgotPasswordGUI.forgotPasswordGUI();
				frame.dispose();
			}
		});
		btnForgotPassword.setBounds(135, 335, 127, 23);
		frame.getContentPane().add(btnForgotPassword);
		btnForgotPassword.setVisible(false);

		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] userData = null;

				String pwd = String.valueOf(passwordField.getPassword());
				String username = usernameTextField.getText();
				try {
					userData = DatabaseReader.getUserInfo(username);
				} catch (DoesNotExistException e1) {
					JOptionPane.showMessageDialog(frame,
							USERNAME_DOESNOT_EXIST);
				}

				if (pwd.equals(userData[2])) {

					int age = Integer.parseInt(userData[4]);
					double cap = Double.parseDouble(userData[5]);
					double capAsShares = Double.parseDouble(userData[6]);
					String email = userData[3];
					User user = new User(username, pwd, email, age, cap,
							capAsShares, SharesDatabaseGenerator
									.returnSharesOfUser(username));
					setLoggedIn(true);
					setLoggedInAsUser(user);
					UserProfileGUI upg = new UserProfileGUI(user);

					upg.frame.setVisible(true);
					frame.dispose();

				} else {
					JOptionPane.showMessageDialog(frame,
							USERNAME_DOESNOT_MATCH_PASSWORD);
					setFailedAttempt(getFailedAttempt() + 1);
					if (getFailedAttempt() >= 3) {
						btnForgotPassword.setVisible(true);
					}
				}

			}
		});
		submitButton.setBounds(119, 244, 159, 59);
		frame.getContentPane().add(submitButton);

		JButton btnSignup = new JButton("SignUp");
		btnSignup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SignupPageGUI spg = new SignupPageGUI();
				spg.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnSignup.setBounds(119, 420, 159, 59);
		frame.getContentPane().add(btnSignup);

		JLabel lblNewUser = new JLabel("New user?");
		lblNewUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewUser.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblNewUser.setBounds(116, 369, 172, 40);
		frame.getContentPane().add(lblNewUser);

	}
}
