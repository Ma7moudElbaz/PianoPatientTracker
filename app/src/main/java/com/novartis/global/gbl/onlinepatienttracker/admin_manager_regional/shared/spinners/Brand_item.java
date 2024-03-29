package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners;

import java.io.Serializable;

public class Brand_item implements Serializable {
   private int id;
    private String name;
    private String imageUrl;

    public Brand_item(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
