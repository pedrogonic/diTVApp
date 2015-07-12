package com.pedrogonic.ditvapp;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Series {
    private ArrayList<Season> series;

    private int id;
    private String name;
    private String airsDayOfWeek;
    private String airsTime;
    private String network;
    private int rating;

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

    public ArrayList<Episode> getEpisodes() {
        ArrayList res = new ArrayList();

        for (Season s : series) {
            ArrayList<Episode> episodes = s.getEpisodes();
            for (Episode e : episodes)
                res.add(e);
        }
        return res;
    }

    public Episode getSelectedEpisode(int season, int episode) {
        return series.get(season).getEpisode(episode);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirsDayOfWeek() {
        return airsDayOfWeek;
    }

    public void setAirsDayOfWeek(String airsDayOfWeek) {
        this.airsDayOfWeek = airsDayOfWeek;
    }

    public String getAirsTime() {
        return airsTime;
    }

    public void setAirsTime(String airsTime) {
        this.airsTime = airsTime;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
