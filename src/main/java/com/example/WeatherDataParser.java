package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

public class WeatherDataParser {
    public static List<CityData> getTopHottestCities(List<String> cities) {
        List<CityData> cityDataList = new ArrayList<>();

        try {
            for (String city : cities) {
                String weatherData = OpenWeatherMapAPI.getWeatherData(city);
                double temperature = getTemperature(weatherData);

                cityDataList.add(new CityData(city, temperature));
            }

            cityDataList.sort(Comparator.comparingDouble(CityData::getTemperature));

            System.out.println("Top 10 Hottest Cities:");
            for (int i = cityDataList.size() - 1; i >= Math.max(0, cityDataList.size() - 10); i--) {
                if (i < cityDataList.size()) {
                    System.out.println(cityDataList.get(i).getCityName() + ": " + cityDataList.get(i).getTemperature());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityDataList;
    }

    public static List<CityData> getRainiestCities(List<String> cities) {
        List<CityData> cityDataList = new ArrayList<>();

        try {
            for (String city : cities) {
                String weatherData = OpenWeatherMapAPI.getWeatherData(city);
                double humidity = getHumidity(weatherData);

                cityDataList.add(new CityData(city, humidity));
            }

            cityDataList.sort(Comparator.comparingDouble(CityData::getTemperature));

            System.out.println("Top 10 Rainiest Cities:");
            for (int i = cityDataList.size() - 1; i >= Math.max(0, cityDataList.size() - 10); i--) {
                if (i < cityDataList.size()) {
                    System.out.println(cityDataList.get(i).getCityName() + ": " + cityDataList.get(i).getTemperature());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityDataList;
    }

    public static double getTemperature(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject main = jsonObject.getJSONObject("main");
        return main.getDouble("temp");
    }

    public static double getHumidity(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject main = jsonObject.getJSONObject("main");
        return main.getDouble("humidity");
    }


    public static void main(String[] args) {
        List<String> cities = Arrays.asList("Kyiv", "Paris", "Tokyo", "New York", "London", "Berlin", "Beijing",
                "Moscow", "Dubai", "Sydney");

        getTopHottestCities(cities);
        System.out.println("\n");
        getRainiestCities(cities);
    }
}
