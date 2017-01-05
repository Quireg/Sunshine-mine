package ua.in.quireg.sunshine_mine.core.models.current_weather_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class RainModel {

    @JsonProperty("3h")
    public int rainVolume;
}
