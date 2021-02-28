package com.cat.pianopatienttracker.admin_manager_regional.shared.spinners;

import java.util.ArrayList;

public class Country_item {
    private int id;
    private String name,iso;
    private ArrayList<Brand_item> brand_list = new ArrayList<>();
    private ArrayList<Region_item> region_list = new ArrayList<>();

    public Country_item(int id, String name, String iso, ArrayList<Brand_item> brand_list, ArrayList<Region_item> region_list) {
        this.id = id;
        this.name = name;
        this.iso = iso;
        this.brand_list = brand_list;
        this.region_list = region_list;
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

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public ArrayList<Brand_item> getBrand_list() {
        return brand_list;
    }

    public void setBrand_list(ArrayList<Brand_item> brand_list) {
        this.brand_list = brand_list;
    }

    public ArrayList<Region_item> getRegion_list() {
        return region_list;
    }

    public void setRegion_list(ArrayList<Region_item> region_list) {
        this.region_list = region_list;
    }
}
