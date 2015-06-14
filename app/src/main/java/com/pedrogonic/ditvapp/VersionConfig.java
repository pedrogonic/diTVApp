package com.pedrogonic.ditvapp;

/**
 * Created by pedrogonic on 6/11/15.
 */
public class VersionConfig {
    //Uncomment one of the following methods for the webservice connection:

    //Test in AVD emulator:
    static final String METHOD = "EMULATOR";

    //Test in a device in the same Wi-Fi (ifconfig):
    //static final String METHOD = "WIFI";

    //Test in a device that's sharing hotspot:
    //static final String METHOD = "HOTSPOT";

    //Cloud Web Service:
    //static final String METHOD = "CLOUD";

    static final String PORT = "8080";

    static final String WIFIIP = "172.20.10.3";

    static final String HOTSPOTIP = "192.168.43.73";

    static final String CLOUDADDRESS = "";

    public String getWSUrl(String page) {
        String url = "";
        switch(METHOD) {
            case "EMULATOR": url = "http://10.0.2.2:" + PORT + "/" + page; break;
            case "WIFI": url = "http://" + WIFIIP + ":" + PORT + "/" + page; break;
            case "HOTSPOT": url = "http://" + HOTSPOTIP + ":" + PORT + "/" + page; break;
            case "CLOUD": url = "http://" + CLOUDADDRESS + "/" + page; break;
        }
        return url;
    }
}
