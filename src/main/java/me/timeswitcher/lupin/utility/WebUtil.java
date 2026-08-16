package me.timeswitcher.lupin.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class WebUtil {

	public static HttpURLConnection openConnection(final String to) throws IOException {
		return (HttpURLConnection) URI.create(to).toURL().openConnection();
	}

	public static String buildResponse(final InputStream source) throws IOException {
		final BufferedReader inputReader = new BufferedReader(new InputStreamReader(source));
		final StringBuilder responseBuilder = new StringBuilder();

		for (String line; (line = inputReader.readLine()) != null;) {
			responseBuilder.append(line);
			responseBuilder.append('\n');
		}
		return responseBuilder.toString();
	}
}