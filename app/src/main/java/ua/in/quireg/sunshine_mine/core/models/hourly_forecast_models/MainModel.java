package ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models;

import org.codehaus.jackson.annotate.JsonProperty;

public class MainModel {

    @JsonProperty("temp")
    public long temp;

    @JsonProperty("temp_min")
    public long temp_min;

    @JsonProperty("temp_max")
    public long temp_max;

    @JsonProperty("pressure")
    public long pressure;

    @JsonProperty("sea_level")
    public long sea_level;

    @JsonProperty("grnd_level")
    public long grnd_level;

    @JsonProperty("humidity")
    public long humidity;

    @JsonProperty("temp_kf")
    public long temp_kf;

}
