package com.hlaway.co.domain;

/**
 * User: hl-away
 * Date: 26.01.14
 */
public class OnlineData {
    private String gamesCount;
    private String usersCount;

    public OnlineData(String gamesCount, String usersCount) {
        this.gamesCount = gamesCount;
        this.usersCount = usersCount;
    }

    public String getGamesCount() {
        return gamesCount;
    }

    public String getUsersCount() {
        return usersCount;
    }
}
