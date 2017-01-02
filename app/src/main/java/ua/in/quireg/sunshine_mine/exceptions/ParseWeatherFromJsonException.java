package ua.in.quireg.sunshine_mine.exceptions;


public class ParseWeatherFromJsonException extends Exception{
    public ParseWeatherFromJsonException() {
        super();
    }

    public ParseWeatherFromJsonException(String message) {
        super(message);
    }

    public ParseWeatherFromJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseWeatherFromJsonException(Throwable cause) {
        super(cause);
    }

}
