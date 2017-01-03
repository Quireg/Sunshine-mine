package ua.in.quireg.sunshine_mine.core.models.daily_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class DailyWeatherAPIJsonRespondModel {
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
