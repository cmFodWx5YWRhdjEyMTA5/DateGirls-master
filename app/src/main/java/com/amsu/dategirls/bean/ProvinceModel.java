package com.amsu.dategirls.bean;

/**
 * Created by HP on 2016/11/29.
 */
import java.util.List;

public class ProvinceModel {
    private String name;
    private List<String> cityList;

    public ProvinceModel() {
        super();
    }

    public ProvinceModel(String name, List<String> cityList) {
        this.name = name;
        this.cityList = cityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "ProvinceModel{" +
                "name='" + name + '\'' +
                ", cityList=" + cityList +
                '}';
    }
}
