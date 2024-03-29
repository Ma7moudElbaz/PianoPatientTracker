package com.novartis.global.gbl.onlinepatienttracker.flm.requests;

public class Request_item {
    int id,patientId;
    String hospital,sector,doctor,dose,byName,process;

    public Request_item(int id, int patientId, String hospital, String sector, String doctor, String dose, String byName, String process) {
        this.id = id;
        this.patientId = patientId;
        this.hospital = hospital;
        this.sector = sector;
        this.doctor = doctor;
        this.dose = dose;
        this.byName = byName;
        this.process = process;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    public String getByName() {
        return byName;
    }

    public void setByName(String byName) {
        this.byName = byName;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
