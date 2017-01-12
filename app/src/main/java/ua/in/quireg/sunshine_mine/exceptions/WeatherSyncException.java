package ua.in.quireg.sunshine_mine.exceptions;


public class WeatherSyncException extends Exception{

    public WeatherSyncException() {
    }

    public WeatherSyncException(String message) {
        super(message);
    }

    public WeatherSyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherSyncException(Throwable cause) {
        super(cause);
    }
}
