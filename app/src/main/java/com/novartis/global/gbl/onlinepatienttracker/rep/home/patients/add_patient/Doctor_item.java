package com.novartis.global.gbl.onlinepatienttracker.rep.home.patients.add_patient;

public class Doctor_item {

    private int id;
    private String name;

    public Doctor_item(int id, String name) {
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
