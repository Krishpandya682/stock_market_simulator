package Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import Shares.Shares;
import TempDriver.UsersDatabaseGenerator;

public class TransactionHistoryDatabaseGenerator {

	public static void transactionHistoryDatabaseGenerator() {
		Connection con = null;

		String sql = "CREATE TABLE IF NOT EXISTS TransactionHistory "
				+ "(ID INTEGER  PRIMARY KEY  AUTOINCREMENT  NOT NULL, "
				+ "BUY_OR_SELL TEXT  NOT NULL,                 "
				+ "DATE TEXT NOT NULL," + "TIME TEXT NOT NULL,"
				+ " COMPANY_NAME       TEXT   NOT NULL, "
				+ " LTP_THEN    DECIMAL     NOT NULL, "
				+ " QUANTITY    DECIMAL     NOT NULL, "
				+ " QUOTE    DECIMAL     NOT NULL, "
				+ " USER_ID    INTEGER     NOT NULL, "
				+ "FOREIGN KEY (USER_ID) REFERENCES Users(USER_ID));";

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

	public static void transactionHistoryDatabaseUpdater(boolean buy,
			int quantity, Shares share, User user) {
		Connection con = null;
		int uid = UsersDatabaseGenerator.userIDGetter(user.getName());
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			String sql = "INSERT INTO  TransactionHistory"
					+ "(BUY_OR_SELL, DATE, TIME, COMPANY_NAME, LTP_THEN, QUANTITY, QUOTE, USER_ID )"
					+ "VALUES (?,?,?,?,?,?,?, ?)";

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, buy ? "BUY" : "SELL");
			ps.setString(2, LocalDateTime.now().toString().split("T", 2)[0]);
			ps.setString(3, LocalDateTime.now().toString().split("T", 2)[1]);
			ps.setString(4, share.getCompanyName());
			ps.setDouble(5, share.getLtp());
			ps.setInt(6, quantity);
			ps.setDouble(7, buy ? share.getLtp() * quantity
					: share.getLtp() * quantity * -1.0);
			ps.setInt(8, uid);
			ps.execute();

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

}
