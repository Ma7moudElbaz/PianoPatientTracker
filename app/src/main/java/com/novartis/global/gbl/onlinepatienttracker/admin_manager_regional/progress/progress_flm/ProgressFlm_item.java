package com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.progress.progress_flm;

public class ProgressFlm_item {
    private int id;
    private String name, address;
    int actualTarget, totalTarget;
    double targetPercent;
    int actualMarket, totalMarket;
    double marketPercent;

    public ProgressFlm_item(int id, String name, String address, int actualTarget, int totalTarget, double targetPercent, int actualMarket, int totalMarket, double marketPercent) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.actualTarget = actualTarget;
        this.totalTarget = totalTarget;
        this.targetPercent = targetPercent;
        this.actualMarket = actualMarket;
        this.totalMarket = totalMarket;
        this.marketPercent = marketPercent;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getActualTarget() {
        return actualTarget;
    }

    public void setActualTarget(int actualTarget) {
        this.actualTarget = actualTarget;
    }

    public int getTotalTarget() {
        return totalTarget;
    }

    public void setTotalTarget(int totalTarget) {
        this.totalTarget = totalTarget;
    }

    public double getTargetPercent() {
        return targetPercent;
    }

    public void setTargetPercent(double targetPercent) {
        this.targetPercent = targetPercent;
    }

    public int getActualMarket() {
        return actualMarket;
    }

    public void setActualMarket(int actualMarket) {
        this.actualMarket = actualMarket;
    }

    public int getTotalMarket() {
        return totalMarket;
    }

    public void setTotalMarket(int totalMarket) {
        this.totalMarket = totalMarket;
    }

    public double getMarketPercent() {
        return marketPercent;
    }

    public void setMarketPercent(double marketPercent) {
        this.marketPercent = marketPercent;
    }
}
