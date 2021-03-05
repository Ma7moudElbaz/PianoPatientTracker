package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.dashboard.regional;

import java.util.ArrayList;

public class DashboardRegional_item {
    int id;
    String name,iso;
    ArrayList<ProductTarget_item> productTarget_items;

    public DashboardRegional_item(int id, String name, String iso, ArrayList<ProductTarget_item> productTarget_items) {
        this.id = id;
        this.name = name;
        this.iso = iso;
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

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public ArrayList<ProductTarget_item> getProductTarget_items() {
        return productTarget_items;
    }

    public void setProductTarget_items(ArrayList<ProductTarget_item> productTarget_items) {
        this.productTarget_items = productTarget_items;
    }
}
