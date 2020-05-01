package com.example.android.corodate.model;

import android.support.annotation.NonNull;

public class Corona {

    private String country;
    private int cases;
    private int deaths;
    private int recovered;
    private int active;
    private int todayCases;
    private int todayDeaths;
    private CountryInfo countryInfo;

    Corona() {}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(int todayCases) {
        this.todayCases = todayCases;
    }

    public int getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(int todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(CountryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }

    @NonNull
    @Override
    public String toString() {
        String space = " ";
        StringBuilder coronaData = new StringBuilder();

        coronaData.append(getCountry()).append(space)
                .append(getCases()).append(space)
                .append(getDeaths()).append(space)
                .append(getRecovered()).append(space)
                .append(getActive()).append(space)
                .append(getTodayCases()).append(space)
                .append(getTodayDeaths()).append(space)
                .append(getCountryInfo().getFlag());

        return coronaData.toString();
    }
}
