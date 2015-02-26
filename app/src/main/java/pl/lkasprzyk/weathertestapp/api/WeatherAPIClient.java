package pl.lkasprzyk.weathertestapp.api;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherAPIClient {

    private static WeatherAPI weatherAPI;
    private static String ROOT_URL =
            "http://api.worldweatheronline.com/free/v2";

    static {
        setupWeatherAPIClient();
    }

    private WeatherAPIClient() {
    }

    public static WeatherAPI get() {
        return weatherAPI;
    }

    private static void setupWeatherAPIClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder().setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ROOT_URL);

        RestAdapter restAdapter = builder.build();
        weatherAPI = restAdapter.create(WeatherAPI.class);
    }
}
