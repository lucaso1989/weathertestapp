package pl.lkasprzyk.weathertestapp.weather_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.lkasprzyk.weathertestapp.MainActivity;
import pl.lkasprzyk.weathertestapp.R;
import pl.lkasprzyk.weathertestapp.api.WeatherAPIClient;
import pl.lkasprzyk.weathertestapp.api.model.weather.DayWeather;
import pl.lkasprzyk.weathertestapp.api.model.weather.WeatherResponseData;
import pl.lkasprzyk.weathertestapp.utils.NetworkUtils;
import pl.lkasprzyk.weathertestapp.weather_list.WeatherListFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lucas on 2015-02-27.
 */
public class WeatherDetailsFragment extends Fragment {

    private static final String LOG_TAG = WeatherListFragment.class.getSimpleName();
    public static final String EXTRA_WEATHER_FORECAST_QUERY = "weather_forecast_query";
    public static final String EXTRA_WEATHER_FORECAST_DATE = "weather_forecast_date";

    private String weatherForecastQuery;
    private String weatherForecastDate;

    private View contentContainer;
    private View progressView;
    private ProgressBar progress;
    private TextView progressTextTv;

    private TextView weatherDateTv;
    private TextView weatherDayTv;
    private TextView weatherTodayTv;
    private TextView weatherDescriptionTv;
    private TextView weatherMaxTempTv;
    private TextView weatherMinTempTv;
    private TextView weatherRealFeelTempTv;
    private TextView weatherWindSpeedTv;
    private TextView weatherWindDirectionTv;
    private TextView weatherHumidityTv;
    private TextView weatherPressureTv;
    private TextView weatherRainChanceTv;

    private ImageView weatherIcon;


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
        if (args != null) {
            weatherForecastDate = args.getString(EXTRA_WEATHER_FORECAST_DATE);
            weatherForecastQuery = args.getString(EXTRA_WEATHER_FORECAST_QUERY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgainToFetchWeather();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getWeatherForecastForDate();
    }


    private void setUpViews(DayWeather dayWeather) {
        setDate(dayWeather.getDate());
        weatherMaxTempTv.setText(getString(R.string.weather_details_temp_value, dayWeather.getMaxTempC()));
        weatherMinTempTv.setText(getString(R.string.weather_details_temp_value, dayWeather.getMinTempC()));
        if (dayWeather.getWeatherConditions().size() > 0) {
            weatherRealFeelTempTv.setText(getString(R.string.weather_details_temp_value, dayWeather.getWeatherConditions().get(0).getFeelsLikeC()));
            weatherRainChanceTv.setText(getString(R.string.weather_details_percent_value, dayWeather.getWeatherConditions().get(0).getChanceOfRain()));
            weatherHumidityTv.setText(getString(R.string.weather_details_percent_value, dayWeather.getWeatherConditions().get(0).getHumidity()));
            weatherPressureTv.setText(getString(R.string.weather_details_pressure_value, dayWeather.getWeatherConditions().get(0).getPressure()));
            weatherWindSpeedTv.setText(getString(R.string.weather_details_speed_value, dayWeather.getWeatherConditions().get(0).getWindSpeedKhm()));
            weatherWindDirectionTv.setText(getString(R.string.weather_details_text_value, dayWeather.getWeatherConditions().get(0).getWindDirection()));
            if (dayWeather.getWeatherConditions().get(0).getWeatherDescription().size() > 0)
                weatherDescriptionTv.setText(dayWeather.getWeatherConditions().get(0).getWeatherDescription().get(0).getWeatherDescription());
            if (dayWeather.getWeatherConditions().get(0).getWeatherIconUrl().size() > 0)
                Picasso.with(getActivity().getApplicationContext()).load(dayWeather.getWeatherConditions().get(0).getWeatherIconUrl().get(0).getWeatherIconUrl()).resizeDimen(R.dimen.weather_current_day_icon_size, R.dimen.weather_current_day_icon_size).into(weatherIcon);
        }
    }

    private void setDate(String date) {
        weatherDateTv.setText(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date d = dateFormat.parse(date);
            Date currentDate = new Date();
            weatherDayTv.setText(DateFormat.format("EEEE", d).toString());
            if (dateFormat.format(currentDate).compareTo(dateFormat.format(d)) == 0)
                weatherTodayTv.setVisibility(View.VISIBLE);
            else
                weatherTodayTv.setVisibility(View.GONE);
        } catch (ParseException e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_details, container, false);
        weatherDateTv = (TextView) rootView.findViewById(R.id.weather_details_date);
        weatherDayTv = (TextView) rootView.findViewById(R.id.weather_details_day_of_week);
        weatherTodayTv = (TextView) rootView.findViewById(R.id.weather_details_today);
        weatherMaxTempTv = (TextView) rootView.findViewById(R.id.weather_details_max_temp_value);
        weatherMinTempTv = (TextView) rootView.findViewById(R.id.weather_details_min_temp_value);
        weatherRealFeelTempTv = (TextView) rootView.findViewById(R.id.weather_details_real_feel_temp_value);
        weatherWindSpeedTv = (TextView) rootView.findViewById(R.id.weather_details_wind_speed_value);
        weatherWindDirectionTv = (TextView) rootView.findViewById(R.id.weather_details_wind_direction_value);
        weatherHumidityTv = (TextView) rootView.findViewById(R.id.weather_details_humidity_value);
        weatherPressureTv = (TextView) rootView.findViewById(R.id.weather_details_pressure_value);
        weatherDescriptionTv = (TextView) rootView.findViewById(R.id.weather_details_description);
        weatherRainChanceTv = (TextView) rootView.findViewById(R.id.weather_details_rain_chance_value);
        weatherIcon = (ImageView) rootView.findViewById(R.id.weather_details_icon);

        contentContainer = rootView.findViewById(R.id.content_container);
        progressView = rootView.findViewById(R.id.progress_view);
        progress = (ProgressBar) progressView.findViewById(R.id.progress);
        progressTextTv = (TextView) progressView.findViewById(R.id.progress_text);

        return rootView;
    }

    private void getWeatherForecastForDate() {
        if (NetworkUtils.isNetworkConnectionAvailable(getActivity().getApplicationContext())) {
            WeatherAPIClient.get().getWeatherDetails(weatherForecastQuery, weatherForecastDate, new Callback<WeatherResponseData>() {
                @Override
                public void success(WeatherResponseData weatherResponseData, Response response) {
                    if (weatherResponseData != null && weatherResponseData.getData() != null && weatherResponseData.getData().getDaysWeather() != null) {
                        progressView.setVisibility(View.GONE);
                        contentContainer.setVisibility(View.VISIBLE);
                        setUpViews(weatherResponseData.getData().getDaysWeather().get(0));
                    } else {
                        showCannotFetchWeatherView();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    showCannotFetchWeatherView();
                }
            });
        } else {
            showCannotFetchWeatherView();
            NetworkUtils.showNetworkDialog(getActivity().getApplicationContext());
        }
    }

    private void showCannotFetchWeatherView() {
        progress.setVisibility(View.GONE);
        progressTextTv.setText(getString(R.string.weather_cannot_featch_from_server_label));
    }

    private void tryAgainToFetchWeather() {
        progress.setVisibility(View.VISIBLE);
        progressTextTv.setText(getString(R.string.fragment_progress_loading_weather_label));
        getWeatherForecastForDate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
