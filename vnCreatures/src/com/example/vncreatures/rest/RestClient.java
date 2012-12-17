/**
 * Example:
  	RestClient client = new RestClient(LOGIN_URL);
 	client.AddParam("accountType", "GOOGLE");
 	client.AddParam("source", "tboda-widgalytics-0.1");
 	client.AddParam("username", username);
	client.AddParam("password", password);
	client.AddParam("service", "analytics");
	client.AddHeader("GData-Version", "2");
	client.addHeader("Cookie", "ci_session=" + controller.getLoginPhpSessId());	
	try {
	    client.execute(RequestMethod.POST);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	String response = client.getResponse();
 */
package com.example.vncreatures.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.example.vncreatures.common.ServerConfig;

public class RestClient {
	public enum RequestMethod {
		GET,
		POST
	}

	private ArrayList <NameValuePair> mParams;
	private ArrayList <NameValuePair> mHeaders;

	private String mUrl;

	private int mResponseCode;
	private String mMessage;

	private String mResponse;

	public String getResponse() {
		return mResponse;
	}

	public String getErrorMessage() {
		return mMessage;
	}

	public int getResponseCode() {
		return mResponseCode;
	}

	public RestClient(final String url) {
		this.mUrl = url;
		mParams = new ArrayList<NameValuePair>();
		mHeaders = new ArrayList<NameValuePair>();
	}

	public void addParam(final String name, final String value) {
		mParams.add(new BasicNameValuePair(name, value));
	}

	public void addHeader(final String name, final String value) {
		mHeaders.add(new BasicNameValuePair(name, value));
	}

	public void execute(RequestMethod method) throws Exception {
		switch(method) {
		case GET: {
			// Add parameters
			String combinedParams = "";
			if(!mParams.isEmpty()) {
				combinedParams += "?";
				for(NameValuePair p : mParams) {
					String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
					if(combinedParams.length() > 1) {
						combinedParams  +=  "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpGet request = new HttpGet(mUrl + combinedParams);

			// Add headers
			for(NameValuePair h : mHeaders) {
				request.addHeader(h.getName(), h.getValue());
			}

			executeRequest(request, mUrl);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(mUrl);

			// Add headers
			for(NameValuePair h : mHeaders) {
				request.addHeader(h.getName(), h.getValue());
			}

			if(!mParams.isEmpty()) {
				request.setEntity(new UrlEncodedFormEntity(mParams, HTTP.UTF_8));
			}

			executeRequest(request, mUrl);
			break;
		}
		default:
			break;
		}
	}

	private void executeRequest(HttpUriRequest request, final String url) {
		HttpClient client = null;
		HttpResponse httpResponse;
		try {
			// Begin setting TIMEOUT param
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			int timeoutConnection = ServerConfig.TIMEOUT;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT) 
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = ServerConfig.TIMEOUT;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// Create a new HttpClient and Post Header
			client = new DefaultHttpClient(httpParameters);
			// End setting TIMEOUT param

			httpResponse = client.execute(request);
			mResponseCode = httpResponse.getStatusLine().getStatusCode();
			mMessage = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				mResponse = convertStreamToString(instream);

				// Closing the input stream will trigger connection release
				instream.close();
			}
		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
