package pl.lkasprzyk.weathertestapp.api.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherDescription {

    @Expose
    @SerializedName("value")
    private String weatherDescription;

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
}
