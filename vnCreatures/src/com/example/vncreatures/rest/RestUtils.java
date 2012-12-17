package com.example.vncreatures.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.example.vncreatures.common.ServerConfig;

import android.util.Log;

public class RestUtils {
	private static final String TAG = RestUtils.class.toString();
	/*
	 * Return results
	 */
	public static String postData(final String url) {
		HttpClient httpClient = null;
		try {
			HttpPost httpPost = new HttpPost(url);

			// BEGIN-Setting TIME-OUT params
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			int timeoutConnection = ServerConfig.TIMEOUT;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = ServerConfig.TIMEOUT;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			// Create a new HttpClient and Post Header
			httpClient = new DefaultHttpClient(httpParameters);
			// END-Setting TIME-OUT params

			// Set Session Id
			//httpPost.addHeader("Cookie", "ci_session=" + fchkController.getLoginPhpSessId());			

			// Execute HTTP Post Request
			HttpResponse postResponse = httpClient.execute(httpPost);
			HttpEntity resEntity = postResponse.getEntity();  
			if (resEntity == null)
				return "";

			String response = EntityUtils.toString(resEntity);
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpClient.getConnectionManager().shutdown();
		}

		return "";
	}

	/*
	 * Return results
	 */
	public static String loadData(final String url, final String session) {
		HttpClient httpClient = null;
		try {
			// BEGIN-Setting TIME-OUT params
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			int timeoutConnection = ServerConfig.TIMEOUT;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = ServerConfig.TIMEOUT;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			// Create a new HttpClient and Post Header
			httpClient = new DefaultHttpClient(httpParameters);
			// END-Setting TIME-OUT params

			StringBuilder builder = new StringBuilder();
			HttpGet httpGet = new HttpGet(url);

			if (!session.isEmpty()) {
				// Set Session Id
				httpGet.addHeader("Cookie", "ci_session=" + session);	
			}
			
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));

				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				content.close();

				return builder.toString();
			} 
		} catch (ClientProtocolException e) {
			Log.i(TAG, e.toString());
		} catch (IOException e) {
			String error = e.toString();
			Log.i(TAG, error);
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpClient.getConnectionManager().shutdown();
		}
		
		return "";
	}	

	public static String loadData(final String url) {
		return loadData(url, "");
	}
}
