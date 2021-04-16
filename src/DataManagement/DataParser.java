package DataManagement;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataParser {

	private static String name;
	private static String ltp;
	private static String chng;
	private static String pchng;
	private static String vol;
	private static String turnover;
	private static String monthPChng;
	private static String yearPChng;
	private static String link;

	public static String[][] ParseData() {
		String[][] data = new String[50][9];
		String url = "https://economictimes.indiatimes.com/indices/nifty_50_companies";
		try {
			Response response = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
					.execute();
			Document doc = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
					.header("Accept-Language", "en-US")
					.header("Accept-Encoding", "gzip,deflate,sdch")
					.cookies(response.cookies()).get();
			Elements temp = doc.getElementsByClass("datalist");

			int i = 0;
			for (Element dataPoint : temp) {
				if (i >= 50) {
					break;
				}
				link = "https://economictimes.indiatimes.com" + dataPoint
						.getElementsByAttributeValue("class", "flt w120")
						.select("a").tagName("href").val("href").toString()
						.split("href=\"")[1].split("\"")[0];

				name = dataPoint
						.getElementsByAttributeValue("class", "flt w120")
						.text();
				ltp = dataPoint
						.getElementsByAttributeValue("class", "w70 alignC")
						.text();
				chng = dataPoint
						.getElementsByAttributeValue("class", "w60 alignR")
						.get(0).text();
				pchng = dataPoint
						.getElementsByAttributeValue("class", "w70 alignR")
						.get(0).text();
				vol = dataPoint
						.getElementsByAttributeValue("class", "w50 alignR")
						.get(0).text();
				turnover = dataPoint
						.getElementsByAttributeValue("class", "w60 alignR")
						.get(1).text();
				monthPChng = dataPoint
						.getElementsByAttributeValue("class", "w70 alignR")
						.get(1).text();
				yearPChng = dataPoint
						.getElementsByAttributeValue("class", "w70 alignR")
						.get(2).text();

				data[i][0] = name;
				data[i][1] = ltp;
				data[i][2] = chng;
				data[i][3] = pchng;
				data[i][4] = vol;
				data[i][5] = turnover;
				data[i][6] = monthPChng;
				data[i][7] = yearPChng;
				data[i][8] = link;

				i++;

			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return data;
	}
}