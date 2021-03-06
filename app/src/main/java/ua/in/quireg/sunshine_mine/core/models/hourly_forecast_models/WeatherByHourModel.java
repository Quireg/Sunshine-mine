package ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class WeatherByHourModel {
    @JsonProperty("dt")
    public long timestamp;

    @JsonProperty("main")
    public MainModel mainModel;

    @JsonProperty("weather")
    public WeatherTypeModel[] weatherTypeModel;

    @JsonProperty("wind")
    public WindModel windModel;

    @JsonProperty("clouds")
    public CloudsModel cloudsModel;

    @JsonProperty("rain")
    public RainModel rainModel;

    @JsonProperty("snow")
    public SnowModel snowModel;

    @JsonProperty("dt_txt")
    public String dt_txt;


}
