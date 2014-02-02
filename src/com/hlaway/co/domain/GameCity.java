package com.hlaway.co.domain;

import com.google.android.gms.maps.model.Marker;
import com.hlaway.co.R;

import java.util.HashMap;
import java.util.Map;

/**
 * User: hl-away
 * Date: 30.01.14
 */
public class GameCity extends City {
    private long userID;
    private boolean newCity;
    private Marker marker;

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

    public boolean isNewCity() {
        return newCity;
    }

    public void setNewCity(boolean newCity) {
        this.newCity = newCity;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }

    public Map<String, Object> getViewMap() {
        Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put( COLUMN_NAME, getName());
        int icon = R.drawable.t;
        if(newCity) {
            icon = R.drawable.star;
        }
        parametersMap.put( COLUMN_ICON, icon );
        return parametersMap;
    }

    public static String[] getParametersList() {
        return new String[]{ COLUMN_NAME, COLUMN_ICON };
    }
}
