package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import Exceptions.NotEnoughCapitalException;
import Exceptions.SellingMoreThanYouHaveException;
import Exceptions.ThisShareIsNotOwnedException;
import TempDriver.UsersDatabaseGenerator;
import Users.SharesDatabaseGenerator;
import Users.User;
import net.proteanit.sql.DbUtils;

public class UserProfileGUI extends MainProgram {

	JFrame frame;
	private JTable table;
	String PLEASE_ENTER_QUANTITY = "Please enter quantity";
	String INVALID_QUANTITY = "Invalid quantity";

	/**
	 * Launch the application.
	 */
	public static void UserProfileWindow(User user) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UserProfileGUI window = new UserProfileGUI(user);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection con = null;
	private JTextField buyQuantTextField;
	private JTextField SellQuantTextField;

	/**
	 * Create the application.
	 * 
	 * @param user
	 */
	public UserProfileGUI(User user) {
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\"
							+ "StockMarketSimulator\\db\\dbNifty50.db");
		} catch (SQLException e) {

			e.printStackTrace();
		}
		initialize(user);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param user
	 */
	private void initialize(User user) {
		int uid = UsersDatabaseGenerator.userIDGetter(user.getName());

		frame = new JFrame();
		frame.setBounds(100, 100, 1024, 768);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblName = new JLabel(user.getName());
		lblName.setBounds(290, 11, 123, 14);
		frame.getContentPane().add(lblName);

		JLabel lblAge = new JLabel(String.valueOf(user.getAge()));
		lblAge.setBounds(291, 36, 69, 14);
		frame.getContentPane().add(lblAge);

		JLabel lblCap = new JLabel(String.valueOf(user.getCapital()));
		lblCap.setBounds(632, 11, 125, 14);
		frame.getContentPane().add(lblCap);

		JLabel lblCapAsShares = new JLabel();
		lblCapAsShares.setBounds(632, 36, 99, 14);
		frame.getContentPane().add(lblCapAsShares);

		JLabel lblTotal = new JLabel("Total = ");
		lblTotal.setBounds(481, 61, 49, 14);
		frame.getContentPane().add(lblTotal);

		JLabel lblTotalCap = new JLabel();
		lblTotalCap.setBounds(630, 61, 99, 14);
		frame.getContentPane().add(lblTotalCap);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(9, 191, 990, 107);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		JButton UserSharesTableReload = new JButton("Reload");
		UserSharesTableReload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BigDecimal sum = new BigDecimal(0);
				BigDecimal quote = new BigDecimal(0);

				try {

					String sql = "SELECT Nifty50Indices.COMPANY_NAME, Nifty50Indices.LTP, Shares.QUANTITY, Nifty50Indices.LTP * Shares.QUANTITY"
							+ " FROM Shares INNER JOIN Nifty50Indices "
							+ " ON Shares.SHARE_ID = Nifty50Indices.ID WHERE USER_ID = ?";
					PreparedStatement ps;
					ps = con.prepareStatement(sql);

					System.out.println("GUI" + uid);
					ps.setInt(1, uid);
					ResultSet rs2 = ps.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs2));

					table.getColumnModel().getColumn(3).setHeaderValue("QUOTE");

					String sqlquote = "SELECT * FROM Shares INNER JOIN Nifty50Indices "
							+ "	ON Shares.SHARE_ID = Nifty50Indices.ID WHERE USER_ID = ?";
					PreparedStatement psquote;
					psquote = con.prepareStatement(sqlquote);
					psquote.setInt(1, uid);

					ResultSet rs = psquote.executeQuery();
					while (rs.next()) {
						BigDecimal ltpBD = new BigDecimal(rs.getDouble("LTP"));
						BigDecimal quantBD = new BigDecimal(
								rs.getInt("QUANTITY"));

						quote = new BigDecimal(
								ltpBD.multiply(quantBD).doubleValue());
						System.out.println("quote = " + quote);
						sum = new BigDecimal(sum.add(quote).doubleValue());
						System.out.println("sum = " + sum);

					}
					System.out.println("final sum = " + sum);

					lblCapAsShares.setText(String.valueOf(sum));
					lblTotalCap.setText((String
							.valueOf(user.getCapital() + sum.doubleValue())));

					SharesDatabaseGenerator.emptyShareRemover();

				} catch (SQLException e1) {

					e1.printStackTrace();
				}
			}
		});
		UserSharesTableReload.setBounds(460, 309, 89, 23);
		frame.getContentPane().add(UserSharesTableReload);

		ArrayList<String> companies = new ArrayList<>();
		try {
			String sql = "SELECT * FROM Nifty50Indices";
			PreparedStatement ps;
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				companies.add(rs.getString("COMPANY_NAME"));
			}

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		;
		JComboBox<Object> buyComboBox = new JComboBox<Object>(
				companies.toArray());
		buyComboBox.setEditable(true);
		buyComboBox.setBounds(281, 412, 99, 22);
		frame.getContentPane().add(buyComboBox);

		JComboBox<Object> sellComboBox = new JComboBox<Object>(
				companies.toArray());
		sellComboBox.setEditable(true);
		sellComboBox.setBounds(734, 412, 99, 22);
		frame.getContentPane().add(sellComboBox);

		JButton btnBuy = new JButton("Buy");
		btnBuy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Integer.parseInt(buyQuantTextField.getText());
				} catch (NumberFormatException e2) {

					System.out.println(SellQuantTextField.getText());
					JOptionPane.showMessageDialog(frame, INVALID_QUANTITY);
					return;
				}
				if (buyQuantTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, PLEASE_ENTER_QUANTITY);
					return;

				} else if (Integer.parseInt(buyQuantTextField.getText()) <= 0) {

					JOptionPane.showMessageDialog(frame, INVALID_QUANTITY);
					return;

				}
				System.out.println(
						"buy " + (String) buyComboBox.getSelectedItem());
				System.out.println("quant" + buyQuantTextField.getText());

				try {
					user.buy((String) buyComboBox.getSelectedItem(),
							Integer.parseInt(buyQuantTextField.getText()));
				} catch (NotEnoughCapitalException e1) {

					JOptionPane.showMessageDialog(frame, e1.getMessage());

				}
				lblCap.setText(String.valueOf(user.getCapital()));
				lblCapAsShares
						.setText(String.valueOf(user.getCapitalAsShares()));
				UserSharesTableReload.doClick();
			}
		});
		btnBuy.setBounds(216, 501, 89, 23);
		frame.getContentPane().add(btnBuy);

		JButton btnSell = new JButton("Sell");
		btnSell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Integer.parseInt(SellQuantTextField.getText());
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(frame, INVALID_QUANTITY);
					return;
				}
				if (SellQuantTextField.getText().equals("")) {
					JOptionPane.showMessageDialog(frame, PLEASE_ENTER_QUANTITY);
					return;
				} else if (Integer
						.parseInt(SellQuantTextField.getText()) <= 0) {
					JOptionPane.showMessageDialog(frame, INVALID_QUANTITY);
					return;

				}
				System.out.println(
						"sell " + (String) sellComboBox.getSelectedItem());
				System.out.println("quant" + SellQuantTextField.getText());

				try {
					user.sell((String) sellComboBox.getSelectedItem(),
							Integer.parseInt(SellQuantTextField.getText()));

				} catch (SellingMoreThanYouHaveException e1) {

					JOptionPane.showMessageDialog(frame, e1.getMessage());

				} catch (ThisShareIsNotOwnedException e1) {

					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
				lblCap.setText(String.valueOf(user.getCapital()));
				lblCapAsShares
						.setText(String.valueOf(user.getCapitalAsShares()));
				UserSharesTableReload.doClick();
			}
		});
		btnSell.setBounds(692, 501, 89, 23);
		frame.getContentPane().add(btnSell);

		JLabel lblCompanyName = new JLabel("Company Name");
		lblCompanyName.setBounds(177, 422, 91, 18);
		frame.getContentPane().add(lblCompanyName);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(177, 452, 91, 18);
		frame.getContentPane().add(lblQuantity);

		buyQuantTextField = new JTextField();
		buyQuantTextField.setColumns(10);
		buyQuantTextField.setBounds(281, 451, 96, 20);
		frame.getContentPane().add(buyQuantTextField);

		SellQuantTextField = new JTextField();
		SellQuantTextField.setColumns(10);
		SellQuantTextField.setBounds(734, 452, 96, 20);
		frame.getContentPane().add(SellQuantTextField);

		JLabel lblSellCompanyName = new JLabel("Company Name");
		lblSellCompanyName.setBounds(630, 423, 91, 18);
		frame.getContentPane().add(lblSellCompanyName);

		JLabel lblSellQuantity = new JLabel("Quantity");
		lblSellQuantity.setBounds(630, 453, 91, 18);
		frame.getContentPane().add(lblSellQuantity);

		JLabel lblName_1 = new JLabel("Name =");
		lblName_1.setBounds(198, 11, 89, 14);
		frame.getContentPane().add(lblName_1);

		JLabel lblAge_1 = new JLabel("Age =");
		lblAge_1.setBounds(198, 37, 69, 14);
		frame.getContentPane().add(lblAge_1);

		JLabel lblCap_1 = new JLabel("Capital =");
		lblCap_1.setBounds(481, 11, 125, 14);
		frame.getContentPane().add(lblCap_1);

		JLabel lblCapAsShares_1 = new JLabel("Capital As Shares =");
		lblCapAsShares_1.setBounds(481, 36, 123, 14);
		frame.getContentPane().add(lblCapAsShares_1);

		JButton btnHomeButton = new JButton("Home");
		btnHomeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HomePageGUI.HomePage();
				frame.dispose();
			}
		});
		btnHomeButton.setBounds(460, 157, 89, 23);
		frame.getContentPane().add(btnHomeButton);

		JButton btnTransHis = new JButton("View Transaction History");
		btnTransHis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TransactionHistoryPageGUI.transactionHistoryGUI(uid);
			}
		});
		btnTransHis.setBounds(668, 157, 155, 23);
		frame.getContentPane().add(btnTransHis);

	}
}
