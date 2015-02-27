package pl.lkasprzyk.weathertestapp.weather_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pl.lkasprzyk.weathertestapp.R;
import pl.lkasprzyk.weathertestapp.api.model.weather.DayWeather;

/**
 * Created by Lucas on 2015-02-26.
 */
public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.ViewHolder> {

    private final int CURRENT_DAY_VIEW_TYPE = 1;
    private final int NEXT_DAY_VIEW_TYPE = 2;
    private List<DayWeather> weatherForecastsList;
    private Context context;

    public WeatherListAdapter(Context context, List<DayWeather> weatherForecastsList) {
        this.context = context;
        this.weatherForecastsList = weatherForecastsList;
    }


    public List<DayWeather> getWeatherForecastsList() {
        return weatherForecastsList;
    }

    @Override
    public WeatherListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        int layoutId = -1;
        if (viewType == CURRENT_DAY_VIEW_TYPE)
            layoutId = R.layout.adapter_weather_current_day_list_row_item;
        else if (viewType == NEXT_DAY_VIEW_TYPE)
            layoutId = R.layout.adapter_weather_next_day_list_row_item;

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? CURRENT_DAY_VIEW_TYPE : NEXT_DAY_VIEW_TYPE;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayWeather dayWeather = weatherForecastsList.get(position);
        if (dayWeather != null) {
            String date = position == 0 ? context.getString(R.string.weather_list_row_item_current_day_today_label, dayWeather.getDate()) : dayWeather.getDate();
            holder.weatherDate.setText(date);
            if (dayWeather.getWeatherConditions().size() > 0) {
                if (dayWeather.getWeatherConditions().get(0).getWeatherDescription().size() > 0)
                    holder.weatherDescription.setText(dayWeather.getWeatherConditions().get(0).getWeatherDescription().get(0).getWeatherDescription());
                holder.weatherRealFeelTemp.setText(context.getString(R.string.weather_list_row_item_real_feel_label, dayWeather.getWeatherConditions().get(0).getFeelsLikeC()));
                if (dayWeather.getWeatherConditions().get(0).getWeatherIconUrl().size() > 0) {
                    int iconSizeDimenId = position == 0 ? R.dimen.weather_current_day_icon_size : R.dimen.weather_next_day_icon_size;
                    Picasso.with(context).load(dayWeather.getWeatherConditions().get(0).getWeatherIconUrl().get(0).getWeatherIconUrl()).resizeDimen(iconSizeDimenId, iconSizeDimenId).into(holder.weatherIcon);
                }
            }
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
        public TextView weatherDate;
        public TextView weatherRealFeelTemp;
        public ImageView weatherIcon;

        public ViewHolder(View v) {
            super(v);
            weatherDescription = (TextView) v.findViewById(R.id.weather_description);
            weatherDate = (TextView) v.findViewById(R.id.weather_date);
            weatherRealFeelTemp = (TextView) v.findViewById(R.id.weather_real_feel_temp);
            weatherIcon = (ImageView) v.findViewById(R.id.weather_icon);
        }
    }
}
