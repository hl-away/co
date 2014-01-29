package com.hlaway.co.domain;

/**
 * User: hl-away
 * Date: 30.01.14
 */
public class GameCity extends City {
    private long userID;
    private boolean foundCity;

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
}
