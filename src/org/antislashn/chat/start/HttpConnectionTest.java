package org.antislashn.chat.start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionTest {

	public static void main(String[] args) throws IOException {
		URL urlServer = new URL("http://www.perdu.com");
		HttpURLConnection connection = (HttpURLConnection) urlServer.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		String line;
		while((line = reader.readLine()) != null){
			System.out.println(line);
		}
		connection.disconnect();

	}

}
