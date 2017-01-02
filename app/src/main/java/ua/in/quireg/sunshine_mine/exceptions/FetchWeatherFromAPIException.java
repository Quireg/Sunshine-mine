package ua.in.quireg.sunshine_mine.exceptions;



public class FetchWeatherFromAPIException extends Exception {
    public FetchWeatherFromAPIException() {
        super();
    }

    public FetchWeatherFromAPIException(String message) {
        super(message);
    }

    public FetchWeatherFromAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public FetchWeatherFromAPIException(Throwable cause) {
        super(cause);
    }
}
