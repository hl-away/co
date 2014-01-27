package com.hlaway.co.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import com.hlaway.co.domain.City;
import com.hlaway.co.domain.User;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

/**
 * User: hl-away
 * Date: 20.10.13
 */
public class DatabaseManager {
    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DBHelper helper;
    public DatabaseManager(Context ctx) {
        helper = new DBHelper(ctx);
    }

    public DBHelper getHelper() {
        return helper;
    }

    public User getUser() {
        try {
            return getHelper().getUserDao().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User createOrUpdateUser(User user) {
        try {
            getHelper().getUserDao().createOrUpdate(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getUser();
    }

    public City getCityByName(String cityName) {
        try {
            QueryBuilder<City, Long> queryBuilder = getHelper().getCityDao().queryBuilder();
                queryBuilder.where().eq("name", cityName);
            PreparedQuery<City> preparedQuery = queryBuilder.prepare();
            List<City> cityList = getHelper().getCityDao().query(preparedQuery);
            if(cityList != null && cityList.size() > 0) {
                return cityList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<City> getAllCities() {
        try {
            return getHelper().getCityDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new LinkedList<City>();
    }
}
