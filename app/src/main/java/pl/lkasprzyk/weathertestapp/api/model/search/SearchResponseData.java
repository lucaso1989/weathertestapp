package pl.lkasprzyk.weathertestapp.api.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 2015-02-27.
 */
public class SearchResponseData {

    @Expose
    @SerializedName("search_api")
    private SearchData searchData;

    public SearchData getSearchData() {
        return searchData;
    }

    public void setSearchData(SearchData searchData) {
        this.searchData = searchData;
    }
}
