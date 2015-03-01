package pl.lkasprzyk.weathertestapp.api.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 2015-02-27.
 */
public class Area {

    @Expose
    @SerializedName("value")
    private String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
