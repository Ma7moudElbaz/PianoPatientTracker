package com.cat.pianopatienttracker.admin.dashboard.regional;

import com.cat.pianopatienttracker.admin.ranking.Ranking_sectors_item;

import java.util.ArrayList;

public class DashboardRegional_item {
    String name;
    ArrayList<ProductTarget_item> productTarget_items;

    public DashboardRegional_item(String name, ArrayList<ProductTarget_item> productTarget_items) {
        this.name = name;
        this.productTarget_items = productTarget_items;
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
