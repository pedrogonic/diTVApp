package com.pedrogonic.ditvapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListFavoriteActivity extends ActionBarActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite);

        DBHelper db = new DBHelper(context);
        ArrayList<Series> seriesList = db.getFavoriteSeries();

        if (seriesList.size() > 0) {
            TextView emptyText = (TextView)findViewById(R.id.emptyText);
            emptyText.setVisibility(View.GONE);

            ListView favoriteList = (ListView)findViewById(R.id.favoriteList);

            final ArrayList<String> result = new ArrayList<String>();
            ArrayAdapter<String> adapter;
            final ArrayList<String>ids = new ArrayList<String>();

            for (Series s : seriesList) {
                result.add(s.getName());
                ids.add(Integer.toString(s.getId()));
            }

            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, result);
            favoriteList.setAdapter(adapter);

            favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, FavoriteActivity.class);
                intent.putExtra("seriesId",ids.get(position));
                intent.putExtra("seriesName",result.get(position));
                startActivity(intent);
                }
            });
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
