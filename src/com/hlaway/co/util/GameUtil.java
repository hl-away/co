package com.hlaway.co.util;

import android.app.ProgressDialog;
import com.hlaway.co.CoActivity;
import com.hlaway.co.StartGameActivity;
import com.hlaway.co.domain.*;
import com.hlaway.co.network.HttpClient;

/**
 * User: hl-away
 * Date: 03.11.13
 */
public class GameUtil {
    public static final String GAME_ID = "game_id";

    public static String getGameRequestUrl() {
        return NetworkUtil.buildUrl("get_game.php");
    }

    public static String getAddCityToGameUrl() {
        return NetworkUtil.buildUrl("game_add_city.php");
    }

    public static String getOnlineDataUrl() {
        return NetworkUtil.buildUrl("get_online_data.php");
    }

    public static void requestGame(CoActivity coActivity, String message, User user) {
        HttpClient httpClient = new HttpClient();
        ProgressDialog progress = new ProgressDialog(coActivity);
        progress.setMessage(message);
        progress.show();
        httpClient.setProgress(progress);
        httpClient.setCoActivity(coActivity);
        httpClient.setRequestGame(true);
        String url = StringUtil.addParamToURL(getGameRequestUrl(), UserUtil.USER_TOKEN, user.getToken());
        httpClient.execute(url);
    }

    public static Game parseGameFromStr(String str) {
        String[] gameArray = str.split(NetworkUtil.SEPARATOR);
        if(gameArray.length < 1) {
            return null;
        }
        Game game = new Game();
        game.setId(Long.valueOf(gameArray[0]));
        for(int i = 1; i < gameArray.length; i++) {
            String[] valueArray = gameArray[i].split(NetworkUtil.SEPARATOR_DATA);
            if(valueArray[0].equals("u")) {
                User user = new User();
                user.setId(Long.valueOf(valueArray[1]));
                user.setLogin(valueArray[2]);
                user.setScore(Long.valueOf(valueArray[3]));
                game.addUser(user);
            } else if(valueArray[0].equals("c")) {
                GameCity city = new GameCity();
                city.setId(Long.valueOf(valueArray[1]));
                city.setName(valueArray[2]);
                city.setLatitude(Double.valueOf(valueArray[3]));
                city.setLongitude(Double.valueOf(valueArray[4]));
                game.addCity(city);
            }
        }
        return game;
    }

    public static boolean isCityInGame(Game game, City city) {
        return getCityInGamePosition(game, city) != -1;
    }

    public static int getCityInGamePosition(Game game, City city) {
        for( int i = 0; i < game.getCities().size(); i++ ) {
            if( game.getCities().get(i).getName().equalsIgnoreCase(city.getName()) ) {
                return i;
            }
        }
        return -1;
    }

    public static void addCityToGame(City city, Game game, User user) {
        HttpClient httpClient = new HttpClient();
        String url = StringUtil.addParamToURL(getAddCityToGameUrl(), UserUtil.USER_TOKEN, user.getToken());
        url = StringUtil.addParamToURL(url, GameUtil.GAME_ID, game.getId());
        url = StringUtil.addParamToURL(url, CityUtil.CITY_ID, city.getServerID());
        httpClient.execute(url);
    }

    public static void requestOnlineData(StartGameActivity startGameActivity, String message) {
        HttpClient httpClient = new HttpClient();
        ProgressDialog progress = new ProgressDialog(startGameActivity);
        progress.setMessage(message);
        progress.show();
        httpClient.setProgress(progress);
        httpClient.setStartGameActivity(startGameActivity);
        httpClient.setRequestOnlineData(true);
        httpClient.execute(getOnlineDataUrl());
    }

    public static OnlineData parseOnlineData(String result) {
        if(StringUtil.notEmpty(result)) {
            String[] strings = result.split(NetworkUtil.SEPARATOR);
            if(strings.length == 2) {
                return new OnlineData(StringUtil.norm(strings[0]), StringUtil.norm(strings[1]));
            }
        }
        return null;
    }
}
