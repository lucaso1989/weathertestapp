package pl.lkasprzyk.weathertestapp.weather_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.lkasprzyk.weathertestapp.R;
import pl.lkasprzyk.weathertestapp.api.WeatherAPIClient;
import pl.lkasprzyk.weathertestapp.api.model.ResponseData;
import pl.lkasprzyk.weathertestapp.utils.NetworkUtils;
import pl.lkasprzyk.weathertestapp.weather_list.WeatherListFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lucas on 2015-02-27.
 */
public class WeatherDetailsFragment extends Fragment {

    public static final String EXTRA_WEATHER_FORECAST_QUERY = "weather_forecast_query";
    public static final String EXTRA_WEATHER_FORECAST_DATE = "weather_forecast_date";
    private static final String LOG_TAG = WeatherListFragment.class.getSimpleName();

    private String weatherForecastQuery;
    private String weatherForecastDate;


    public WeatherDetailsFragment() {
    }

    public static WeatherDetailsFragment newInstance(String query, String date) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_WEATHER_FORECAST_QUERY, query);
        args.putString(EXTRA_WEATHER_FORECAST_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle args = getArguments();
        if(args != null) {
            weatherForecastDate = args.getString(EXTRA_WEATHER_FORECAST_DATE);
            weatherForecastQuery = args.getString(EXTRA_WEATHER_FORECAST_QUERY);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        setUpViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkUtils.isNetworkConnectionAvailable(getActivity().getApplicationContext())) {
            getWeatherForecastForDate();
        } else {
            NetworkUtils.showNetworkDialog(getActivity().getApplicationContext());
        }
    }

    private void initViews() {
    }

    private void setUpViews() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_details, container, false);
        return rootView;
    }

    private void getWeatherForecastForDate() {
        WeatherAPIClient.get().getWeatherDetails(weatherForecastQuery, weatherForecastDate, new Callback<ResponseData>() {
            @Override
            public void success(ResponseData responseData, Response response) {
                if (responseData != null && responseData.getData() != null && responseData.getData().getDaysWeather() != null) {
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, error.toString());
            }
        });
    }
}
