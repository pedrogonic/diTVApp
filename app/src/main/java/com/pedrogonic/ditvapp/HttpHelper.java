package com.pedrogonic.ditvapp;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HttpHelper {

    static void allowNetworkOnUIThread() {
        // allow network in the UI thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    static Document getXMLFromWeb(String urlStr) throws IOException, ParserConfigurationException, SAXException {
        InputStream in;
        in = OpenHttpConnection(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder;
        builder = dbf.newDocumentBuilder();
        return builder.parse(in);
    }


    static String getResponse(String urlString, Context context) {
        // Toast.makeText(getApplicationContext(), "getResponse begin",
        // Toast.LENGTH_LONG).show();

        String response = null;
        HttpClient httpclient = null;
        try {
            URL url = new URL(urlString);
            HttpGet httpget = new HttpGet(url.toURI());
            httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(httpget);

            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Toast.makeText(context,
                        "statusCode != HttpStatus.SC_OK", Toast.LENGTH_LONG)
                        .show();
                throw new Exception("Got HTTP " + statusCode + " ("
                        + httpResponse.getStatusLine().getReasonPhrase() + ')');
            }

            if (httpResponse.getEntity() == null)
                Toast.makeText(context,
                        "httpResponse.getEntity().toString()==null",
                        Toast.LENGTH_LONG).show();
            response = EntityUtils.toString(httpResponse.getEntity(),
                    HTTP.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (httpclient != null) {
                httpclient.getConnectionManager().shutdown();
                httpclient = null;
            }
        }
        return response;
    }

    static InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        // Toast.makeText(getApplicationContext(),
        // "urlString=" + urlString , Toast.LENGTH_LONG).show();

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            // if (!(conn instanceof HttpURLConnection))
            // throw new IOException("Not an HTTP connection");

            HttpURLConnection httpConn = (HttpURLConnection) conn;

            // httpConn.setAllowUserInteraction(false);
            // httpConn.setInstanceFollowRedirects(false);
            // httpConn.setRequestMethod("GET");
            // httpConn.setUseCaches(false);
            // httpConn.setConnectTimeout(25000 /* milliseconds */);
            // httpConn.setReadTimeout(15000/*10000*/ /* milliseconds */);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);

            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                // in = httpConn.getInputStream();
                in = new BufferedInputStream(httpConn.getInputStream());
            } else {
                throw new Exception();
            }

        } catch (Exception ex) {
            Log.v("exception", "==== " + ex.getMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    static void getTextContent(Node node, StringBuffer sb) throws DOMException {
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.TEXT_NODE) {
                sb.append(child.getNodeValue());
            } else {
                getTextContent(child, sb);
            }
            child = child.getNextSibling();
        }
    }

    static String node2string(Node node) throws DOMException {
        String textContent = "";

        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            textContent = node.getNodeValue();
        } else {
            Node child = node.getFirstChild();
            if (child != null) {
                Node sibling = child.getNextSibling();
                if (sibling != null) {
                    StringBuffer sb = new StringBuffer();
                    getTextContent(node, sb);
                    textContent = sb.toString();
                } else {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        textContent = child.getNodeValue();
                    } else {
                        textContent = node2string(child);
                    }
                }
            }
        }

        return textContent;
    }

}
