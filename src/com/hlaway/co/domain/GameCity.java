package com.hlaway.co.domain;

import com.hlaway.co.R;

import java.util.Map;

/**
 * User: hl-away
 * Date: 30.01.14
 */
public class GameCity extends City {
    private long userID;
    private boolean foundCity;

    public static final String COLUMN_ICON = "icon";

    public GameCity() {
    }

    public GameCity(City city) {
        setName(city.getName());
        setLatitude(city.getLatitude());
        setLongitude(city.getLongitude());
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public boolean isFoundCity() {
        return foundCity;
    }

    public void setFoundCity(boolean foundCity) {
        this.foundCity = foundCity;
    }

    public Map<String, Object> getViewMap() {
        Map<String, Object> parametersMap = super.getViewMap();
        int icon = R.drawable.t;
        if(foundCity) {
            icon = R.drawable.star;
        }
        parametersMap.put( COLUMN_ICON, icon );
        return parametersMap;
    }

    public static String[] getParametersList() {
        return new String[]{ COLUMN_NAME, COLUMN_ICON };
    }
}
