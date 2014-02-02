package com.hlaway.co.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.hlaway.co.CoActivity;
import com.hlaway.co.db.DatabaseManager;
import com.hlaway.co.domain.City;
import com.hlaway.co.domain.Country;
import com.hlaway.co.domain.GameCity;
import com.hlaway.co.network.HttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public static final String COUNTRY_NAME = "country_name";
    public static final String COUNTRY_CODE = "country_code";
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

    public static GameCity parseCityFromStr(String str) {
        String[] cityArray = str.split(NetworkUtil.SEPARATOR);
        if(cityArray.length != 4) {
            return null;
        }

        try {
            GameCity city = new GameCity();
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
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(CITY_NAME, city.getName()));
        parameters.add(new BasicNameValuePair(LATITUDE, String.valueOf(city.getLatitude())));
        parameters.add(new BasicNameValuePair(LONGITUDE, String.valueOf(city.getLongitude())));
        parameters.add(new BasicNameValuePair(COUNTRY_NAME, city.getCountry().getName()));
        parameters.add(new BasicNameValuePair(COUNTRY_CODE, city.getCountry().getCode()));
        httpClient.setParameters(parameters);
        httpClient.execute(getSetCityUrl());
    }

    public static City initCity(String cityName) {
        City city = new City(cityName);
        initCityFromGeocoder(city);
        return city;
    }

    private static void initCityFromGeocoder(City city) {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(city.getName(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            city.setLatitude(address.getLatitude());
            city.setLongitude(address.getLongitude());
            city.setCountry(new Country(address.getCountryName(), address.getCountryCode()));
        }
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
