package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

class PrecipitationAnalysis implements Runnable {
    private List<DataEntry> precipitationDataList;

    public List<DataEntry> getPrecipitationDataList() {
        return precipitationDataList;
    }

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

                precipitationDataList = new ArrayList<>();
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