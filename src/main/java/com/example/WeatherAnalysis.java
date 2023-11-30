package com.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeatherAnalysis {
    public static void main(String[] args) {
        // Замените YOUR_WEATHERBIT_API_KEY на ваш реальный API-ключ
        String apiKey = "7bb3369ddcca41529f48d855d2fa7d51";
        String[] cities = { "Kiev", "London", "New-York", "Tokyo", "Sydney", "Chicago", "Moscow", "Beijing", "Dubai",
                "Mumbai" };

        // Создаем карту для хранения данных о текущей температуре и влажности для
        // каждого города
        Map<String, Double> currentTemperatures = new HashMap<>();
        Map<String, Double> currentHumidity = new HashMap<>();

        for (String city : cities) {
            String apiUrl = "https://api.weatherbit.io/v2.0/current?" +
                    "city=" + city +
                    "&key=" + apiKey;

            try {
                // Выполняем запрос к API
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Получаем ответ
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Парсим данные
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Получаем текущую температуру и влажность
                JSONObject data = jsonResponse.getJSONArray("data").getJSONObject(0);
                double currentTemperature = data.getDouble("temp");
                double currentHumidityValue = data.getDouble("rh");

                // Сохраняем результаты в карту
                currentTemperatures.put(city, currentTemperature);
                currentHumidity.put(city, currentHumidityValue);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Map.Entry<String, Double>> sortedTemperatures = currentTemperatures.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Вывод температур
        System.out.println("Температура (по убыванию):");
        for (Map.Entry<String, Double> entry : sortedTemperatures) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " °C");
        }

        // Сортировка влажности по убыванию
        List<Map.Entry<String, Double>> sortedHumidity = currentHumidity.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Вывод влажности
        System.out.println("\nВлажность (по убыванию):");
        for (Map.Entry<String, Double> entry : sortedHumidity) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " %");
        }
    }
}