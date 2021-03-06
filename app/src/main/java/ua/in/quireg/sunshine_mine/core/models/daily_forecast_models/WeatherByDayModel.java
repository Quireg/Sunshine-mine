package ua.in.quireg.sunshine_mine.core.models.daily_forecast_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class WeatherByDayModel {
    @JsonProperty("dt")
    public long timestamp;

    @JsonProperty("temp")
    public TemperatureModel temperatureModel;

    @JsonProperty("pressure")
    public double pressure;

    @JsonProperty("humidity")
    public double humidity;

    @JsonProperty("weather")
    public WeatherTypeModel[] weatherTypeModel;

    @JsonProperty("speed")
    public double speed;

    @JsonProperty("deg")
    public double deg;

    @JsonProperty("clouds")
    public double clouds;

//    @JsonProperty("snow")
//    public double snow;

}
