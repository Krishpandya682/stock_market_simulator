package DataManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseGenerator {

	public static void createDatabaseTable() {

		String sql = "CREATE TABLE IF NOT EXISTS Nifty50Indices "
				+ "(ID INTEGER  PRIMARY KEY  AUTOINCREMENT  NOT NULL, "
				+ " COMPANY_NAME       TEXT   NOT NULL UNIQUE, "
				+ " LTP    DECIMAL     NOT NULL, "
				+ " CHANGE    DECIMAL     NOT NULL, "
				+ " P_CHANGE    DECIMAL     NOT NULL, "
				+ " VOL    DECIMAL     NOT NULL, "
				+ " TURNOVER    DECIMAL     NOT NULL, "
				+ " MONTH_P_CHNG    DECIMAL     NOT NULL, "
				+ " YEAR_P_CHNG    DECIMAL     NOT NULL, "
				+ " LINK    TEXT     NOT NULL )";

		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
			System.out.println("Opened database successfully");
			Statement s = con.createStatement();

			s.executeUpdate(sql);
			s.close();
			con.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void insertInDatabaseTable() {
		String[][] data = DataParser.ParseData();

		String sql = "INSERT OR REPLACE INTO Nifty50Indices "
				+ "(ID, COMPANY_NAME, LTP , CHANGE , P_CHANGE , "
				+ "VOL , TURNOVER , MONTH_P_CHNG , YEAR_P_CHNG, LINK)"
				+ "VALUES ((SELECT ID FROM Nifty50Indices WHERE COMPANY_NAME = ?),"
				+ "?,?,?,?,?,?,?,?,?)";

		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");

			PreparedStatement smnt = con.prepareStatement(sql);

			for (int i = 0; i < data.length; i++) {
				smnt.setString(1, data[i][0]);
				smnt.setString(2, data[i][0]);
				smnt.setDouble(3, Double.valueOf(data[i][1]));
				smnt.setDouble(4, Double.valueOf(data[i][2]));
				smnt.setDouble(5, Double.valueOf(data[i][3]));
				smnt.setDouble(6, Double.valueOf(data[i][4]));
				smnt.setDouble(7, Double.valueOf(data[i][5]));
				smnt.setDouble(8, Double.valueOf(data[i][6]));
				smnt.setDouble(9, Double.valueOf(data[i][7]));
				smnt.setString(10, data[i][8]);
				;

				smnt.execute();

			}
			smnt.close();
			con.close();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Table Not Populated");
			e.printStackTrace();
		}

	}

	public static void updateDatabaseTable() {
		String[][] data = DataParser.ParseData();

		String sql = "UPDATE Nifty50Indices "
				+ "SET LTP = ? , CHANGE =? , P_CHANGE =? , "
				+ "VOL = ?, TURNOVER=? , MONTH_P_CHNG = ? , YEAR_P_CHNG = ? WHERE COMPANY_NAME = ?";

		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection(
					"jdbc:sqlite:c:\\Users\\krish\\eclipse-workspace\\StockMarketSimulator\\db\\dbNifty50.db");
			System.out.println("Opened database successfully");

			PreparedStatement smnt = con.prepareStatement(sql);

			for (int i = 0; i < data.length; i++) {
				smnt.setDouble(1, Double.valueOf(data[i][1]));
				smnt.setDouble(2, Double.valueOf(data[i][2]));
				smnt.setDouble(3, Double.valueOf(data[i][3]));
				smnt.setDouble(4, Double.valueOf(data[i][4]));
				smnt.setDouble(5, Double.valueOf(data[i][5]));
				smnt.setDouble(6, Double.valueOf(data[i][6]));
				smnt.setDouble(7, Double.valueOf(data[i][7]));
				smnt.setString(8, data[i][0]);
				smnt.executeUpdate();

			}
			smnt.close();
			con.close();
		} catch (SQLException | ClassNotFoundException e) {

			System.out.println("Table Not Populated");
			e.printStackTrace();
		}

	}

}