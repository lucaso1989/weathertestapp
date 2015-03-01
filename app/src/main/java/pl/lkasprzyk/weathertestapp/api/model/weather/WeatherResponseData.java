package pl.lkasprzyk.weathertestapp.api.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherResponseData {

    @Expose
    @SerializedName("data")
    ForecastData data;

    public ForecastData getData() {
        return data;
    }

    public void setData(ForecastData data) {
        this.data = data;
    }
}
