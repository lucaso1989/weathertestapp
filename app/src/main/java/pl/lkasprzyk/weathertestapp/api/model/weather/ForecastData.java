package pl.lkasprzyk.weathertestapp.api.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lucas on 2015-02-26.
 */
public class ForecastData {

    @Expose
    @SerializedName("weather")
    private List<DayWeather> daysWeather;

    public List<DayWeather> getDaysWeather() {
        return daysWeather;
    }

    public void setDaysWeather(List<DayWeather> daysWeather) {
        this.daysWeather = daysWeather;
    }
}
