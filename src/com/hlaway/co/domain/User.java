package com.hlaway.co.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;

/**
 * User: hl-away
 * Date: 27.10.13
 */
@DatabaseTable
public class User {
    @DatabaseField(id = true)
    private long id;
    @DatabaseField()
    private String login;
    @DatabaseField()
    private String password;
    @DatabaseField()
    private String token;
    @DatabaseField()
    private long score = 0;

    private long gamesCount = 0;

    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_SCORE = "score";

    public User() {
        login = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User(String login) {
        this.login = login;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getGamesCount() {
        return gamesCount;
    }

    public void setGamesCount(long gamesCount) {
        this.gamesCount = gamesCount;
    }

    public Map<String, Object> getViewMap() {
        Map<String, Object> parametersMap = new HashMap<String, Object>();
        parametersMap.put( COLUMN_LOGIN, login);
        parametersMap.put( COLUMN_SCORE, score);
        return parametersMap;
    }

    public static String[] getParametersList() {
        return new String[]{COLUMN_LOGIN, COLUMN_SCORE };
    }
}
