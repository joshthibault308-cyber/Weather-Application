package com.mycompany.app;

import com.sun.net.httpserver.*;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    private static final Logger logger = LogManager.getLogger("weather");

    public static void main(String[] args) throws IOException {

        logger.info("test");

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

    static String dayTimeBackground(String placeCoordinates) {

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
        String dateTime = "startTime\": \"" + date;

        String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
        int startIndex = coordinates.indexOf("forecastHourly") + 18;
        coordinates = coordinates.substring(startIndex);
        int endIndex = coordinates.indexOf("\"");
        coordinates = coordinates.substring(0, endIndex);


        String text = WeatherAPI(coordinates);

        startIndex = text.indexOf(dateTime);
        text = text.substring(startIndex);

        startIndex = text.indexOf("isDaytime") + 12;
        text = text.substring(startIndex);
        endIndex = text.indexOf(",");
        text = text.substring(0, endIndex);

        if (text.equals("true")) {

            return "#B0E0E6";

        } else {

            return "#9999FF";

        }

    }

    static String wind(String placeCoordinates) {

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
        String dateTime = "startTime\": \"" + date;

        String coordinates = WeatherAPI("https://api.weather.gov/points/" + placeCoordinates + "#/properties");
        int startIndex = coordinates.indexOf("forecastHourly") + 18;
        coordinates = coordinates.substring(startIndex);
        int endIndex = coordinates.indexOf("\"");
        coordinates = coordinates.substring(0, endIndex);


        String text = WeatherAPI(coordinates);
        startIndex = text.indexOf(dateTime);
        text = text.substring(startIndex);

        startIndex = text.indexOf("windSpeed") + 13;
        text = text.substring(startIndex);
        endIndex = text.indexOf("\",");
        text = text.substring(0, endIndex);


        String text2 = WeatherAPI(coordinates);
        startIndex = text2.indexOf(dateTime);
        text2 = text2.substring(startIndex);

        startIndex = text2.indexOf("windDirection") + 17;
        text2 = text2.substring(startIndex);
        endIndex = text2.indexOf("\",");
        text2 = text2.substring(0, endIndex);

        return text + " " + text2 ;

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
        String wind = wind(placeCoordinates);

        String dayofWeek = String.valueOf(LocalDateTime.now().plusDays(daysAfter).getDayOfWeek());
        dayofWeek = dayofWeek.charAt(0) + dayofWeek.substring(1).toLowerCase();

        if (daysAfter == 0) {

            return image + image2 + "Hourly Temperature: " + temperature + "<br>" + "Wind: " + wind + "<br>" + detailedForecast + "<br> <br>";

        } else {

            return image + image2 + dayofWeek + " Temperature: " + temperature + "<br>" + detailedForecast + "<br> <br>";

        }

    }

    static String mainPicture(String placeCoordinates) {

        String regular = "<img src=\"https://images.unsplash.com/photo-1601134467661-3d775b999c8b?q=80&w=1375&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D\" width=\"800\" height=\"500\" style=\"display : block; margin-bottom: 100-pk;\"> ";
        String rainy = "<img src=\"https://images.unsplash.com/photo-1626124902047-f3db8b02f740?q=80&w=987&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D\" width=\"800\" height=\"500\" style=\"display : block; margin-bottom: 100-pk;\"> ";
        String cloudy = "<img src=\"https://plus.unsplash.com/premium_photo-1733317236155-b0e1a2930f37?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D\" width=\"800\" height=\"500\" style=\"display : block; margin-bottom: 100-pk;\"> ";

        String forecast = stringForecast(placeCoordinates, 0);

        if ((forecast.toLowerCase().contains("rain")) || (forecast.toLowerCase().contains("shower")) || (forecast.toLowerCase().contains("thunderstorm"))) {

            return rainy;

        } else if(forecast.toLowerCase().contains("cloud")) {

            return cloudy;

        } else {

            return regular;

        }

    }

    static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {

            String method = exchange.getRequestMethod();

            String response = "<html>" +
                    "<head><style>" +
                    "#dallas-toggle-box {display:none;}" +
                    ".dallas-text {max-height:0; overflow:hidden; transition: max-height 0.3s ease-out;}" +
                    ".dallas-toggle {display: inline-block; background-color: #EFEFEF}" +
                    ".dallas-toggle::after {content: \"Dallas Temperature:\";}" +
                    "#dallas-toggle-box:checked ~ .dallas-text {max-height: 200px; transition: max-height 0.3s ease-in;}" +
                    "#dallas-toggle-box:checked ~ .dallas-toggle::after {content: \"Close\";}" +

                    "#newyork-toggle-box {display:none;}" +
                    ".newyork-text {max-height:0; overflow:hidden; transition: max-height 0.3s ease-out;}" +
                    ".newyork-toggle {display: inline-block; background-color: #EFEFEF}" +
                    ".newyork-toggle::after {content: \"New York City Temperature:\";}" +
                    "#newyork-toggle-box:checked ~ .newyork-text {max-height: 200px; transition: max-height 0.3s ease-in;}" +
                    "#newyork-toggle-box:checked ~ .newyork-toggle::after {content: \"Close\";}" +

                    "#santafe-toggle-box {display:none;}" +
                    ".santafe-text {max-height:0; overflow:hidden; transition: max-height 0.3s ease-out;}" +
                    ".santafe-toggle {display: inline-block; background-color: #EFEFEF}" +
                    ".santafe-toggle::after {content: \"Santa Fe City Temperature:\";}" +
                    "#santafe-toggle-box:checked ~ .santafe-text {max-height: 200px; transition: max-height 0.3s ease-in;}" +
                    "#santafe-toggle-box:checked ~ .santafe-toggle::after {content: \"Close\";}" +

                    "#sacramento-toggle-box {display:none;}" +
                    ".sacramento-text {max-height:0; overflow:hidden; transition: max-height 0.3s ease-out;}" +
                    ".sacramento-toggle {display: inline-block; background-color: #EFEFEF}" +
                    ".sacramento-toggle::after {content: \"Sacramento Temperature:\";}" +
                    "#sacramento-toggle-box:checked ~ .sacramento-text {max-height: 200px; transition: max-height 0.3s ease-in;}" +
                    "#sacramento-toggle-box:checked ~ .sacramento-toggle::after {content: \"Close\";}" +

                    "</style></head>" +
                    " <body style=\"background-color:" + dayTimeBackground("29.17,-81.07") + ";\"> " + mainPicture("29.17,-81.07") +
                    tempHTML("29.17,-81.07", 0) +
                    tempHTML("29.17,-81.07", 1) +
                    tempHTML("29.17,-81.07", 2) +
                    tempHTML("29.17,-81.07", 3) +
                    tempHTML("29.17,-81.07", 4) +
                    tempHTML("29.17,-81.07", 5) +
                    tempHTML("29.17,-81.07", 6) +

                    "<div class=\"container\"> <form action=\"/\" method=\"POST\">" + " <button class=\"button button1\" type=\"submit\">Refresh</button> </form>" +
                    "<input type=\"checkbox\" id=\"dallas-toggle-box\"> <p class=\"dallas-text\">" + tempHTML("32.85,-96.86", 0) + "</p>" + "<label for=\"dallas-toggle-box\" class=\"dallas-toggle\"> </label>" +
                    "<input type=\"checkbox\" id=\"newyork-toggle-box\"> <p class=\"newyork-text\">" + tempHTML("40.78,-73.97", 0) + "</p>" + "<label for=\"newyork-toggle-box\" class=\"newyork-toggle\"> </label>" +
                    "<input type=\"checkbox\" id=\"santafe-toggle-box\"> <p class=\"santafe-text\">" + tempHTML("35.61,-106.1", 0) + "</p>" + "<label for=\"santafe-toggle-box\" class=\"santafe-toggle\"> </label>" +
                    "<input type=\"checkbox\" id=\"sacramento-toggle-box\"> <p class=\"sacramento-text\">" + tempHTML("38.5,-121.49", 0) + "</p>" + "<label for=\"sacramento-toggle-box\" class=\"sacramento-toggle\"> </label>" +
                    "</div> </body> </html>";

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
