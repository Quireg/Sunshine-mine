package ua.in.quireg.sunshine_mine.data;


import ua.in.quireg.sunshine_mine.core.models.current_weather_models.CurrentWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherAPIJsonRespondModel;

public class WeatherDbImporter {
    private static WeatherDbImporter mInstance;

    private WeatherDbImporter(){
    }

    public static WeatherDbImporter getInstance(){
        if(mInstance == null){
            mInstance = new WeatherDbImporter();
        }
        return mInstance;
    }

    public boolean proceedHourlyWeatherAPIJsonRespondModel(HourlyWeatherAPIJsonRespondModel model){
        return true;
    }

    public boolean proceedDailyWeatherAPIJsonRespondModel(DailyWeatherAPIJsonRespondModel model){
        return true;
    }

    public boolean proceedCurrentWeatherAPIJsonRespondModel(CurrentWeatherAPIJsonRespondModel model){
        return true;
    }
}
