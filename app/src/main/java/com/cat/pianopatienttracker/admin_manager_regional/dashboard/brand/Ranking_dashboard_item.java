package com.cat.pianopatienttracker.admin_manager_regional.dashboard.brand;

public class Ranking_dashboard_item {
    private int id;
    private String name;
    private int patientsNo;

    public Ranking_dashboard_item(int id, String name, int patientsNo) {
        this.id = id;
        this.name = name;
        this.patientsNo = patientsNo;
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

    public int getPatientsNo() {
        return patientsNo;
    }

    public void setPatientsNo(int patientsNo) {
        this.patientsNo = patientsNo;
    }
}
