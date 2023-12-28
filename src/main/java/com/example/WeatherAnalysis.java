package com.example;

public class WeatherAnalysis {
    public static void main(String[] args) {
        try {
            Thread temperatureThread = new Thread(new TemperatureAnalysis());
            temperatureThread.start();
            temperatureThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class DataEntry implements Comparable<DataEntry> {
    String time;
    double value;

    public DataEntry(String time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(DataEntry other) {
        return Double.compare(this.value, other.value);
    }
}