package ua.in.quireg.sunshine_mine.exceptions;



public class WeatherJsonDownloaderException extends Exception {
    public WeatherJsonDownloaderException() {
        super();
    }

    public WeatherJsonDownloaderException(String message) {
        super(message);
    }

    public WeatherJsonDownloaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherJsonDownloaderException(Throwable cause) {
        super(cause);
    }
}
