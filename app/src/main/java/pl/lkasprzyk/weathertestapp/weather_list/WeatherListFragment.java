package pl.lkasprzyk.weathertestapp.weather_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pl.lkasprzyk.weathertestapp.MainActivity;
import pl.lkasprzyk.weathertestapp.R;
import pl.lkasprzyk.weathertestapp.api.WeatherAPIClient;
import pl.lkasprzyk.weathertestapp.api.model.search.SearchResponseData;
import pl.lkasprzyk.weathertestapp.api.model.search.SearchResult;
import pl.lkasprzyk.weathertestapp.api.model.weather.DayWeather;
import pl.lkasprzyk.weathertestapp.api.model.weather.WeatherResponseData;
import pl.lkasprzyk.weathertestapp.utils.Constants;
import pl.lkasprzyk.weathertestapp.utils.NetworkUtils;
import pl.lkasprzyk.weathertestapp.utils.customviews.CustomAutoCompleteTextView;
import pl.lkasprzyk.weathertestapp.utils.customviews.RecyclerViewItemClickListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherListFragment extends android.support.v4.app.Fragment implements WeatherLocationAutoCompleteAdapter.OnSearchLocationQueryListener {

    private static final String LOG_TAG = WeatherListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private View progressView;
    private ProgressBar progress;
    private TextView progressTextTv;
    private WeatherListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private List<DayWeather> weatherForecasts = new ArrayList<>();
    private OnDayWeatherForecastSelectedListener dayWeatherForecastSelectedListener;
    private OnActionBarRefreshListener actionBarRefreshListener;

    public WeatherListFragment() {
    }

    @Override
    public List<SearchResult> OnLocationSearch(String query) {
        SearchResponseData searchResponseData = WeatherAPIClient.get().searchLocation(query);
        if (searchResponseData != null && searchResponseData.getSearchData() != null && searchResponseData.getSearchData().getResult() != null) {
            return searchResponseData.getSearchData().getResult();
        } else {
            return null;
        }
    }

    public interface OnDayWeatherForecastSelectedListener {
        public void onDayWeatherForecastSelected(String query, String date);
    }

    public interface OnActionBarRefreshListener {
        public void OnActionBarRefresh();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dayWeatherForecastSelectedListener = (OnDayWeatherForecastSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDayWeatherForecastSelectedListener");
        }

        try {
            actionBarRefreshListener = (OnActionBarRefreshListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnActionBarRefreshListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initViews();
        setUpViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_weather_list_fragment, menu);
    }

    private void getWeatherForecast() {
        if (NetworkUtils.isNetworkConnectionAvailable(getActivity().getApplicationContext())) {
            WeatherAPIClient.get().get5DayForecast(getActivity().getApplicationContext().getSharedPreferences(Constants.PREFS_CURRENT_WEATHER_LOCATION, Context.MODE_PRIVATE).getString(Constants.PREFS_CURRENT_WEATHER_LOCATION_NAME, Constants.DEFAULT_LOCATION), new Callback<WeatherResponseData>() {
                @Override
                public void success(WeatherResponseData weatherResponseData, Response response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (weatherResponseData != null && weatherResponseData.getData() != null && weatherResponseData.getData().getDaysWeather() != null) {
                        progressView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        weatherForecasts = weatherResponseData.getData().getDaysWeather();
                        adapter.setWeatherForecastsList(weatherForecasts);
                        updateLastUpdateTime();
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

    private void updateLastUpdateTime() {
        long timestamp = System.currentTimeMillis();
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFS_CURRENT_WEATHER_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences != null ? sharedPreferences.edit() : null;
        if (editor != null) {
            editor.putLong(Constants.PREFS_CURRENT_LAST_WEATHER_UPDATE, timestamp);
            editor.apply();
        }
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        adapter.setLastUpdate(DateFormat.format("dd-MM-yyyy HH:mm", cal).toString());
    }

    private void showCannotFetchWeatherView() {
        progress.setVisibility(View.GONE);
        progressTextTv.setText(getString(R.string.weather_cannot_featch_from_server_label));
    }

    @Override
    public void onStart() {
        super.onStart();
        getWeatherForecast();
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
        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgainToFetchWeather();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tryAgainToFetchWeather();
            }
        });
    }

    private void tryAgainToFetchWeather() {
        progress.setVisibility(View.VISIBLE);
        progressTextTv.setText(getString(R.string.fragment_progress_loading_weather_label));
        getWeatherForecast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        progressView = rootView.findViewById(R.id.progress_view);
        progress = (ProgressBar) progressView.findViewById(R.id.progress);
        progressTextTv = (TextView) progressView.findViewById(R.id.progress_text);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_to_refresh_layout);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_location) {
            showChangeLocationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChangeLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dialog_choose_location, null);
        final CustomAutoCompleteTextView autoCompleteTextView = (CustomAutoCompleteTextView) view.findViewById(R.id.auto_complete_view);
        ProgressBar progress = (ProgressBar) view.findViewById(R.id.auto_complete_loading_indicator);
        autoCompleteTextView.setAdapter(new WeatherLocationAutoCompleteAdapter(getActivity().getApplicationContext(), WeatherListFragment.this));
        autoCompleteTextView.setLoadingIndicator(progress);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult searchResult = (SearchResult) parent.getItemAtPosition(position);
                autoCompleteTextView.setText(searchResult.getArea().get(0).getAreaName());
            }
        });
        builder.setView(view);
        builder.setTitle(getString(R.string.weather_location_dialog_title));
        builder.setPositiveButton(getString(R.string.common_ok_dialog_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                autoCompleteTextView.setError(null);
                if (autoCompleteTextView.getText().toString().length() > 2) {
                    storeLocation(autoCompleteTextView.getText().toString());
                    dialog.cancel();
                    tryAgainToFetchWeather();
                } else {
                    autoCompleteTextView.setError(getString(R.string.weather_location_dialog_location_name_to_short))
                    ;
                }
            }
        });
        builder.setNegativeButton(getString(R.string.common_cancel_dialog_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void storeLocation(String locationName) {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.PREFS_CURRENT_WEATHER_LOCATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences != null ? sharedPreferences.edit() : null;
        if (editor != null) {
            editor.putString(Constants.PREFS_CURRENT_WEATHER_LOCATION_NAME, locationName);
            editor.apply();
        }
        actionBarRefreshListener.OnActionBarRefresh();
    }

}
