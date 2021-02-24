package com.cat.pianopatienttracker.regional_product.shared;

import java.util.ArrayList;

public class Country_Brand_item {
    private int id;
    private String name;
    private ArrayList<Brand_item> brand_list = new ArrayList<>();

    public Country_Brand_item(int id, String name, ArrayList<Brand_item> brand_list) {
        this.id = id;
        this.name = name;
        this.brand_list = brand_list;
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

    public ArrayList<Brand_item> getBrand_list() {
        return brand_list;
    }

    public void setBrand_list(ArrayList<Brand_item> brand_list) {
        this.brand_list = brand_list;
    }
}
