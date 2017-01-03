package ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class CloudsModel {

    @JsonProperty("all")
    public int cloudiness;
}
