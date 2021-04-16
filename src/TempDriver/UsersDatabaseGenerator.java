package TempDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Users.User;

public class UsersDatabaseGenerator {

	public static void usersDatabaseGenerator() {
		Connection con = null;

		String sql = "CREATE TABLE IF NOT EXISTS Users "
				+ "(USER_ID INTEGER  PRIMARY KEY   AUTOINCREMENT    NOT NULL, "
				+ " NAME           TEXT  NOT NULL UNIQUE ,"
				+ " PASSWORD           TEXT  NOT NULL,"
				+ " EMAIL                     TEXT NOT NULL,"
				+ " AGE    INTEGER     NOT NULL, "
				+ " CAPITAL    DECIMAL     NOT NULL, "
				+ " CAPITAL_AS_SHARES    DECIMAL     NOT NULL) ";

		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			Statement s = con.createStatement();
			s.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void userDatabasePopulator(User user) {
		try {
			Connection con = null;

			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			String sql = "INSERT OR REPLACE INTO Users "
					+ "(USER_ID, NAME, AGE, CAPITAL, CAPITAL_AS_SHARES, PASSWORD, EMAIL)"
					+ "VALUES ((SELECT USER_ID FROM Users WHERE NAME = ?),"
					+ "?,?,?,?,?,?)";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setString(2, user.getName());
			ps.setInt(3, user.getAge());
			ps.setDouble(4, user.getCapital());
			ps.setDouble(5, user.getCapitalAsShares());
			ps.setString(6, user.getPwd());
			ps.setString(7, user.getEmail());

			ps.execute();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static int userIDGetter(String username) {
		Connection con = null;
		int uid = 0;
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			String userIdGetter = "SELECT * FROM Users WHERE NAME = ?";
			PreparedStatement stUIG = con.prepareStatement(userIdGetter);
			stUIG.setString(1, username);
			ResultSet rs = stUIG.executeQuery();
			while (rs.next()) {

				uid = Integer.parseInt(rs.getString("USER_ID"));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return uid;
	}

}
