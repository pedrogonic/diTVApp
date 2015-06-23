package com.pedrogonic.ditvapp;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Series {
    private ArrayList<Season> series;

    public Series() {
        this.series = new ArrayList();
    }

    public void addEpisode(Episode ep, String seasonName) {
        if(!this.seasonExists(seasonName))
            this.series.add(new Season(seasonName));
        for (int i = 0;i < series.size();i++) {
            if(seasonName.equals(series.get(i).getName()))
                series.get(i).addEpisode(ep);
        }
    }

    public ArrayList<String> getSeasonsNames() {
        ArrayList<String> seasonsNames = new ArrayList<String>();

        for (int i = 0;i < this.series.size();i++)
            seasonsNames.add("Season " + this.series.get(i).getName());

        return seasonsNames;
    }

    public HashMap<String, List<String>> createHashMap() {
        HashMap<String, List<String>> hm = new HashMap<String, List<String>>();

        for (int i = 0;i < this.series.size();i++)
            hm.put("Season " + this.series.get(i).getName(), this.series.get(i).getEpisodeNames());

        return hm;
    }

    private boolean seasonExists(String seasonName) {
        for (int i = 0;i < series.size();i++) {
            if(seasonName.equals(series.get(i).getName()))
                return true;
        }
        return false;
    }
}
