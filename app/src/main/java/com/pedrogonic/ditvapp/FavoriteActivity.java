package com.pedrogonic.ditvapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FavoriteActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_favorite);

        final TextView seriesNameView = (TextView)findViewById(R.id.seriesName);
        final ExpandableListView expListView = new ExpandableListView(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            seriesId = bundle.getString("seriesId");
            seriesName = bundle.getString("seriesName");
            seriesNameView.setText(seriesName);
        }

        DBHelper db = new DBHelper(context);
        series = db.getSeriesEpisodes(Integer.parseInt(seriesId));

        listDataChild = series.createHashMap();
        headers = series.getSeasonsNames();
        expAdapter = new ExpandableListAdapter(context, headers, listDataChild);
        expListView.setAdapter(expAdapter);

        LinearLayout myLayout = (LinearLayout)findViewById(R.id.main);

        myLayout.addView(expListView);
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
