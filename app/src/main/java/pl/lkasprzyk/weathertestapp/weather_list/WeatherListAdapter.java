package pl.lkasprzyk.weathertestapp.weather_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.lkasprzyk.weathertestapp.R;
import pl.lkasprzyk.weathertestapp.api.model.DayWeather;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.ViewHolder> {

    private List<DayWeather> weatherForecastsList;

    public WeatherListAdapter(List<DayWeather> weatherForecastsList) {
        this.weatherForecastsList = weatherForecastsList;
    }

    @Override
    public WeatherListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_weather_list_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayWeather dayWeather = weatherForecastsList.get(position);
        if (dayWeather != null) {
            holder.weatherDescription.setText(dayWeather.getDate() + ", maxTemp =  " + dayWeather.getMaxTempC() + ", minTemp = " + dayWeather.getMinTempC());
        }
    }

    @Override
    public int getItemCount() {
        return weatherForecastsList.size();
    }

    public void setWeatherForecastsList(List<DayWeather> usernameList) {
        this.weatherForecastsList = usernameList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView weatherDescription;

        public ViewHolder(View v) {
            super(v);
            weatherDescription = (TextView) v.findViewById(R.id.weather_list_weather_description);
        }
    }
}
