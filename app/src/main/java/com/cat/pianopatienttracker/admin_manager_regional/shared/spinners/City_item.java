package com.cat.pianopatienttracker.admin_manager_regional.shared.spinners;

import java.io.Serializable;

public class City_item implements Serializable {

    private int id;
    private String name;

    public City_item(int id, String name) {
        this.id = id;
        this.name = name;
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
}
