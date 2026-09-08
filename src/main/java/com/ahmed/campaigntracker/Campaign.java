package com.ahmed.campaigntracker;

import jakarta.persistence.*;

@Entity
public class Campaign {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String title;
    private String sparkCode;
    private int clicks;
    private double spend;
    private double ctr;
    private int conversions;
    private double landedRate;
    private double epc;
    private int impressions;
    private double cpm;
    private double cpc;
    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    public Campaign() {
    }

    public Campaign(String title, String sparkCode, int clicks, double spend, double ctr, int conversions, int impressions, double cpm, double cpc) {
        this.title = title;
        this.sparkCode = sparkCode;
        this.clicks = clicks;
        this.spend = spend;
        this.ctr = ctr;
        this.conversions = conversions;
        this.impressions = impressions;
        this.cpm = cpm;
        this.cpc = cpc;
        this.status = CampaignStatus.TESTING;
    }

    public double getLandedClicks() {
        return clicks * landedRate;
    }

    public double getRoas() {
        if (spend == 0) {
            return 0;
        }
        return getEstimatedRevenue() / spend;
    }

    public double getEstimatedRevenue() {
        return getLandedClicks() * epc;
    }

    public double getEstimatedProfit() {
        return getEstimatedRevenue() - spend;
    }

    public void setLandedRate(double landedRate) {
        this.landedRate = landedRate;
    }

    public void setEpc(double epc) {
        this.epc = epc;
    }

    public double getLandedRate() {
        return landedRate;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public double getEpc() {
        return epc;
    }

    public int getImpressions() {
        return impressions;
    }

    public double getCpm() {
        return cpm;
    }

    public double getCpc() {
        return cpc;
    }

    public void setSparkCode(String sparkCode) {
        this.sparkCode = sparkCode;
    }

    public String getTitle() {
        return title;
    }

    public String getSparkCode() {
        return sparkCode;
    }

    public int getClicks() {
        return clicks;
    }

    public double getSpend() {
        return spend;
    }

    public double getCtr() {
        return ctr;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public int getConversions() {
        return conversions;
    }

    public Long getId() {
        return id;
    }
}