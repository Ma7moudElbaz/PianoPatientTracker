package com.cat.pianopatienttracker.regional_product.ranking;

public class Ranking_hospitals_item {
    int id;
    String name, sector,address;
    int doctorsNo,patientsNo;

    public Ranking_hospitals_item(int id, String name, String sector, String address, int doctorsNo, int patientsNo) {
        this.id = id;
        this.name = name;
        this.sector = sector;
        this.address = address;
        this.doctorsNo = doctorsNo;
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

    public int getDoctorsNo() {
        return doctorsNo;
    }

    public void setDoctorsNo(int doctorsNo) {
        this.doctorsNo = doctorsNo;
    }

    public int getPatientsNo() {
        return patientsNo;
    }

    public void setPatientsNo(int patientsNo) {
        this.patientsNo = patientsNo;
    }
}
