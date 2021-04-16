package DataManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.DoesNotExistException;

public class DatabaseReader {

	public static String[] getShareInfo(String companyName)
			throws DoesNotExistException {

		String[] shareInfo = new String[10];
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
			String sql = "SELECT ID, COMPANY_NAME, LTP , CHANGE , P_CHANGE ,"
					+ "VOL , TURNOVER , MONTH_P_CHNG , YEAR_P_CHNG, LINK FROM "
					+ "Nifty50Indices WHERE COMPANY_NAME = ?";
			PreparedStatement smnt = conn.prepareStatement(sql);
			smnt.setString(1, companyName);
			ResultSet r = smnt.executeQuery();
			if (r.isClosed()) {
				throw new DoesNotExistException(
						"There is no share of the given Company name");
			}
			while (r.next()) {
				shareInfo[0] = r.getString("ID");
				shareInfo[1] = r.getString("COMPANY_NAME");
				shareInfo[2] = r.getString("LTP");
				shareInfo[3] = r.getString("CHANGE");
				shareInfo[4] = r.getString("P_CHANGE");
				shareInfo[5] = r.getString("VOL");
				shareInfo[6] = r.getString("TURNOVER");
				shareInfo[7] = r.getString("MONTH_P_CHNG");
				shareInfo[8] = r.getString("YEAR_P_CHNG");
				shareInfo[9] = r.getString("LINK");
			}

			conn.close();
			smnt.close();
			r.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return shareInfo;

	}

	public static ResultSet getShareInfo() {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
			String sql = "SELECT * FROM Nifty50Indices";
			PreparedStatement ps;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;
	}

	public static String[] getUserInfo(String name)
			throws DoesNotExistException {

		String[] userInfo = new String[7];
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
			String sql = "SELECT USER_ID, NAME, PASSWORD , EMAIL , AGE ,"
					+ "CAPITAL , CAPITAL_AS_SHARES FROM "
					+ "Users WHERE NAME = ?";
			PreparedStatement smnt = conn.prepareStatement(sql);
			smnt.setString(1, name);
			ResultSet r = smnt.executeQuery();
			if (r.isClosed()) {
				throw new DoesNotExistException(
						"There is no User of the given name");
			}
			while (r.next()) {
				userInfo[0] = r.getString("USER_ID");
				userInfo[1] = r.getString("NAME");
				userInfo[2] = r.getString("PASSWORD");
				userInfo[3] = r.getString("EMAIL");
				userInfo[4] = r.getString("AGE");
				userInfo[5] = r.getString("CAPITAL");
				userInfo[6] = r.getString("CAPITAL_AS_SHARES");
			}

			conn.close();
			smnt.close();
			r.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return userInfo;

	}

}
