package DataManagement;

import TempDriver.UsersDatabaseGenerator;
import Users.SharesDatabaseGenerator;
import Users.TransactionHistoryDatabaseGenerator;

public class DatabasePopulator {

	public static void populate() {
		DatabaseGenerator.createDatabaseTable();
		DatabaseGenerator.insertInDatabaseTable();
		SharesDatabaseGenerator.sharesDatabaseGenerator();
		UsersDatabaseGenerator.usersDatabaseGenerator();
		TransactionHistoryDatabaseGenerator
				.transactionHistoryDatabaseGenerator();

		// FirebaseDatabase fd = new FirebaseDatabase();
		// fd.nifty50IndicesPopulator(DataParser.ParseData());
		// fd.nifty50IndicesReader();
	}
}
