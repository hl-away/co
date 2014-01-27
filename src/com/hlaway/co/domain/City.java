package com.hlaway.co.domain;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;

/**
 * User: hl-away
 * Date: 19.10.13
 */
@DatabaseTable
public class City {
    @DatabaseField(generatedId=true)
    private long id;
    @DatabaseField(index = true)
    private String name;
    @DatabaseField()
    private double latitude = 0;
    @DatabaseField()
    private double longitude = 0;
    @DatabaseField()
    private double serverID = 0;

    public static final String COLUMN_NAME = "name";

    public City() {
        name = "";
    }

    public City(String name) {
        this.name = name;
    }

    public City(String name, LatLng latLng) {
        this.name = name;
        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
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

    public double getServerID() {
        return serverID;
    }

    public void setServerID(double serverID) {
        this.serverID = serverID;
    }

    public Map<String, Object> getViewMap() {
        Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put( COLUMN_NAME, name);
        return parametersMap;
    }

    public static String[] getParametersList() {
        return new String[]{ COLUMN_NAME };
    }
}
