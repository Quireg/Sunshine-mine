package ua.in.quireg.sunshine_mine.core.models.current_weather_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class CloudsModel {

    @JsonProperty("all")
    public int cloudiness;
}
