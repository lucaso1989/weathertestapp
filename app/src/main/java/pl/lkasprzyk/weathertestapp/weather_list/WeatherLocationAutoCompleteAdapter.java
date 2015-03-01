package pl.lkasprzyk.weathertestapp.weather_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.lkasprzyk.weathertestapp.R;
import pl.lkasprzyk.weathertestapp.api.model.search.SearchResult;

/**
 * Created by Lucas on 2015-03-01.
 */
public class WeatherLocationAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 5;
    private Context context;
    private List<SearchResult> resultList = new ArrayList<SearchResult>();
    private OnSearchLocationQueryListener listener;

    public interface OnSearchLocationQueryListener {
        public List<SearchResult> OnLocationSearch(String query);
    }


    public WeatherLocationAutoCompleteAdapter(Context context, OnSearchLocationQueryListener listener) {
        this.context = context;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public SearchResult getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResult searchResult = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_location_autocomplete_list_row_item, parent, false);
            holder = new ViewHolder();
            holder.locationName = (TextView) convertView.findViewById(R.id.location_name);
            holder.locationDescription = (TextView) convertView.findViewById(R.id.location_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (searchResult != null) {
            if (searchResult.getArea().size() > 0)
                holder.locationName.setText(searchResult.getArea().get(0).getAreaName());
            if (searchResult.getCountry().size() > 0)
                holder.locationDescription.setText(searchResult.getCountry().get(0).getCountryName());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView locationName;
        TextView locationDescription;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<SearchResult> searchResults = findLocation(context, constraint.toString());

                    filterResults.values = searchResults;
                    filterResults.count = searchResults.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<SearchResult>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    private List<SearchResult> findLocation(Context context, String query) {
        return listener.OnLocationSearch(query);
    }
}
