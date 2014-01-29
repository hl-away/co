package com.hlaway.co.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: hl-away
 * Date: 27.10.13
 */
public class Game implements Parcelable {
    private long id;
    private ArrayList<User> users;
    private ArrayList<GameCity> cities;
    private int newCitiesCount;

    public Game() {
        users = new ArrayList<User>();
        cities = new ArrayList<GameCity>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public List<GameCity> getCities() {
        return cities;
    }

    public void setCities(ArrayList<GameCity> cities) {
        this.cities = cities;
    }

    public void addCity(GameCity city) {
        cities.add(city);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public City getLastCity() {
        if(cities.size() > 0) {
            return cities.get(cities.size() - 1);
        }
        return null;
    }

    public int getNewCitiesCount() {
        return newCitiesCount;
    }

    public void setNewCitiesCount(int newCitiesCount) {
        this.newCitiesCount = newCitiesCount;
    }

    public void incrementNewCitiesCount() {
        newCitiesCount++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        /*parcel.writeString(color);
        parcel.writeString(number);*/
    }
}
