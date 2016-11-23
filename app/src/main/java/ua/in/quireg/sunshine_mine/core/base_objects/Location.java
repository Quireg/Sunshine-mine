package ua.in.quireg.sunshine_mine.core.base_objects;

import android.util.Log;

/**
 * Created by Arcturus on 11/23/2016.
 */

public class Location {
    private long id;
    private String name;
    private double lat;
    private double lon;
    private String countryCode;

    public Location(long id, String name, double lat, double lon, String countryCode) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.countryCode = countryCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public static Location[] getFakeLocationsArray(){
        Location[] loc = new Location[10];
        int i = 0;
        while (i < 10){
            loc[i] = new Location(123, "test", 12, 12, "ASd");
            i++;
        }
        return loc;
    }

}
