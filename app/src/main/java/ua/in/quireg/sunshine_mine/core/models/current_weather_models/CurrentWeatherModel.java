package ua.in.quireg.sunshine_mine.core.models.current_weather_models;


import org.codehaus.jackson.annotate.JsonProperty;

import ua.in.quireg.sunshine_mine.interfaces.IWeatherModel;


public class CurrentWeatherModel implements IWeatherModel {
    @JsonProperty("coord")
    public CoordinatesModel coordinates;

    @JsonProperty("weather")
    public WeatherTypeModel[] weatherTypeModel;

    @JsonProperty("base")
    public String base;

    @JsonProperty("main")
    public MainModel mainModel;

    @JsonProperty("wind")
    public WindModel windModel;

    @JsonProperty("clouds")
    public CloudsModel cloudsModel;

    @JsonProperty("rain")
    public RainModel rainModel;

    @JsonProperty("snow")
    public SnowModel snowModel;

    @JsonProperty("dt")
    public long timestamp;

    @JsonProperty("sys")
    public SysModel sysModel;

    @JsonProperty("id")
    public int id;

    @JsonProperty("cod")
    public int respondCode;

    @JsonProperty("message")
    public double message;




}
