package pl.lkasprzyk.weathertestapp.weather_list;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pl.lkasprzyk.weathertestapp.R;
import pl.lkasprzyk.weathertestapp.api.WeatherAPIClient;
import pl.lkasprzyk.weathertestapp.api.model.DayWeather;
import pl.lkasprzyk.weathertestapp.api.model.ResponseData;
import pl.lkasprzyk.weathertestapp.utils.NetworkUtils;
import pl.lkasprzyk.weathertestapp.utils.RecyclerViewItemClickListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherListFragment extends android.support.v4.app.Fragment {

    private static final String LOG_TAG = WeatherListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private WeatherListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<DayWeather> weatherForecasts = new ArrayList<>();
    private OnDayWeatherForecastSelectedListener dayWeatherForecastSelectedListener;

    public WeatherListFragment() {
    }

    public interface OnDayWeatherForecastSelectedListener {
        public void onDayWeatherForecastSelected(String query, String date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dayWeatherForecastSelectedListener = (OnDayWeatherForecastSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDayWeatherForecastSelectedListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        setUpViews();
    }


    private void getWeatherForecast() {
        WeatherAPIClient.get().get5DayForecast("London", new Callback<ResponseData>() {
            @Override
            public void success(ResponseData responseData, Response response) {
                if (responseData != null && responseData.getData() != null && responseData.getData().getDaysWeather() != null) {
                    weatherForecasts = responseData.getData().getDaysWeather();
                    adapter.setWeatherForecastsList(weatherForecasts);
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkUtils.isNetworkConnectionAvailable(getActivity().getApplicationContext())) {
            getWeatherForecast();
        } else {
            NetworkUtils.showNetworkDialog(getActivity().getApplicationContext());
        }
    }

    private void initViews() {
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        adapter = new WeatherListAdapter(getActivity().getApplicationContext(), weatherForecasts);
    }

    private void setUpViews() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity().getApplicationContext(), new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DayWeather dayWeather = adapter.getWeatherForecastsList().get(position);
                if (dayWeather != null) {
                    dayWeatherForecastSelectedListener.onDayWeatherForecastSelected("London", dayWeather.getDate());
                }
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

}
