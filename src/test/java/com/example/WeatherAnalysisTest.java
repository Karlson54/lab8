package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class WeatherAnalysisTest {

    private TemperatureAnalysis temperatureAnalysis;
    private PrecipitationAnalysis precipitationAnalysis;

    @BeforeEach
    public void setUp() {
        temperatureAnalysis = new TemperatureAnalysis();
        precipitationAnalysis = new PrecipitationAnalysis();
    }

    @Test
    public void testTemperatureAnalysis() {
        temperatureAnalysis.run();

        List<DataEntry> temperatureDataList = temperatureAnalysis.getTemperatureDataList();

        assertFalse(temperatureDataList.isEmpty());

        assertTrue(isSortedDescending(temperatureDataList));
    }

    @Test
    public void testPrecipitationAnalysis() {
        precipitationAnalysis.run();

        List<DataEntry> precipitationDataList = precipitationAnalysis.getPrecipitationDataList();

        assertFalse(precipitationDataList.isEmpty());

        assertTrue(isSortedDescending(precipitationDataList));
    }

    private boolean isSortedDescending(List<DataEntry> dataList) {
        for (int i = 1; i < dataList.size(); i++) {
            if (dataList.get(i - 1).getValue() < dataList.get(i).getValue()) {
                return false;
            }
        }
        return true;
    }
}
