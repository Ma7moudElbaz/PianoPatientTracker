package com.cat.pianopatienttracker.admin_manager_regional.dashboard.regional;

import java.util.ArrayList;

public class DashboardRegional_item {
    int id;
    String name;
    ArrayList<ProductTarget_item> productTarget_items;

    public DashboardRegional_item(int id, String name, ArrayList<ProductTarget_item> productTarget_items) {
        this.id = id;
        this.name = name;
        this.productTarget_items = productTarget_items;
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

    public ArrayList<ProductTarget_item> getProductTarget_items() {
        return productTarget_items;
    }

    public void setProductTarget_items(ArrayList<ProductTarget_item> productTarget_items) {
        this.productTarget_items = productTarget_items;
    }
}
