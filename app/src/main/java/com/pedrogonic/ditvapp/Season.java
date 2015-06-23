package com.pedrogonic.ditvapp;

import java.util.ArrayList;


public class Season {
    private ArrayList<Episode> season;

    private String name;

    public Season(String name) {
        this.season = new ArrayList();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addEpisode(Episode ep) {
        this.season.add(ep);
    }

    public ArrayList<Episode> getEpisodes() {
        return this.season;
    }

    public ArrayList<String> getEpisodeNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i<this.season.size();i++)
            names.add(this.season.get(i).getName());
        return names;
    }
}
