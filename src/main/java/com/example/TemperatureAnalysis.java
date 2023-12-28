package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

class TemperatureAnalysis implements Runnable {
    private List<DataEntry> temperatureDataList;

    public List<DataEntry> getTemperatureDataList() {
        return temperatureDataList;
    }

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

                temperatureDataList = new ArrayList<>();
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