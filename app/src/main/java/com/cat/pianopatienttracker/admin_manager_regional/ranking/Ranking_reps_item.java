package com.cat.pianopatienttracker.admin_manager_regional.ranking;

public class Ranking_reps_item {
    int id;
    String name;
    int patientsNo;

    public Ranking_reps_item(int id, String name, int patientsNo) {
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
