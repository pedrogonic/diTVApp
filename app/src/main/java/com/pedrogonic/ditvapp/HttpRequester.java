package com.pedrogonic.ditvapp;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@SuppressLint("NewApi")
public class HttpRequester {

	private Document doc = null;

	public String getResponse() {

		Element element = (Element) doc.getChildNodes().item(0);
		Node node = element.getElementsByTagName("ns:return").item(0)
				.getChildNodes().item(0);

		Log.i("Debug", "Resultado " + node.getNodeValue());
		Log.i("HttpRequester", "Resultado " + node.getNodeValue());
		Log.i("CatalogVideo",
				"Resultado da requisicao => " + node.getNodeValue());

		return node.getNodeValue();
	}

	public ArrayList<String> getResponses() {

		ArrayList<String> list = new ArrayList<String>();

		Element element = (Element) doc.getChildNodes().item(0);
		NodeList nodeList = element.getElementsByTagName("ns:return");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Log.i("Debug", "Resultado " + nodeList.item(i).getChildNodes().item(0).getNodeValue());
			Log.i("HttpRequester", "Resultado " + nodeList.item(i).getChildNodes().item(0).getNodeValue());
			Log.i("CatalogVideo",
					"Resultado da requisicao => " + nodeList.item(i).getChildNodes().item(0).getNodeValue());
			list.add(nodeList.item(i).getChildNodes().item(0).getNodeValue());
		}

		return list;
	}

	public void setRequest(String url) {

		// allow network in the UI thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Log.i("Debug", "SetRequest this url: " + url);
		Log.i("httpRequester", "SetRequest this url: " + url);

		try {
			InputStream in = OpenHttpConnection(url);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.parse(in);

			Log.i("Debug", "doc set");
			Log.i("httpRequester", "doc set");

		} catch (IOException e) {
			Log.i("Debug", "Failed - OpenHttpConnection");
			Log.i("httpRequester", "Failed - OpenHttpConnection");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Log.i("Debug", "Failed - DocumentBuilderFactory");
			Log.i("httpRequester", "Failed - DocumentBuilderFactory");
			e.printStackTrace();
		} catch (SAXException e) {
			Log.i("Debug", "Failed - parse");
			Log.i("httpRequester", "Failed - parse");
			e.printStackTrace();
		}
	}

	private InputStream OpenHttpConnection(String urlString) throws IOException {

		InputStream in = null;

		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();

			// if (!(conn instanceof HttpURLConnection))
			// throw new IOException("Not an HTTP connection");

			HttpURLConnection httpConn = (HttpURLConnection) conn;

			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			httpConn.connect();

			int response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				// in = httpConn.getInputStream();
				in = new BufferedInputStream(httpConn.getInputStream());
			} else {
				throw new Exception();
			}

		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		return in;
	}

}
