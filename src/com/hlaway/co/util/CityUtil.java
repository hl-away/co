package com.hlaway.co.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.hlaway.co.CoActivity;
import com.hlaway.co.db.DatabaseManager;
import com.hlaway.co.domain.City;
import com.hlaway.co.network.HttpClient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * User: hl-away
 * Date: 19.10.13
 */
public class CityUtil {
    public static final String CITY_NAME = "city_name";
    public static final String CITY_ID = "city_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    private static Context ctx;

    public static void setCtx(Context ctx) {
        CityUtil.ctx = ctx;
    }

    public static String getCitySnippet(City currentCity, City lastCity) {
        if(lastCity == null)
            return "";

        return lastCity.getName() + " > ~" + getDistance(currentCity, lastCity) + "км > " + currentCity.getName();
    }

    public static int getDistance(City c1, City c2) {
        Location locationA = new Location("");
        locationA.setLatitude(c1.getPosition().latitude);
        locationA.setLongitude(c1.getPosition().longitude);
        Location locationB = new Location("");
        locationB.setLatitude(c2.getPosition().latitude);
        locationB.setLongitude(c2.getPosition().longitude);
        return (int) locationA.distanceTo(locationB)/1000;
    }

    public static City getCity(String cityName) {
        cityName = StringUtil.normCityName(cityName);
        City city = getCityFromDB(cityName);
        if(isEmpty(city)) {
            //getCityFromServer(cityName);
            if (city == null) {
                return null;
            }

            if(city.getLatitude() == 0 && city.getLongitude() == 0) {
                city = initCity(cityName);
                saveCityInServer(city);
            }

            saveCityInDB(city);
        }
        return city;
    }

    public static City getCityFromDB(String cityName) {
        return DatabaseManager.getInstance().getCityByName(cityName);
    }

    public static void getCityFromServer(String cityName, CoActivity coActivity) {
        HttpClient httpClient = new HttpClient();
        ProgressDialog progress = new ProgressDialog(ctx);
        progress.setMessage("Поиск города " + cityName + " в базе сервера");
        progress.show();
        httpClient.setProgress(progress);
        httpClient.setRequestCity(true);
        httpClient.setCoActivity(coActivity);
        String params = "?" + CITY_NAME + "=" + cityName;
        httpClient.execute(getGetCityUrl() + params);
    }

    public static City parseCityFromStr(String str) {
        String[] cityArray = str.split(NetworkUtil.SEPARATOR);
        if(cityArray.length != 4) {
            return null;
        }

        try {
            City city = new City();
            city.setServerID(Long.valueOf(cityArray[0]));
            city.setName(cityArray[1]);
            city.setLatitude(Double.valueOf(cityArray[2]));
            city.setLongitude(Double.valueOf(cityArray[3]));
            return city;
        } catch (Exception ignored) { }

        return null;
    }

    public static boolean saveCityInDB(City city) {
        if (isEmpty(city)) return false;

        try {
            DatabaseManager.getInstance().getHelper().getCityDao().createIfNotExists(city);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void saveCityInServer(City city) {
        HttpClient httpClient = new HttpClient();
        StringBuilder params = new StringBuilder();
        params.append("?").append(CITY_NAME).append("=").append(city.getName());
        params.append("&").append(LATITUDE).append("=").append(city.getLatitude());
        params.append("&").append(LONGITUDE).append("=").append(city.getLongitude());
        httpClient.execute(getSetCityUrl() + params.toString());
    }

    public static City initCity(String cityName) {
        return new City(cityName, getCityPositionFromGeocoder(cityName));
    }

    private static LatLng getCityPositionFromGeocoder(String cityName) {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(cityName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double latitude = 0;
        double longitude = 0;
        if(addresses != null && addresses.size() > 0) {
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
        }
        return new LatLng(latitude, longitude);
    }

    public static City getRandomCity(Context context) {
        List<City> cities = new DatabaseManager(context).getAllCities();
        if(cities.size() > 0) {
            int index = new Random().nextInt(cities.size());
            return cities.get(index);
        }
        return null;
    }

    private static String getGetCityUrl() {
        return NetworkUtil.buildUrl("get_city.php");
    }

    private static String getSetCityUrl() {
        return NetworkUtil.buildUrl("set_city.php");
    }

    public static boolean isEmpty(City city) {
        return city == null || (city.getLatitude() == 0 && city.getLongitude() == 0);
    }

    public static boolean isNotEmpty(City city) {
        return !isEmpty(city);
    }
}
