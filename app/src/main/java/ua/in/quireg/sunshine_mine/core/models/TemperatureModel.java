package ua.in.quireg.sunshine_mine.core.models;


import org.codehaus.jackson.annotate.JsonProperty;

public class TemperatureModel {
    @JsonProperty("day")
    public double day;

    @JsonProperty("min")
    public double min;

    @JsonProperty("max")
    public double max;

    @JsonProperty("night")
    public double night;

    @JsonProperty("eve")
    public double eve;

    @JsonProperty("morn")
    public double morn;

}
