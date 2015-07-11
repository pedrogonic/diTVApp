package com.pedrogonic.ditvapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public static final String DATABASE_NAME = "diTV.db";

    public static final String EPISODE_TABLE = "episode";
    public static final String SERIES_TABLE = "series";
    public static final String ALERTS_TABLE = "alert";

    public static final String EPISODE_COLUMN_ID = "episode_id";
    public static final String EPISODE_COLUMN_NAME = "episode_name";
    public static final String EPISODE_COLUMN_NUMBER = "episode_number";
    public static final String EPISODE_COLUMN_FIRSTAIRED = "episode_firstaired";
    public static final String EPISODE_COLUMN_SEASONNUMBER = "episode_seasonnumber";
    public static final String EPISODE_COLUMN_ABSOLUTE_NUMBER = "episode_absolute_number";
    public static final String EPISODE_COLUMN_SERIES_ID = "episode_series_id";

    public static final String SERIES_COLUMN_ID = "series_id";
    public static final String SERIES_COLUMN_NAME = "series_name";
    public static final String SERIES_COLUMN_AIRS_DAYOFWEEK = "series_airs_dayofweek";
    public static final String SERIES_COLUMN_AIRS_TIME = "series_airs_time";
    public static final String SERIES_COLUMN_NETWORK = "series_network";
    public static final String SERIES_COLUMN_RATING = "series_rating";

    public static final String ALERT_COLUMN_SERIES_ID = "alert_series_id";
    public static final String ALERT_COLUMN_SWITCH = "alert_series_switch";
    public static final String ALERT_COLUMN_HOURSPRIOR = "alert_hoursprior";


    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + SERIES_TABLE
                + "("+SERIES_COLUMN_ID+" integer, "+SERIES_COLUMN_NAME+" text, "+SERIES_COLUMN_AIRS_DAYOFWEEK+" text, "
                + SERIES_COLUMN_AIRS_TIME+" text, "+SERIES_COLUMN_NETWORK+" text,"+SERIES_COLUMN_RATING+" integer)"
        );
        db.execSQL(
                "create table "+EPISODE_TABLE
                +"("+EPISODE_COLUMN_ID+" integer, "+EPISODE_COLUMN_NAME+" text, "+EPISODE_COLUMN_NUMBER+" integer, "
                +EPISODE_COLUMN_FIRSTAIRED+" text, "+EPISODE_COLUMN_SEASONNUMBER+" integer, "
                +EPISODE_COLUMN_ABSOLUTE_NUMBER+" integer, "+ EPISODE_COLUMN_SERIES_ID+" integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+SERIES_TABLE);
        onCreate(db);
    }

    public boolean addSeries(Series series) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SERIES_COLUMN_ID, series.getId());
        contentValues.put(SERIES_COLUMN_NAME, series.getName());
        contentValues.put(SERIES_COLUMN_AIRS_DAYOFWEEK, series.getAirsDayOfWeek());
        contentValues.put(SERIES_COLUMN_AIRS_TIME,series.getAirsTime());
        contentValues.put(SERIES_COLUMN_NETWORK,series.getNetwork());
        contentValues.put(SERIES_COLUMN_RATING,series.getRating());

        db.insert(SERIES_TABLE,null,contentValues);

        ArrayList<Episode> episodes = series.getEpisodes();

        for (Episode e : episodes) {
            contentValues = new ContentValues();
            contentValues.put(EPISODE_COLUMN_ID,e.getId());
            contentValues.put(EPISODE_COLUMN_NAME,e.getName());
            contentValues.put(EPISODE_COLUMN_NUMBER,e.getNumber());
            contentValues.put(EPISODE_COLUMN_FIRSTAIRED,e.getFirstAired());
            contentValues.put(EPISODE_COLUMN_SEASONNUMBER,e.getSeason());
            contentValues.put(EPISODE_COLUMN_ABSOLUTE_NUMBER,e.getAbsoluteNumber());
            contentValues.put(EPISODE_COLUMN_SERIES_ID,e.getSeries_id());

            db.insert(EPISODE_TABLE,null,contentValues);
        }

        db.close();

        return true;
    }

    public boolean removeSeries(String seriesId) {
        SQLiteDatabase db = this.getWritableDatabase();

        //DELETE ALERTS!
        db.delete(EPISODE_TABLE,EPISODE_COLUMN_SERIES_ID+" = "+seriesId,null);
        db.delete(SERIES_TABLE,SERIES_COLUMN_ID+" = "+ seriesId,null);

        db.close();

        return true;
    }

    public ArrayList<Series> getFavoriteSeries() {
        ArrayList<Series> seriesList = new ArrayList<Series>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =db.rawQuery("select * from "+SERIES_TABLE,null);
        c.moveToFirst();
        while(c.isAfterLast() == false) {
            Series series = new Series();

            series.setId(c.getInt(c.getColumnIndex(SERIES_COLUMN_ID)));
            series.setName(c.getString(c.getColumnIndex(SERIES_COLUMN_NAME)));

            seriesList.add(series);

            c.moveToNext();
        }

        db.close();
        c.close();

        return seriesList;
    }

    public Series getSeriesEpisodes(int seriesId) {
        Series series = new Series();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+EPISODE_TABLE+" where "+EPISODE_COLUMN_SERIES_ID+" = ?",new String[] {Integer.toString(seriesId)});
        c.moveToFirst();
        while(c.isAfterLast() == false) {
            Episode episode = new Episode();

            episode.setSeries_id(seriesId);
            episode.setId(c.getInt(c.getColumnIndex(EPISODE_COLUMN_ID)));
            episode.setName(c.getString(c.getColumnIndex(EPISODE_COLUMN_NAME)));
            episode.setNumber(c.getInt(c.getColumnIndex(EPISODE_COLUMN_NUMBER)));
            episode.setFirstAired(c.getString(c.getColumnIndex(EPISODE_COLUMN_FIRSTAIRED)));
            episode.setSeason(c.getInt(c.getColumnIndex(EPISODE_COLUMN_SEASONNUMBER)));
            episode.setAbsoluteNumber(c.getInt(c.getColumnIndex(EPISODE_COLUMN_ABSOLUTE_NUMBER)));

            series.addEpisode(episode,Integer.toString(c.getInt(c.getColumnIndex(EPISODE_COLUMN_SEASONNUMBER))));

            c.moveToNext();
        }

        db.close();
        c.close();

        return series;
    }
}
