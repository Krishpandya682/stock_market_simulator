package GUI;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.proteanit.sql.DbUtils;

public class TransactionHistoryPageGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void transactionHistoryGUI(int uid) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TransactionHistoryPageGUI window = new TransactionHistoryPageGUI(
							uid);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection con = null;
	private JTable table;

	/**
	 * Create the application.
	 */
	public TransactionHistoryPageGUI(int uid) {
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize(uid);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(int uid) {
		frame = new JFrame();
		frame.setBounds(100, 100, 1024, 768);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(44, 66, 812, 475);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		try {
			String sql = "SELECT * FROM TransactionHistory WHERE USER_ID = ?";
			PreparedStatement ps;
			ps = con.prepareStatement(sql);
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}

}
