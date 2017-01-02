package ua.in.quireg.sunshine_mine.core.models;


import org.codehaus.jackson.annotate.JsonProperty;

public class WeatherTypeModel {

    @JsonProperty("id")
    public int id;

    @JsonProperty("main")
    public String main;

    @JsonProperty("description")
    public String description;

    @JsonProperty("icon")
    public String icon;

}
