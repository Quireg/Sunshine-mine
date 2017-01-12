package ua.in.quireg.sunshine_mine.data;


import android.content.ContentValues;
import android.content.Context;

import ua.in.quireg.sunshine_mine.core.models.current_weather_models.CurrentWeatherModelAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherModelAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherModelAPIJsonRespondModel;

import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;


public class WeatherDbImporter {
    private Context mContext;

    public WeatherDbImporter(Context context) {
        mContext = context;
    }


    public boolean proceedHourlyWeatherAPIJsonRespondModel(HourlyWeatherModelAPIJsonRespondModel model) {
        ContentValues[] hourValuesArray = new ContentValues[model.weatherByHourModels.length];

        for (int i = 0; i < model.weatherByHourModels.length; i++) {
            //I bet there is much easier way to do this.
            ContentValues hourValues = new ContentValues();
            hourValues.put(WeatherByHourEntry.COLUMN_LOC_KEY, model.cityModel.id);
            hourValues.put(WeatherByHourEntry.COLUMN_DATE, model.weatherByHourModels[i].timestamp);
            hourValues.put(WeatherByHourEntry.COLUMN_AVG_TEMP, model.weatherByHourModels[i].mainModel.temp);
            hourValues.put(WeatherByHourEntry.COLUMN_MAX_TEMP, model.weatherByHourModels[i].mainModel.temp_max);
            hourValues.put(WeatherByHourEntry.COLUMN_MIN_TEMP, model.weatherByHourModels[i].mainModel.temp_min);
            hourValues.put(WeatherByHourEntry.COLUMN_HUMIDITY, model.weatherByHourModels[i].mainModel.humidity);
            hourValues.put(WeatherByHourEntry.COLUMN_PRESSURE, model.weatherByHourModels[i].mainModel.pressure);
            hourValues.put(WeatherByHourEntry.COLUMN_PRESSURE_GRND_LEVEL, model.weatherByHourModels[i].mainModel.grnd_level);
            hourValues.put(WeatherByHourEntry.COLUMN_PRESSURE_SEA_LEVEL, model.weatherByHourModels[i].mainModel.sea_level);
            hourValues.put(WeatherByHourEntry.COLUMN_RAIN, model.weatherByHourModels[i].rainModel.rainVolume);
            hourValues.put(WeatherByHourEntry.COLUMN_SNOW, model.weatherByHourModels[i].snowModel.snowVolume);
            hourValues.put(WeatherByHourEntry.COLUMN_CLOUDS, model.weatherByHourModels[i].cloudsModel.cloudiness);
            hourValues.put(WeatherByHourEntry.COLUMN_WIND_SPEED, model.weatherByHourModels[i].windModel.speed);
            hourValues.put(WeatherByHourEntry.COLUMN_WIND_DEG, model.weatherByHourModels[i].windModel.degrees);
            hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_DESC, model.weatherByHourModels[i].weatherTypeModel[0].description);
            hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_ICON, model.weatherByHourModels[i].weatherTypeModel[0].icon);
            hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_ID, model.weatherByHourModels[i].weatherTypeModel[0].id);
            hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_MAIN, model.weatherByHourModels[i].weatherTypeModel[0].main);
            hourValues.put(WeatherByHourEntry.COLUMN_CALCULATION_TIME, model.weatherByHourModels[i].dt_txt);

            hourValuesArray[i] = hourValues;
        }
        mContext.getContentResolver().bulkInsert(WeatherByHourEntry.CONTENT_URI, hourValuesArray);
        return true;
    }

    public boolean proceedDailyWeatherAPIJsonRespondModel(DailyWeatherModelAPIJsonRespondModel model) {
        ContentValues[] dailyValuesArray = new ContentValues[model.weatherByDayModels.length];

        for (int i = 0; i < model.weatherByDayModels.length; i++) {

            ContentValues dailyValues = new ContentValues();
            dailyValues.put(WeatherByDayEntry.COLUMN_LOC_KEY, model.cityModel.id);
            dailyValues.put(WeatherByDayEntry.COLUMN_DATE, model.weatherByDayModels[i].timestamp);
            dailyValues.put(WeatherByDayEntry.COLUMN_MAX_TEMP, model.weatherByDayModels[i].temperatureModel.max);
            dailyValues.put(WeatherByDayEntry.COLUMN_MIN_TEMP, model.weatherByDayModels[i].temperatureModel.min);
            dailyValues.put(WeatherByDayEntry.COLUMN_EVE_TEMP, model.weatherByDayModels[i].temperatureModel.eve);
            dailyValues.put(WeatherByDayEntry.COLUMN_MORN_TEMP, model.weatherByDayModels[i].temperatureModel.morn);
            dailyValues.put(WeatherByDayEntry.COLUMN_NIGHT_TEMP, model.weatherByDayModels[i].temperatureModel.night);
            dailyValues.put(WeatherByDayEntry.COLUMN_CLOUDS, model.weatherByDayModels[i].clouds);
            dailyValues.put(WeatherByDayEntry.COLUMN_HUMIDITY, model.weatherByDayModels[i].humidity);
            dailyValues.put(WeatherByDayEntry.COLUMN_PRESSURE, model.weatherByDayModels[i].pressure);
            dailyValues.put(WeatherByDayEntry.COLUMN_WIND_SPEED, model.weatherByDayModels[i].speed);
            dailyValues.put(WeatherByDayEntry.COLUMN_WIND_DEG, model.weatherByDayModels[i].deg);
            dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_DESC, model.weatherByDayModels[i].weatherTypeModel[0].description);
            dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_ICON, model.weatherByDayModels[i].weatherTypeModel[0].icon);
            dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_ID, model.weatherByDayModels[i].weatherTypeModel[0].id);
            dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_MAIN, model.weatherByDayModels[i].weatherTypeModel[0].main);

            dailyValuesArray[i] = dailyValues;
        }
        mContext.getContentResolver().bulkInsert(WeatherByDayEntry.CONTENT_URI, dailyValuesArray);
        return true;
    }

    public boolean proceedCurrentWeatherAPIJsonRespondModel(CurrentWeatherModelAPIJsonRespondModel model) {

        ContentValues currentValues = new ContentValues();
        currentValues.put(CurrentWeatherEntry.COLUMN_LOC_KEY, model.id);
        currentValues.put(CurrentWeatherEntry.COLUMN_DATE, model.timestamp);
        currentValues.put(CurrentWeatherEntry.COLUMN_AVG_TEMP, model.mainModel.temp);
        currentValues.put(CurrentWeatherEntry.COLUMN_MAX_TEMP, model.mainModel.temp_max);
        currentValues.put(CurrentWeatherEntry.COLUMN_MIN_TEMP, model.mainModel.temp_min);
        currentValues.put(CurrentWeatherEntry.COLUMN_HUMIDITY, model.mainModel.humidity);
        currentValues.put(CurrentWeatherEntry.COLUMN_PRESSURE, model.mainModel.pressure);
        currentValues.put(CurrentWeatherEntry.COLUMN_PRESSURE_GRND_LEVEL, model.mainModel.grnd_level);
        currentValues.put(CurrentWeatherEntry.COLUMN_PRESSURE_SEA_LEVEL, model.mainModel.sea_level);
        currentValues.put(CurrentWeatherEntry.COLUMN_CLOUDS, model.cloudsModel.cloudiness);
        currentValues.put(CurrentWeatherEntry.COLUMN_RAIN, model.rainModel.rainVolume);
        currentValues.put(CurrentWeatherEntry.COLUMN_SNOW, model.snowModel.snowVolume);
        currentValues.put(CurrentWeatherEntry.COLUMN_WIND_SPEED, model.windModel.speed);
        currentValues.put(CurrentWeatherEntry.COLUMN_WIND_DEG, model.windModel.degrees);
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_DESC, model.weatherTypeModel[0].description);
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_ICON, model.weatherTypeModel[0].icon);
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_ID, model.weatherTypeModel[0].id);
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_MAIN, model.weatherTypeModel[0].main);
        currentValues.put(CurrentWeatherEntry.COLUMN_SUNRISE, model.sysModel.sunrise);
        currentValues.put(CurrentWeatherEntry.COLUMN_SUNSET, model.sysModel.sunset);

        mContext.getContentResolver().insert(CurrentWeatherEntry.CONTENT_URI, currentValues);
        return true;
    }
}
