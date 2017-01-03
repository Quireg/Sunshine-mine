package ua.in.quireg.sunshine_mine.core.models.daily_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class CoordinatesModel {
    @JsonProperty("lon")
    public double longitude;

    @JsonProperty("lat")
    public double latitude;
}
