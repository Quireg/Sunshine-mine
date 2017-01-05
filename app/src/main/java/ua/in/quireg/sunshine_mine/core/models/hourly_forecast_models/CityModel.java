package ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class CityModel {
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("coord")
    public CoordinatesModel coordinates;

    @JsonProperty("country")
    public String country;

//    @JsonProperty("population")
//    public int population;

}
