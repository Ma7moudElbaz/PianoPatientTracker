package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.ranking;

public class Ranking_doctors_item {
    int id;
    String name, hospital, address;
    int patientsNo;

    public Ranking_doctors_item(int id, String name, String hospital, String address, int patientsNo) {
        this.id = id;
        this.name = name;
        this.hospital = hospital;
        this.address = address;
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

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPatientsNo() {
        return patientsNo;
    }

    public void setPatientsNo(int patientsNo) {
        this.patientsNo = patientsNo;
    }
}
