package com.mycompany.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppTest {

  @ParameterizedTest()
  @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6})
  @DisplayName("Test showing if temperature comes back as int")
  void temperatureIsInteger(int days) {

    int temperature = App.getTemp("29.17,-81.07", days);

    Assertions.assertInstanceOf(Integer.class, temperature);

  }

  @ParameterizedTest()
  @ValueSource(ints = {-20, 40, 60, 80, 90, 100})
  @DisplayName("Test showing if the temperature matches the correct image")
  void correctTemperatureImages(int temperature) {

    String hot = "https://images.unsplash.com/vector-1756860574578-02605c8c8513?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
    String warm = "https://images.unsplash.com/vector-1738925114917-a04836908f34?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
    String cold = "https://images.unsplash.com/vector-1747850626323-30a2e114df38?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

    String image = App.tempPicture(temperature);

    if (temperature >= 90) {

      Assertions.assertTrue(image.contains(hot));

    } else if (temperature <= 70) {

      Assertions.assertTrue(image.contains(cold));

    } else {

      Assertions.assertTrue(image.contains(warm));

    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"30.18,-82.58", "29.17,-81.07", "45.03,-102.02", "32.33,-102.53", "32.35,-86.98", "31.35,-100.40"})
  @DisplayName("Test showing multiple location temperatures for the current day")
  void worksWithMultipleLocationsForCurrentDay(String locations) {

    int day = 0;

    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
    String dateTime = "startTime\": \"" + date;

    App.WeatherAPI("https://api.weather.gov/points/" + locations + "#/properties");
    String coordinates = App.WeatherAPI("https://api.weather.gov/points/" + locations + "#/properties");
    int startIndex = coordinates.indexOf("forecastHourly") + 18;
    coordinates = coordinates.substring(startIndex);
    int endIndex = coordinates.indexOf("\"");
    coordinates = coordinates.substring(0, endIndex);

    String exampleTemp = App.WeatherAPI(coordinates);
    startIndex = exampleTemp.indexOf(dateTime);
    exampleTemp = exampleTemp.substring(startIndex);

    startIndex = exampleTemp.indexOf("temperature") + 14;
    exampleTemp = exampleTemp.substring(startIndex, startIndex + 3);
    exampleTemp = exampleTemp.replaceAll("\\D", "");

    Assertions.assertEquals(Integer.parseInt(exampleTemp), App.getTemp(locations, day));

  }

  @ParameterizedTest
  @ValueSource(strings = {"30.18,-82.58", "29.17,-81.07", "45.03,-102.02", "32.33,-102.53", "32.35,-86.98"})
  @DisplayName("Test showing multiple location temperatures for days ahead")
  void worksWithMultipleLocationsForDayAhead(String locations) {

    int day = 2;

    String dayOfWeek = String.valueOf(LocalDateTime.now().plusDays(day).getDayOfWeek());
    dayOfWeek = dayOfWeek.charAt(0) + dayOfWeek.substring(1).toLowerCase();

    String coordinates = App.WeatherAPI("https://api.weather.gov/points/" + locations + "#/properties");
    int startIndex = coordinates.indexOf("forecast\": ") + 12;
    coordinates = coordinates.substring(startIndex);
    int endIndex = coordinates.indexOf("\"");
    coordinates = coordinates.substring(0, endIndex);


    String exampleTemp = App.WeatherAPI(coordinates);

    startIndex = exampleTemp.indexOf(dayOfWeek);
    exampleTemp = exampleTemp.substring(startIndex);

    startIndex = exampleTemp.indexOf("temperature") + 14;
    exampleTemp = exampleTemp.substring(startIndex, startIndex + 3);
    exampleTemp = exampleTemp.replaceAll("\\D", "");

    Assertions.assertEquals(Integer.parseInt(exampleTemp), App.getTemp(locations, day));

  }
}