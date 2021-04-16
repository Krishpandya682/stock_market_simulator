package Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Shares.Shares;
import TempDriver.UsersDatabaseGenerator;

public class SharesDatabaseGenerator {

	public static void sharesDatabaseGenerator() {
		Connection con = null;

		String sql = "CREATE TABLE IF NOT EXISTS Shares "
				+ "(USER_SHARE_ID INTEGER PRIMARY KEY  NOT NULL,"
				+ "SHARE_ID INTEGER  NOT NULL UNIQUE, "
				+ " QUANTITY    DECIMAL     NOT NULL, "
				+ " QUOTE    DECIMAL     NOT NULL, "
				+ " USER_ID    INTEGER     NOT NULL, "
				+ "FOREIGN KEY (USER_ID) REFERENCES Users(USER_ID)"
				+ "FOREIGN KEY (SHARE_ID) REFERENCES Nifty50Indices(ID));";

		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
			con.setAutoCommit(false);
			Statement s = con.createStatement();
			s.executeUpdate(sql);
			con.commit();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static void sharesDatabaseUpdater(Shares share) {
		Connection con = null;
		int uid = UsersDatabaseGenerator
				.userIDGetter(share.getCurrUser().getName());
		try {

			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			String sql = "INSERT OR REPLACE INTO Shares "
					+ "(SHARE_ID, QUANTITY, USER_ID, USER_SHARE_ID, QUOTE)"
					+ "VALUES ((SELECT ID FROM Nifty50Indices WHERE COMPANY_NAME = ?),?,?, "
					+ "(SELECT USER_SHARE_ID From Shares WHERE SHARE_ID = "
					+ "((SELECT ID FROM Nifty50Indices WHERE COMPANY_NAME = ?))), "
					+ "((SELECT LTP FROM Nifty50Indices WHERE COMPANY_NAME = ?)*?))";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, share.getCompanyName());
			ps.setInt(2, share.getQuantity());
			ps.setInt(3, uid);
			ps.setString(4, share.getCompanyName());
			ps.setString(5, share.getCompanyName());
			ps.setInt(6, share.getQuantity());

			ps.execute();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static void emptyShareRemover() {
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			String sql = "DELETE FROM Shares WHERE QUANTITY = 0";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.execute();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static ArrayList<Shares> returnSharesOfUser(String username) {
		System.out.println("userName = " + username);
		ArrayList<Shares> a = new ArrayList<Shares>();
		Connection con = null;
		int uid = -1;
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			uid = UsersDatabaseGenerator.userIDGetter(username);
			System.out.println("UID" + uid);
			String shareGetter = "SELECT * FROM Shares INNER JOIN Nifty50Indices"
					+ " ON Shares.SHARE_ID = Nifty50Indices.ID WHERE USER_ID = ?";
			PreparedStatement st = con.prepareStatement(shareGetter);
			st.setInt(1, uid);
			ResultSet rs2 = st.executeQuery();
			while (rs2.next()) {

				Shares share = new Shares(rs2.getString("COMPANY_NAME"),
						Integer.parseInt(rs2.getString("QUANTITY")), uid);
				a.add(share);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return a;
	}

}
