package com.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeatherAnalysis2 {
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

        for (String city : cities) {
            // Задаем период для получения исторических данных (например, последние 10 дней)
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(8); // Пример: последние 10 дней

            String startDateStr = startDate.toString();
            String endDateStr = endDate.toString();

            String apiUrl = "https://api.weatherbit.io/v2.0/history/daily?" +
                    "city=" + city +
                    "&start_date=" + startDateStr +
                    "&end_date=" + endDateStr +
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
                // System.out.println(jsonResponse);

                // Получаем исторические данные о погоде
                JSONArray historyData = jsonResponse.getJSONArray("data");
                // System.out.println(historyData);
                // Анализируем исторические данные для дождливых дней
                int consecutiveRainyDays = 0;
                for (int i = 0; i < historyData.length(); i++) {
                    JSONObject dayData = historyData.getJSONObject(i);
                    double precipitation = dayData.getDouble("rh");

                    // Если есть дождь, увеличиваем счетчик дней с дождем
                    if (precipitation > 50) {
                        consecutiveRainyDays++;
                    } else {
                        consecutiveRainyDays = 0; // Сбрасываем счетчик, если день без дождя
                    }

                    // Если найдено больше 7 дней подряд дождь, выводим информацию
                    if (consecutiveRainyDays > 7) {
                        System.out.println(city + " был дождь более 7 дней подряд.");
                        break; // Прекращаем анализ, так как условие уже выполнено
                    }

                }

                int consecutiveDaysWithTemperatureIncrease = 0;
                for (int i = 1; i < historyData.length(); i++) {
                    JSONObject currentDayData = historyData.getJSONObject(i);
                    JSONObject previousDayData = historyData.getJSONObject(i - 1);

                    double currentTemperature = currentDayData.getDouble("temp");
                    double previousTemperature = previousDayData.getDouble("temp");

                    // Если температура выросла на 5°C или более, увеличиваем счетчик
                    if (currentTemperature - previousTemperature >= 1) {
                        consecutiveDaysWithTemperatureIncrease++;
                    } else {
                        consecutiveDaysWithTemperatureIncrease = 0; // Сбрасываем счетчик
                    }

                    // Если найдено 5 дней подряд с ростом температуры, выводим информацию
                    if (consecutiveDaysWithTemperatureIncrease >= 3) {
                        System.out.println(city + ": Температура выросла на 1°C или более в течение 3 дней.");
                        break; // Прекращаем анализ, так как условие выполнено
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}