package ua.in.quireg.sunshine_mine.core.models.current_weather_models;

import org.codehaus.jackson.annotate.JsonProperty;

public class SnowModel {

    @JsonProperty("3h")
    public int snowVolume;
}
