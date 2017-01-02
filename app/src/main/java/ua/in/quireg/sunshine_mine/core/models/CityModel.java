package ua.in.quireg.sunshine_mine.core.models;


import android.util.Pair;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class CityModel {
    @JsonProperty("id")
    public int ID;

    @JsonProperty("name")
    public String name;

    @JsonProperty("coord")
    public CoordinatesModel coordinates;

    @JsonProperty("country")
    public String country;

    @JsonProperty("population")
    public int population;

}
