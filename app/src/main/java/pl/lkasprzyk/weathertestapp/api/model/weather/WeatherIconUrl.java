package pl.lkasprzyk.weathertestapp.api.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherIconUrl {

    @Expose
    @SerializedName("value")
    private String weatherIconUrl;

    public String getWeatherIconUrl() {
        return weatherIconUrl;
    }

    public void setWeatherIconUrl(String weatherIconUrl) {
        this.weatherIconUrl = weatherIconUrl;
    }
}
