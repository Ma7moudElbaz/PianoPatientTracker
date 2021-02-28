package com.cat.pianopatienttracker.admin_manager_regional.shared.spinners;

import java.io.Serializable;
import java.util.ArrayList;

public class Region_item implements Serializable {

    private int id;
    private String name;
    private ArrayList<City_item> city_list = new ArrayList<>();

    public Region_item(int id, String name, ArrayList<City_item> city_list) {
        this.id = id;
        this.name = name;
        this.city_list = city_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<City_item> getCity_list() {
        return city_list;
    }

    public void setCity_list(ArrayList<City_item> city_list) {
        this.city_list = city_list;
    }
}
