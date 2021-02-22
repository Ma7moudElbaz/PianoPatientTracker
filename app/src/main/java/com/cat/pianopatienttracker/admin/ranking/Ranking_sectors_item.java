package com.cat.pianopatienttracker.admin.ranking;

public class Ranking_sectors_item {
    int id;
    String name;
    int doctorsNo, hospitalsNo;

    public Ranking_sectors_item(int id, String name, int doctorsNo, int hospitalsNo) {
        this.id = id;
        this.name = name;
        this.doctorsNo = doctorsNo;
        this.hospitalsNo = hospitalsNo;
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

    public int getDoctorsNo() {
        return doctorsNo;
    }

    public void setDoctorsNo(int doctorsNo) {
        this.doctorsNo = doctorsNo;
    }

    public int getHospitalsNo() {
        return hospitalsNo;
    }

    public void setHospitalsNo(int hospitalsNo) {
        this.hospitalsNo = hospitalsNo;
    }
}
