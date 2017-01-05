package ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

import ua.in.quireg.sunshine_mine.interfaces.IWeather;

public class HourlyWeatherAPIJsonRespondModel implements IWeather{
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
