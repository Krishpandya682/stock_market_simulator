package NewReader;

//Key - 200f5dca44d349ee98ea43fd22473a9d
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class testJSONParserFromAPI {
	public static void main(String[] args) {
		try {
			String url = "http://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=200f5dca44d349ee98ea43fd22473a9d";
			URL obj = new URL(url);
			HttpURLConnection con;

			con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			// add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print in String
			// System.out.println(response.toString());
			JSONObject myResponse = new JSONObject(response.toString());
			JSONArray articles = myResponse.getJSONArray("articles");

			for (Object article : articles) {
				JSONObject articleObj = (JSONObject) article;
				String author = articleObj.get("author").toString();
				String title = articleObj.get("title").toString();
				String description = articleObj.get("description").toString();
				String content = articleObj.get("content").toString();
				String artileURL = articleObj.get("url").toString();

				System.out.println("Author - " + author);
				System.out.println("Title - " + title);
				System.out.println("Description -" + description);
				System.out.println("Content -" + content);
				System.out.println("URL - " + artileURL);
				System.out.println("-------------------------------");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}