package pl.lkasprzyk.weathertestapp.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherAstronomy {

    @Expose
    @SerializedName("sunrise")
    String sunrise;

    @SerializedName("sunset")
    String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
