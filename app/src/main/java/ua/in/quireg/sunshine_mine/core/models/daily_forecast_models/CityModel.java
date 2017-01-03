package ua.in.quireg.sunshine_mine.core.models.daily_forecast_models;


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

//    @JsonProperty("population")
//    public int population;

}
