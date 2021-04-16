package DataManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseDatabase {

	Firestore db;

	public FirebaseDatabase() {

		// Use a service account
		InputStream serviceAccount = null;
		GoogleCredentials credentials = null;
		try {
			serviceAccount = new FileInputStream(
					"C:\\Users\\krish\\Downloads\\sharemarketsimulator-firebase-adminsdk-guah7-b4e3ec15d9.json");
			credentials = GoogleCredentials.fromStream(serviceAccount);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(credentials).build();
		FirebaseApp.initializeApp(options);

		db = FirestoreClient.getFirestore();

	}

	public void nifty50IndicesPopulator(String[][] nifty50Indices) {

		// Use a service account
		InputStream serviceAccount = null;
		GoogleCredentials credentials = null;
		try {
			serviceAccount = new FileInputStream(
					"C:\\Users\\krish\\Downloads\\sharemarketsimulator-firebase-adminsdk-guah7-b4e3ec15d9.json");
			credentials = GoogleCredentials.fromStream(serviceAccount);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(credentials).build();

		FirebaseApp.initializeApp(options);

		Firestore db = FirestoreClient.getFirestore();

		for (int i = 0; i < nifty50Indices.length; i++) {
			String name = nifty50Indices[i][0];

			if (db.collection("Nifty50Indices").document(name).equals(null)) {
				DocumentReference docRef = db.collection("Nifty50Indices")
						.document(name);

				// Add document data with id name using a hashmap
				Map<String, Object> data = new HashMap<>();
				data.putIfAbsent("id", i);
				data.putIfAbsent("Company Name", name);
				data.putIfAbsent("LTP", nifty50Indices[i][1]);
				data.putIfAbsent("Change", nifty50Indices[i][2]);
				data.putIfAbsent("Percent Change", nifty50Indices[i][3]);
				data.putIfAbsent("Volume", nifty50Indices[i][4]);
				data.putIfAbsent("Turnover", nifty50Indices[i][5]);
				data.putIfAbsent("Monthly Change", nifty50Indices[i][6]);
				data.putIfAbsent("Yearly Change", nifty50Indices[i][7]);
				data.putIfAbsent("Link", nifty50Indices[i][8]);

				// asynchronously write data
				ApiFuture<WriteResult> result = docRef.set(data);
				// ...
				// result.get() blocks on response
			}
		}
	}

	public Map<String, Object>[] nifty50IndicesReader() {

		ApiFuture<QuerySnapshot> query = db.collection("users").get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = null;
		try {
			querySnapshot = query.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		for (QueryDocumentSnapshot document : documents) {

			System.out.println(document.getData());

		}
		return null;

	}

}
