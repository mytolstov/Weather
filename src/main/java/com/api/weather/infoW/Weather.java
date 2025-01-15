package com.api.weather.infoW;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Weather {
    private String city;

    private double latitude;
    private double longitude;

    private String temperature;
    private String rain;
    private String cloudy;
    private String windSp;
    private double windSpeedNum;
    private String isDay;

    private String image;




    public Weather(String city) {
        this.city = city;
        weather1(city);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getCloudy() {
        return cloudy;
    }

    public void setCloudy(String cloudy) {
        this.cloudy = cloudy;
    }

    public String getWindSp() {
        return windSp;
    }

    public void setWindSp(String windSp) {
        this.windSp = windSp;
    }

    public String getIsDay() {
        return isDay;
    }

    public void setIsDay(String isDay) {
        this.isDay = isDay;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Main
    public void weather1(String city) {

        latitude = 0;
        longitude = 0;
        Map<String,Double> locationMap = new HashMap<>();

        city = city.replace(' ', '+');
        String urlString1 = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en&format=json";
        try {
            //Http Anfrage
            //Создаём соединением с сервером и запрашиваем с него информацию(Методом "GET")
            HttpURLConnection connection = httpAnfrage(urlString1);

            //Bekommen Antwort vom Server
            //записываем полученные данные в StringBuffer response
            StringBuffer response = jsonInhalt(connection);

            //Parsing Json Antwort
            //из JSON в JsonNode для работы с ним
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            JsonNode result = rootNode.path("results");

            //является массивом и содержит хотя бы один элемент
            if (result.isArray() && result.size() > 0) {
                JsonNode location = result.get(0);
                latitude = location.path("latitude").asDouble();
                longitude = location.path("longitude").asDouble();
                System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
                //
                Map<String, String> weatherInfo = weather2(this.latitude, this.longitude);
                weatherInfo.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
                System.out.println();
                System.out.println("-".repeat(30));
                //тут
            } else {
                System.out.println("City not found.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        locationMap.put("latitude",latitude);
        locationMap.put("longitude",longitude);
        zuweisungLocation(locationMap);
        zuweisung(weather2(latitude,longitude));
    }

    public Map<String, String> weather2(double latitude, double longitude) {
        Map<String, String> weatherInfo = new HashMap<>();
        String UrlTemperatur = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,is_day,rain,cloud_cover,wind_speed_10m";

        try {
            //1
            HttpURLConnection connection = httpAnfrage(UrlTemperatur);

            //2
            StringBuffer responce = jsonInhalt(connection);

            //3
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responce.toString());
            JsonNode current = rootNode.path("current");
            
            //температура
            double temperatureNum = current.path("temperature_2m").asDouble();
            temperature = temperatureNum + "°C";
            weatherInfo.put("temperature", temperature);
            
            //дождь
            double rainNum = current.path("rain").asDouble();
            rain = "";
            if (rainNum == 0) {
                rain = "no rain";
            } else if (rainNum > 0 && rainNum <= 0.25) {
                rain = "almost no rain";
            } else if (rainNum > 0.25 && rainNum <= 2.5) {
                rain = "weak rain";
            } else if (rainNum > 2.5 && rainNum <= 8.0) {
                rain = "rain";
            } else if (rainNum > 8.0) {
                rain = "strong rain";
            }
            weatherInfo.put("rain", rain);
            //облачно/солнечно
            double cloudsNum = current.path("cloud_cover").asDouble();
            cloudy = "";
            if (cloudsNum >= 0 && cloudsNum <= 25) {
                cloudy = "sunny";
            } else if (cloudsNum > 25 && cloudsNum <= 65) {
                cloudy = "partly cloudy";
            } else if (cloudsNum > 65 && cloudsNum <= 75) {
                cloudy = "cloudy";
            } else if (cloudsNum > 75) {
                cloudy = "cloudy/foggy";
            }
            weatherInfo.put("cloudy", cloudy);
            //скорость ветра
            windSpeedNum = current.path("wind_speed_10m").asDouble();
            windSp = windSpeedNum + "km/h";
            weatherInfo.put("windSp", windSp);

            //день/ночь
            double dayNight = current.path("is_day").asDouble();
            if(dayNight == 1){
                isDay = "true";
            }else {
                isDay = "false";
            }
            weatherInfo.put("isDay",isDay);

            //устанавливает image
            setImageSrc(isDay);
            weatherInfo.put("ImageScr",image);

            return weatherInfo;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return weatherInfo;
    }

    //внутри
    public HttpURLConnection httpAnfrage(String Url12) throws IOException {
        URL url = new URL(Url12);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }

    public StringBuffer jsonInhalt(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer responce = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            responce.append(inputLine);
        }
        reader.close();
        return responce;
    }

    //надо выполнить
    public void zuweisung(Map<String, String> info) {
        this.temperature = info.get("temperature");
        this.rain = info.get("rain");
        this.cloudy = info.get("cloudy");
        this.windSp = info.get("windSp");
    }

    //надо выполнить
    public void zuweisungLocation(Map<String,Double> location){
        this.latitude = location.get("latitude");
        this.longitude = location.get("longitude");
    }

    public void setImageSrc(String isDay){
        String basic = "https://basmilius.github.io/weather-icons/production/fill/all/";

        String day = basic + "clear-day.svg";  //солнечный день
        String night = basic + "clear-night.svg";  //ясная ночь
        String cloudyDay = basic + "overcast-day.svg";  //пасмурно день
        String cloudyNight = basic + "overcast-night.svg";   //пасмурно ночь
        String rainUrl = basic + "rain.svg";  //дождь
        String partlyCloudyDay = basic + "partly-cloudy-day.svg";  //частично пасмурно день
        String partlyCloudyNight = basic + "partly-cloudy-night.svg";   //частично пасмурно ночь
        String wind = basic + "wind.svg";  //сильный ветер

//        if(isDay.equals("true")){
//            image = day;
//        }else {
//            image = night;
//        }
        
        if(windSpeedNum >= 30){
            image = wind;
        } else if (rain.equals("weak rain") || rain.equals("rain")|| rain.equals("strong rain")) {
            image = rainUrl;
        } else if (isDay.equals("true") && cloudy.equals("sunny") && rain.equals("no rain")) {
            image = day;
        }else if (isDay.equals("false") && cloudy.equals("sunny") && rain.equals("no rain")) {
            image = night;
        } else if (isDay.equals("true") && (cloudy.equals("cloudy") || cloudy.equals("cloudy/foggy")) && (rain.equals("no rain")||rain.equals("almost no rain"))) {
            image = cloudyDay;//пасмурный день
        } else if (isDay.equals("true") && (cloudy.equals("partly cloudy")) && (rain.equals("no rain")||rain.equals("almost no rain"))) {
            image = partlyCloudyDay; //частично пасмурный день
        }else if (isDay.equals("false") && (cloudy.equals("cloudy") || cloudy.equals("cloudy/foggy")) && (rain.equals("no rain")||rain.equals("almost no rain"))) {
            image = cloudyNight;//пасмурный ночь
        } else if (isDay.equals("false") && (cloudy.equals("partly cloudy")) && (rain.equals("no rain")||rain.equals("almost no rain"))) {
            image = partlyCloudyNight; //частично пасмурный ночь
        }


    }
}
