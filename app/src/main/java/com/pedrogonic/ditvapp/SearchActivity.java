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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;


public class SearchActivity extends ActionBarActivity {

    VersionConfig version = new VersionConfig();

    Document mainDoc;
    ProgressDialog progress;
    Context context = this;
    ArrayList<String> result;
    ArrayAdapter<String>adapter;
    ArrayList<String>ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }


    void task(String str) {
        String urlStr = "axis2/services/diTVWs/search?search=" + str;

        urlStr = version.getWSUrl(urlStr);

        try {
            mainDoc = HttpHelper.getXMLFromWeb(urlStr);

            NodeList nodeList = mainDoc.getElementsByTagName("ns:return");

            ids = new ArrayList();

            result = new ArrayList<String>();
            Log.v("result", "==== "+ nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i +=2) {
                ids.add(nodeList.item(i).getChildNodes().item(0).getNodeValue());
                result.add(nodeList.item(i+1).getChildNodes().item(0).getNodeValue());
            }

        } catch (Exception e) {
            Log.v("exception", "==== "+e.getMessage());
        }
    }

    public void doSearch(View view) {
        final TextView searchStringView = (TextView)findViewById(R.id.searchStr);
        final String searchString = searchStringView.getText().toString();
        final ListView listView = (ListView)findViewById(R.id.searchResult);



        progress = new ProgressDialog(context);
        progress.setMessage("accessing the cloud");
        progress.setTitle("wait");
        // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(true);
        progress.setProgress(0);
        progress.show();

        task(searchString);

        new Thread(new Runnable() {
            @Override
            public void run() {
                task(searchString);
                Log.v("test", "==== test");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Log.v("taskdone", "==== search");
                        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, result);
                        listView.setAdapter(adapter);
                        Utils utils = new Utils();
                        utils.hideKeyboard(context, getWindow());
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(context, SeriesInfo.class);
                                intent.putExtra("id",ids.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }).start();
    }

//    protected void chooseSeries(ListView l, View v, final int position, long id) {
//        //super.onListItemClick(l, v, position, id);
//        Log.v("click","==== "+ position);
//    }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
