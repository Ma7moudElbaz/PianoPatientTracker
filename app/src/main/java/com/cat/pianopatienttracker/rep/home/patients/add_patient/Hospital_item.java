package com.cat.pianopatienttracker.rep.home.patients.add_patient;

import com.cat.pianopatienttracker.admin_manager_regional.shared.spinners.Brand_item;

import java.util.ArrayList;

public class Hospital_item {
    private int id,sectorId;
    private String name;
    private ArrayList<Doctor_item> doctors_list = new ArrayList<>();

    public Hospital_item(int id, int sectorId, String name, ArrayList<Doctor_item> doctors_list) {
        this.id = id;
        this.sectorId = sectorId;
        this.name = name;
        this.doctors_list = doctors_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Doctor_item> getDoctors_list() {
        return doctors_list;
    }

    public void setDoctors_list(ArrayList<Doctor_item> doctors_list) {
        this.doctors_list = doctors_list;
    }
}
