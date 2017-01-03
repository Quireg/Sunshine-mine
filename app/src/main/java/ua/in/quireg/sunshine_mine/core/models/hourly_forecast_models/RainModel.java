package ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class RainModel {

    @JsonProperty("3h")
    public int rainVolume;
}
