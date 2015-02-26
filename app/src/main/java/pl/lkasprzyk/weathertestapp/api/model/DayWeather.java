package pl.lkasprzyk.weathertestapp.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lucas on 2015-02-26.
 */
public class DayWeather {
    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("hourly")
    private List<WeatherConditions> weatherConditions;

    @Expose
    @SerializedName("maxtempC")
    private int maxTempC;

    @Expose
    @SerializedName("mintempC")
    private int minTempC;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<WeatherConditions> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<WeatherConditions> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public int getMaxTempC() {
        return maxTempC;
    }

    public void setMaxTempC(int maxTempC) {
        this.maxTempC = maxTempC;
    }

    public int getMinTempC() {
        return minTempC;
    }

    public void setMinTempC(int minTempC) {
        this.minTempC = minTempC;
    }
}
