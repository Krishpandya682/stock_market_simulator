package StockInfo;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import DataManagement.DatabaseReader;
import Exceptions.DoesNotExistException;

public class OpenIndexInfoPage {

	public static void openIndexInfoPage(String CompanyName) {
		String link = null;
		try {
			link = DatabaseReader.getShareInfo(CompanyName)[9];

			System.out.println(DatabaseReader.getShareInfo(CompanyName)[0]);
			System.out.println(DatabaseReader.getShareInfo(CompanyName)[1]);
			System.out.println(DatabaseReader.getShareInfo(CompanyName)[2]);
		} catch (DoesNotExistException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(link);
		try {
			Desktop.getDesktop().browse(new URL(link).toURI());
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
