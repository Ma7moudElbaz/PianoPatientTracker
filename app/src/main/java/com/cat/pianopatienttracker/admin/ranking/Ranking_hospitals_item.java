package com.cat.pianopatienttracker.admin.ranking;

public class Ranking_hospitals_item {
    int id;
    String name, sector,address;

    public Ranking_hospitals_item(int id, String name, String sector, String address) {
        this.id = id;
        this.name = name;
        this.sector = sector;
        this.address = address;
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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
