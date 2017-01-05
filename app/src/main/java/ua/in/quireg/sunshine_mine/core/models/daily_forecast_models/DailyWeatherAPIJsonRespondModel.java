package ua.in.quireg.sunshine_mine.core.models.daily_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

import ua.in.quireg.sunshine_mine.interfaces.IWeather;

public class DailyWeatherAPIJsonRespondModel implements IWeather{
    @JsonProperty("city")
    public CityModel cityModel;

    @JsonProperty("cod")
    public int respondCode;

    @JsonProperty("message")
    public double message;

    @JsonProperty("cnt")
    public int cnt;

    @JsonProperty("list")
    public WeatherByDayModel[] weatherByDayModels;
}
