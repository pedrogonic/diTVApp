package com.pedrogonic.ditvapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SeriesInfo extends ActionBarActivity {

    VersionConfig version = new VersionConfig();

    Document mainDoc;
    ProgressDialog progress;
    Context context = this;
    ArrayList<String> episodeName;
    ArrayList<String>episodeId;
    ArrayList<String> season;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpandableListAdapter expAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_info);
        //setContentView(R.layout.my_layout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            String seriesId = bundle.getString("id");

            LinearLayout myLayout = (LinearLayout)findViewById(R.id.main);

            showEpisodes(myLayout,seriesId);
        }
    }


    void task1(String str) {
        String urlStr = "axis2/services/diTVWs/getEpisodes?seriesId=" + str;

        urlStr = version.getWSUrl(urlStr);

        try {
            mainDoc = HttpHelper.getXMLFromWeb(urlStr);

            NodeList nodeList = mainDoc.getElementsByTagName("ns:return");

            episodeId = new ArrayList();
            episodeName = new ArrayList<String>();
            season = new ArrayList<String>();
            Log.v("result", "==== " + nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i +=3) {
                episodeId.add(nodeList.item(i).getChildNodes().item(0).getNodeValue());
                episodeName.add(nodeList.item(i+1).getChildNodes().item(0).getNodeValue());
                season.add(nodeList.item(i+2).getChildNodes().item(0).getNodeValue());
            }
            Log.v("season","=== season");

        } catch (Exception e) {
            Log.v("exception", "==== "+e.getMessage());
        }
    }

/*    void task2(String str) {

        //Uncomment one of these:
        //Test in emulator:
        //String urlStr = "http://10.0.2.2:8080/
        //Test in a device in the same Wi-Fi (ifconfig):
        String urlStr = "http://192.168.1.191:8080/";
        //Test in a device that's sharing hotspot:
        //String urlStr = "http://192.168.43.73:8080/
        //Cloud Web Service:
        //String urlStr = "http://???/

        urlStr += "axis2/services/diTVWs/getSeasons?seriesId=" + str;

        try {
            mainDoc = HttpHelper.getXMLFromWeb(urlStr);

            NodeList nodeList = mainDoc.getElementsByTagName("ns:return");

            ids = new ArrayList();

            result = new ArrayList<String>();
            Log.v("result", "==== " + nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i +=2) {
                ids.add(nodeList.item(i).getChildNodes().item(0).getNodeValue());
                result.add(nodeList.item(i+1).getChildNodes().item(0).getNodeValue());
            }

        } catch (Exception e) {
            Log.v("exception", "==== "+e.getMessage());
        }
    }*/

    public void showEpisodes(final LinearLayout myLayout,final String seriesId) {
        final ListView listView = new ListView(this);

        final ExpandableListView expListView = new ExpandableListView(this);


        progress = new ProgressDialog(context);
        progress.setMessage("accessing the cloud");
        progress.setTitle("wait");
        // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(true);
        progress.setProgress(0);
        progress.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                task1(seriesId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Log.v("place", "==== "+season.size());

                        listDataHeader = new ArrayList<String>();
                        for (int c = 0 ; c < season.size(); c++)
                            listDataHeader.add(season.get(c));
                        listDataChild = new HashMap<String, List<String>>();

                        ArrayList ar = new ArrayList();
                        ar.add("ep1");
                        ar.add("ep2");

                        for (int c = 0 ; c < season.size(); c++)
                            listDataChild.put(season.get(c),ar);

                        expAdapter = new ExpandableListAdapter(context, season, listDataChild);

                        expListView.setAdapter(expAdapter);

                        myLayout.addView(expListView);

                        progress.dismiss();
                    }
                });
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_series_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
