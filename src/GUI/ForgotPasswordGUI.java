package GUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import DataManagement.DatabaseReader;
import EmailSender.EmailSender;
import Exceptions.DoesNotExistException;

public class ForgotPasswordGUI {

	private JFrame frame;
	private JTextField usernameTextField;
	private JTextField EmailIDTextField;
	private JButton btnSendOTP;
	private JTextField OTPtextField;
	private JButton btnSubmit;

	private String USERNAME_DOESNOT_MATCH_EMAIL = "Username does not match the email";
	private String USERNAME_DOESNOT_EXIST = "Username does not exist";
	private String INCORRECT_OTP = "Incorrect OTP";
	private String ERROR_OCCURED = "Error occured";

	private int otp;

	/**
	 * Launch the application.
	 */
	public static void forgotPasswordGUI() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ForgotPasswordGUI window = new ForgotPasswordGUI();
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
	public ForgotPasswordGUI() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 579, 454);
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

		EmailIDTextField = new JTextField();
		EmailIDTextField.setColumns(10);
		EmailIDTextField.setBounds(224, 123, 215, 28);
		frame.getContentPane().add(EmailIDTextField);

		JLabel lblEmail = new JLabel("Email ID");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblEmail.setBounds(33, 123, 103, 28);
		frame.getContentPane().add(lblEmail);

		btnSendOTP = new JButton("Send OTP");
		btnSendOTP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String[] userData = null;

				String email = EmailIDTextField.getText();
				String username = usernameTextField.getText();
				try {
					userData = DatabaseReader.getUserInfo(username);
				} catch (DoesNotExistException e2) {
					JOptionPane.showMessageDialog(frame, e2.getMessage());
				}

				if (email.equals(userData[3])) {
					btnSubmit.setVisible(true);
					OTPtextField.setVisible(true);
					try {
						otp = EmailSender
								.emailSender(EmailIDTextField.getText());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, ERROR_OCCURED);
					}

				} else {
					JOptionPane.showMessageDialog(frame,
							USERNAME_DOESNOT_MATCH_EMAIL);

				}

			}
		});
		btnSendOTP.setBounds(198, 195, 89, 23);
		frame.getContentPane().add(btnSendOTP);

		OTPtextField = new JTextField();
		OTPtextField.setColumns(10);
		OTPtextField.setBounds(143, 269, 215, 28);
		frame.getContentPane().add(OTPtextField);
		OTPtextField.setVisible(false);

		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Integer.valueOf(OTPtextField.getText()) == otp) {
					ResetPasswordPageGUI
							.resetPasswordPageGUI(usernameTextField.getText());
					frame.dispose();
				} else {
					JOptionPane.showMessageDialog(frame, INCORRECT_OTP);
				}
			}
		});
		btnSubmit.setBounds(198, 323, 89, 23);
		frame.getContentPane().add(btnSubmit);
		btnSubmit.setVisible(false);

	}

}
