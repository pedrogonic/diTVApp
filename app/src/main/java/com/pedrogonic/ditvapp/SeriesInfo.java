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
    ArrayList<String> headers;

    Series series = new Series();

    HashMap<String, List<String>> listDataChild;
    ExpandableListAdapter expAdapter;

    String seriesId;
    String seriesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_info);
        //setContentView(R.layout.my_layout);

        final TextView seriesNameView = (TextView)findViewById(R.id.seriesName);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            seriesId = bundle.getString("id");
            seriesName = bundle.getString("name");

            seriesNameView.setText(seriesName);

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

            for (int i = 0; i < nodeList.getLength(); i +=6) {
                Episode ep = new Episode();
                ep.setId(Integer.parseInt(nodeList.item(i).getChildNodes().item(0).getNodeValue()));
                ep.setName(nodeList.item(i+1).getChildNodes().item(0).getNodeValue());
                ep.setNumber(Integer.parseInt(nodeList.item(i+2).getChildNodes().item(0).getNodeValue()));
                ep.setFirstAired(nodeList.item(i+3).getChildNodes().item(0).getNodeValue());
                ep.setSeason(Integer.parseInt(nodeList.item(i+4).getChildNodes().item(0).getNodeValue()));
                ep.setAbsoluteNumber(Integer.parseInt(nodeList.item(i+5).getChildNodes().item(0).getNodeValue()));
                ep.setSeries_id(Integer.parseInt(seriesId));
                String seasonName = nodeList.item(i+4).getChildNodes().item(0).getNodeValue();

                series.addEpisode(ep,seasonName);
            }

        } catch (Exception e) {
            Log.v("exception", "==== "+e.getMessage());
        }
    }

    public void showEpisodes(final LinearLayout myLayout,final String seriesId) {
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

                        listDataChild = series.createHashMap();

                        headers = series.getSeasonsNames();

                        expAdapter = new ExpandableListAdapter(context, headers, listDataChild);

                        expListView.setAdapter(expAdapter);

                        myLayout.addView(expListView);

                        progress.dismiss();
                    }
                });
            }
        }).start();
    }

    void task2(String str) {
        String urlStr = "axis2/services/diTVWs/getSeries?id=" + str;

        urlStr = version.getWSUrl(urlStr);

        try {
            mainDoc = HttpHelper.getXMLFromWeb(urlStr);

            NodeList nodeList = mainDoc.getElementsByTagName("ns:return");

            series.setId(Integer.parseInt(nodeList.item(0).getChildNodes().item(0).getNodeValue()));
            series.setName(nodeList.item(1).getChildNodes().item(0).getNodeValue());
            series.setAirsDayOfWeek(nodeList.item(2).getChildNodes().item(0).getNodeValue());
            series.setAirsTime(nodeList.item(3).getChildNodes().item(0).getNodeValue());
            series.setNetwork(nodeList.item(4).getChildNodes().item(0).getNodeValue());
            series.setRating(Integer.parseInt(nodeList.item(5).getChildNodes().item(0).getNodeValue()));

        } catch (Exception e) {
            Log.v("exception", "==== "+e.getMessage());
        }
    }

    public void saveSeriesInfo(final String seriesId) {
        progress = new ProgressDialog(context);
        progress.setMessage("Saving Favorite");
        progress.setTitle("wait");
        // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(true);
        progress.setProgress(0);
        progress.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                task2(seriesId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DBHelper db = new DBHelper(getApplicationContext());
                        db.addSeries(series);
                        progress.dismiss();

                        Intent intent = new Intent(context, FavoriteActivity.class);
                        intent.putExtra("seriesId",seriesId);
                        intent.putExtra("seriesName",seriesName);
                        startActivity(intent);
                    }
                });
            }
        }).start();
    }


    public void favorite(View view) {
        saveSeriesInfo(seriesId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            Intent intent = new Intent(context, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.menu_favorites) {
            Intent intent = new Intent(context, ListFavoriteActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
