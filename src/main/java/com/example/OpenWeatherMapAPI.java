package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherMapAPI {

    private static final String API_KEY = "9358279448d5685726fe933c480c18a7";
    private static final String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public static String getWeatherData(String city) throws Exception {
        String apiUrl = String.format(API_ENDPOINT, city, API_KEY);
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        return response.toString();
    }
}