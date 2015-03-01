package pl.lkasprzyk.weathertestapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import pl.lkasprzyk.weathertestapp.utils.Constants;
import pl.lkasprzyk.weathertestapp.weather_details.WeatherDetailsFragment;
import pl.lkasprzyk.weathertestapp.weather_list.WeatherListFragment;


public class MainActivity extends ActionBarActivity implements WeatherListFragment.OnDayWeatherForecastSelectedListener, WeatherListFragment.OnActionBarRefreshListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpWeatherLocation();
        setUpActionBar();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherListFragment())
                    .commit();
        }
    }

    @Override
    public void OnActionBarRefresh() {
        setUpActionBar();
    }

    private void setUpWeatherLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_CURRENT_WEATHER_LOCATION, Context.MODE_PRIVATE);
        if (!sharedPreferences.contains(Constants.PREFS_CURRENT_WEATHER_LOCATION_NAME)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.PREFS_CURRENT_WEATHER_LOCATION_NAME, Constants.DEFAULT_LOCATION);
            editor.apply();
        }
    }

    private void setUpActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getSharedPreferences(Constants.PREFS_CURRENT_WEATHER_LOCATION, Context.MODE_PRIVATE).getString(Constants.PREFS_CURRENT_WEATHER_LOCATION_NAME, getString(R.string.app_name)));
        }
    }


    @Override
    public void onDayWeatherForecastSelected(String query, String date) {
        WeatherDetailsFragment fragment = WeatherDetailsFragment.newInstance(query, date);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).addToBackStack(null)
                .commit();
    }
}
