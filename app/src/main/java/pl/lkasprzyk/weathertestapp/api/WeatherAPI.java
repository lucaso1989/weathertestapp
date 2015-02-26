package pl.lkasprzyk.weathertestapp.api;

import pl.lkasprzyk.weathertestapp.api.model.ResponseData;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Lucas on 2015-02-26.
 */
public interface WeatherAPI {

    static final String API_KEY = "a2e9682df47c3d468e0baafe5dc4b";
    static final String JSON_FORMAT = "json";

    @GET("/weather.ashx?key=" + API_KEY + "&format=" + JSON_FORMAT + "&num_of_days=5" + "&fx=yes&cc=no&tp=24")
    void get5DayForecast(@Query("q") String query, Callback<ResponseData> callback);

    @GET("/weather.ashx?key=" + API_KEY + "&format=" + JSON_FORMAT + "&num_of_days=1&fx=yes&cc=no&tp=24")
    void getWeatherDetails(@Query("q") String query, @Query("date") String date, Callback<ResponseData> callback);

}
