package com.novartis.global.gbl.onlinepatienttracker.rep.home.patients;

public class Patient_item {
    int id;
    String hospital,sector,doctor,dose;

    public Patient_item(int id, String hospital, String sector, String doctor, String dose) {
        this.id = id;
        this.hospital = hospital;
        this.sector = sector;
        this.doctor = doctor;
        this.dose = dose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
