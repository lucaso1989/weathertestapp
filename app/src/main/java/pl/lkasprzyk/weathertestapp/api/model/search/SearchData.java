package pl.lkasprzyk.weathertestapp.api.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lucas on 2015-02-27.
 */
public class SearchData {

    @Expose
    @SerializedName("result")
    private List<SearchResult> result;


    public List<SearchResult> getResult() {
        return result;
    }

    public void setResult(List<SearchResult> result) {
        this.result = result;
    }
}
