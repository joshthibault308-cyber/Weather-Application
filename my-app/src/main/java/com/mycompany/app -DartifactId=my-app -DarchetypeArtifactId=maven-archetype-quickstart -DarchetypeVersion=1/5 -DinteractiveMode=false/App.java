package com.mycompany.app;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Handler());
        server.start();

    }

    static String WeatherAPI(String API) {

        try {

            URL url = new URL(API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {

                throw new RuntimeException(String.valueOf(responseCode));

            } else {

                StringBuilder string = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {

                    string.append(scanner.nextLine());

                }

                scanner.close();
                return (String.valueOf(string));

            }

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    static int getTemp(String placeCoordinates, int daysAfter) {

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
        String dateTime = "startTime\": \"" + date;

        if (daysAfter == 0) {
            String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
            int startIndex = coordinates.indexOf("forecastHourly") + 18;
            coordinates = coordinates.substring(startIndex);
            int endIndex = coordinates.indexOf("\"");
            coordinates = coordinates.substring(0, endIndex);


            String text = WeatherAPI(coordinates);

            startIndex = text.indexOf(dateTime);
            text = text.substring(startIndex);

            startIndex = text.indexOf("temperature") + 14;
            text = text.substring(startIndex, startIndex + 3);
            text = text.replaceAll("\\D", "");

            return Integer.parseInt(text);

        } else {

            String dayofWeek = String.valueOf(LocalDateTime.now().plusDays(daysAfter).getDayOfWeek());
            dayofWeek = dayofWeek.charAt(0) + dayofWeek.substring(1).toLowerCase();

            String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
            int startIndex = coordinates.indexOf("forecast\": ") + 12;
            coordinates = coordinates.substring(startIndex);
            int endIndex = coordinates.indexOf("\"");
            coordinates = coordinates.substring(0, endIndex);


            String text = WeatherAPI(coordinates);

            startIndex = text.indexOf(dayofWeek);
            text = text.substring(startIndex);

            startIndex = text.indexOf("temperature") + 14;
            text = text.substring(startIndex, startIndex + 3);
            text = text.replaceAll("\\D", "");

            return Integer.parseInt(text);

        }

    }

    static String tempPicture(int temperature) {

        String hot = "<img src=\"https://images.unsplash.com/vector-1756860574578-02605c8c8513?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D\" width=\"25\" height=\"25\"\"> ";
        String warm = "<img src=\"https://images.unsplash.com/vector-1738925114917-a04836908f34?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D\" width=\"25\" height=\"25\"\"> ";
        String cold = "<img src=\"https://images.unsplash.com/vector-1747850626323-30a2e114df38?q=80&w=880&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D\" width=\"25\" height=\"25\"\"> ";

        if (temperature >= 90) {

            return hot;

        } else if (temperature <= 70) {

            return cold;

        } else {

            return warm;

        }
    }

    static String apiPicture(String placeCoordinates, int daysAfter) {

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
        String dateTime = "startTime\": \"" + date;

        if (daysAfter == 0) {
            String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
            int startIndex = coordinates.indexOf("forecastHourly") + 18;
            coordinates = coordinates.substring(startIndex);
            int endIndex = coordinates.indexOf("\"");
            coordinates = coordinates.substring(0, endIndex);


            String text = WeatherAPI(coordinates);

            startIndex = text.indexOf(dateTime);
            text = text.substring(startIndex);

            startIndex = text.indexOf("icon") + 7;
            text = text.substring(startIndex);
            endIndex = text.indexOf(", ");
            text = text.substring(0, endIndex);

            return "<img src=" + text + " width=\"25\" height=\"25\"\"> ";


        } else {

            String dayofWeek = String.valueOf(LocalDateTime.now().plusDays(daysAfter).getDayOfWeek());
            dayofWeek = dayofWeek.charAt(0) + dayofWeek.substring(1).toLowerCase();

            String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
            int startIndex = coordinates.indexOf("forecast\": ") + 12;
            coordinates = coordinates.substring(startIndex);
            int endIndex = coordinates.indexOf('"');
            coordinates = coordinates.substring(0, endIndex);


            String text = WeatherAPI(coordinates);

            startIndex = text.indexOf(dayofWeek);
            text = text.substring(startIndex);

            startIndex = text.indexOf("icon") + 7;
            text = text.substring(startIndex);
            endIndex = text.indexOf(", ");
            text = text.substring(0, endIndex);

            return "<img src=" + text + " width=\"25\" height=\"25\"\"> ";

        }

    }

    static String stringForecast(String placeCoordinates, int daysAfter) {

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
        String dateTime = "startTime\": \"" + date;

        if (daysAfter == 0) {
            String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
            int startIndex = coordinates.indexOf("forecastHourly") + 18;
            coordinates = coordinates.substring(startIndex);
            int endIndex = coordinates.indexOf("\"");
            coordinates = coordinates.substring(0, endIndex);


            String text = WeatherAPI(coordinates);

            startIndex = text.indexOf(dateTime);
            text = text.substring(startIndex);

            startIndex = text.indexOf("shortForecast") + 17;
            text = text.substring(startIndex);
            endIndex = text.indexOf('"');
            text = text.substring(0, endIndex);


            return text;

        } else {

            String dayofWeek = String.valueOf(LocalDateTime.now().plusDays(daysAfter).getDayOfWeek());
            dayofWeek = dayofWeek.charAt(0) + dayofWeek.substring(1).toLowerCase();

            String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
            int startIndex = coordinates.indexOf("forecast\": ") + 12;
            coordinates = coordinates.substring(startIndex);
            int endIndex = coordinates.indexOf("\"");
            coordinates = coordinates.substring(0, endIndex);


            String text = WeatherAPI(coordinates);

            startIndex = text.indexOf(dayofWeek);
            text = text.substring(startIndex);

            startIndex = text.indexOf("detailedForecast") + 20;
            text = text.substring(startIndex);
            endIndex = text.indexOf('"');
            text = text.substring(0, endIndex);


            return text;

        }

    }

    static String tempHTML(String placeCoordinates, int daysAfter) {

        int temperature = getTemp(placeCoordinates, daysAfter);
        String image = tempPicture(temperature);
        String image2 = apiPicture(placeCoordinates, daysAfter);
        String detailedForecast = stringForecast(placeCoordinates, daysAfter);

        String dayofWeek = String.valueOf(LocalDateTime.now().plusDays(daysAfter).getDayOfWeek());
        dayofWeek = dayofWeek.charAt(0) + dayofWeek.substring(1).toLowerCase();

        if (daysAfter == 0) {

            return image + image2 + "Hourly Temperature: " + temperature + "<br>" + detailedForecast + "<br> <br>";

        } else {

            return image + image2 + dayofWeek + " Temperature: " + temperature + "<br>" + detailedForecast + "<br> <br>";

        }

    }

    static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {

            String method = exchange.getRequestMethod();

            String response = "<html> <body style=\"background-color:#B0E0E6;\"> <img src=\"https://images.unsplash.com/photo-1601134467661-3d775b999c8b?q=80&w=1375&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D\" width=\"800\" height=\"500\" style=\"display : block; margin-bottom: 100-pk;\"> " +
                    tempHTML("29.17,-81.07", 0) +
                    tempHTML("29.17,-81.07", 1) +
                    tempHTML("29.17,-81.07", 2) +
                    tempHTML("29.17,-81.07", 3) +
                    tempHTML("29.17,-81.07", 4) +
                    tempHTML("29.17,-81.07", 5) +
                    tempHTML("29.17,-81.07", 6) +

                    " <form action=\"/\" method=\"POST\">" + " <button type=\"submit\">Refresh</button>" + "<button onClick=\"\">Daytona Beach, Florida</button>" + "</body> </html>";

            try {

                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (IOException e) {

                System.out.print(e.getMessage());

            }
        }
    }
}