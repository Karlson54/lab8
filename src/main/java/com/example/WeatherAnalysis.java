package com.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

class TemperatureAnalysis implements Runnable {
    @Override
    public void run() {
        try {
            URL url = new URL(
                    "https://archive-api.open-meteo.com/v1/archive?latitude=50.45466&longitude=30.5238&start_date=2023-01-01&end_date=2023-12-26&daily=temperature_2m_max");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());

                JSONObject dailyData = jsonResponse.getJSONObject("daily");

                JSONArray timeArray = dailyData.getJSONArray("time");
                JSONArray temperatureArray = dailyData.getJSONArray("temperature_2m_max");

                List<DataEntry> temperatureDataList = new ArrayList<>();
                for (int i = 0; i < timeArray.length(); i++) {
                    String time = timeArray.getString(i);
                    double temperature = temperatureArray.getDouble(i);

                    temperatureDataList.add(new DataEntry(time, temperature));
                }

                Collections.sort(temperatureDataList, Collections.reverseOrder());

                System.out.println();
                System.out.println("Top 10 hottest days:");
                System.out.println();
                for (int i = 0; i < Math.min(10, temperatureDataList.size()); i++) {
                    DataEntry data = temperatureDataList.get(i);
                    System.out.println("Time: " + data.time);
                    System.out.println("Temperature: " + data.value);
                }
                System.out.println();

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(new PrecipitationAnalysis());
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class PrecipitationAnalysis implements Runnable {
    @Override
    public void run() {
        try {
            URL url = new URL(
                    "https://archive-api.open-meteo.com/v1/archive?latitude=50.45466&longitude=30.5238&start_date=2023-01-01&end_date=2023-12-26&daily=precipitation_sum");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());

                JSONObject dailyData = jsonResponse.getJSONObject("daily");

                JSONArray timeArray = dailyData.getJSONArray("time");
                JSONArray precipitationArray = dailyData.getJSONArray("precipitation_sum");

                List<DataEntry> precipitationDataList = new ArrayList<>();
                int consecutiveRainyDays = 0;

                for (int i = 0; i < timeArray.length(); i++) {
                    String time = timeArray.getString(i);
                    double precipitation = precipitationArray.getDouble(i);

                    precipitationDataList.add(new DataEntry(time, precipitation));

                    if (precipitation > 0) {
                        consecutiveRainyDays++;
                    } else {
                        consecutiveRainyDays = 0;
                    }

                    if (consecutiveRainyDays == 7) {
                        System.out.println("Found 7 consecutive rainy days starting from " + time);
                        System.out.println();
                    }
                }

                Collections.sort(precipitationDataList, Collections.reverseOrder());

                System.out.println("Top 10 days with the highest precipitation:");
                System.out.println();
                for (int i = 0; i < Math.min(10, precipitationDataList.size()); i++) {
                    DataEntry data = precipitationDataList.get(i);
                    System.out.println("Time: " + data.time);
                    System.out.println("Precipitation: " + data.value);
                }
            }
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

    @Override
    public int compareTo(DataEntry other) {
        return Double.compare(this.value, other.value);
    }
}
