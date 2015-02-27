package pl.lkasprzyk.weathertestapp.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherConditions {

    @Expose
    @SerializedName("humidity")
    private int humidity;

    @Expose
    @SerializedName("WindChillC")
    private int windChillC;

    @Expose
    @SerializedName("windspeedKmph")
    private int windSpeedKhm;

    @Expose
    @SerializedName("chanceofrain")
    private int chanceOfRain;

    @Expose
    @SerializedName("pressure")
    private int pressure;

    @Expose
    @SerializedName("FeelsLikeC")
    private int feelsLikeC;

    @Expose
    @SerializedName("winddir16Point")
    private String windDirection;

    @Expose
    @SerializedName("weatherDesc")
    private List<WeatherDescription> weatherDescription;

    @Expose
    @SerializedName("weatherIconUrl")
    private List<WeatherIconUrl> weatherIconUrl;


    public int getWindChillC() {
        return windChillC;
    }

    public void setWindChillC(int windChillC) {
        this.windChillC = windChillC;
    }

    public int getWindSpeedKhm() {
        return windSpeedKhm;
    }

    public void setWindSpeedKhm(int windSpeedKhm) {
        this.windSpeedKhm = windSpeedKhm;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getFeelsLikeC() {
        return feelsLikeC;
    }

    public void setFeelsLikeC(int feelsLikeC) {
        this.feelsLikeC = feelsLikeC;
    }

    public int getChanceOfRain() {
        return chanceOfRain;
    }

    public void setChanceOfRain(int chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getHumidity() {

        return humidity;
    }

    public List<WeatherIconUrl> getWeatherIconUrl() {
        return weatherIconUrl;
    }

    public void setWeatherIconUrl(List<WeatherIconUrl> weatherIconUrl) {
        this.weatherIconUrl = weatherIconUrl;
    }

    public List<WeatherDescription> getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(List<WeatherDescription> weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
