package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.shared.spinners;

public class Sector_item {

    private int id;
    private String name;

    public Sector_item(int id, String name) {
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
