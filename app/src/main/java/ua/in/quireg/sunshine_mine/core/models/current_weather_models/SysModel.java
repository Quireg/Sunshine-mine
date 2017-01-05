package ua.in.quireg.sunshine_mine.core.models.current_weather_models;


import org.codehaus.jackson.annotate.JsonProperty;

public class SysModel {

    @JsonProperty("type")
    public int type;

    @JsonProperty("id")
    public int id;

    @JsonProperty("message")
    public String message;

    @JsonProperty("country")
    public String country;

    @JsonProperty("sunrise")
    public int sunrise;

    @JsonProperty("sunset")
    public int sunset;

}
