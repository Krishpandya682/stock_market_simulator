package GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import DataManagement.DatabasePopulator;
import DataManagement.DatabaseReader;
import StockInfo.OpenIndexInfoPage;
import net.proteanit.sql.DbUtils;

public class HomePageGUI extends MainProgram {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void HomePage() {
		DatabasePopulator.populate();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					HomePageGUI window = new HomePageGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	Connection con = null;
	private JScrollPane scrollPane;
	private JButton UserProfileButton;
	private JButton btnLogout;
	private JButton btnSignup;

	/**
	 * Create the application.
	 */
	public HomePageGUI() {
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.out.println("is logged in? " + getIsLoggedIn());
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1024, 768);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 113, 990, 337);
		frame.getContentPane().add(scrollPane);

		table = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};

		};

		scrollPane.setViewportView(table);
		table.setBackground(Color.LIGHT_GRAY);
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow() + 1;
					int column = target.getSelectedColumn();
					System.out.println(row + " , " + column);
					System.out.println();
					try {
						String sql = "SELECT * FROM Nifty50Indices";
						PreparedStatement ps;
						ps = con.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						int counter = 0;
						while (rs.next()) {
							counter++;
							if (counter >= row) {
								break;
							}
						}
						
						OpenIndexInfoPage.openIndexInfoPage(
								rs.getString("COMPANY_NAME"));
					} catch (SQLException e1) {

						e1.printStackTrace();
					}
				}
			}
		});

		JButton DataReloadButton = new JButton("Reload");
		DataReloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				table.setModel(DbUtils
						.resultSetToTableModel(DatabaseReader.getShareInfo()));

			}
		});
		DataReloadButton.setBounds(461, 468, 89, 23);
		frame.getContentPane().add(DataReloadButton);

		UserProfileButton = new JButton("MyProfile");
		UserProfileButton.setBackground(Color.WHITE);
		UserProfileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!getIsLoggedIn()) {
					LoginPageGUI login = new LoginPageGUI();
					login.frame.setVisible(true);
					frame.dispose();
				} else {
					UserProfileGUI upg = new UserProfileGUI(
							getLoggedInAsUser());
					upg.frame.setVisible(true);
					frame.dispose();
				}
			}
		});
		UserProfileButton.setBounds(461, 35, 89, 23);
		frame.getContentPane().add(UserProfileButton);

		String inOrOut;
		if (getIsLoggedIn()) {
			inOrOut = "Logout";
		} else {
			inOrOut = "Login";
		}
		btnLogout = new JButton(inOrOut);
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getIsLoggedIn()) {
					setLoggedIn(false);
					setLoggedInAsUser(null);
					frame.dispose();
					HomePage();
				} else {
					LoginPageGUI login = new LoginPageGUI();
					login.frame.setVisible(true);
					frame.dispose();
				}
			}
		});
		btnLogout.setBounds(770, 35, 89, 23);
		frame.getContentPane().add(btnLogout);

		btnSignup = new JButton("Signup");
		btnSignup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SignupPageGUI spg = new SignupPageGUI();
				spg.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnSignup.setBounds(911, 35, 89, 23);
		frame.getContentPane().add(btnSignup);

	}
}
