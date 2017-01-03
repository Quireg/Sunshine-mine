package ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class HourlyWeatherAPIJsonRespondModel {
    @JsonProperty("city")
    public CityModel cityModel;

    @JsonProperty("code")
    public int respondCode;

    @JsonProperty("message")
    public double message;

    @JsonProperty("cnt")
    public int cnt;

    @JsonProperty("list")
    public WeatherByHourModel[] weatherByHourModels;
}
