package com.example;

public class CityData {
    private String cityName;
    private double temperature;

    public CityData(String cityName, double temperature) {
        this.cityName = cityName;
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }
}