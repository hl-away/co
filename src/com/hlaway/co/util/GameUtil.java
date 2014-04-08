package com.hlaway.co.util;

import android.app.ProgressDialog;
import com.hlaway.co.CoActivity;
import com.hlaway.co.R;
import com.hlaway.co.StartGameActivity;
import com.hlaway.co.domain.*;
import com.hlaway.co.network.HttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * User: hl-away
 * Date: 03.11.13
 */
public class GameUtil {
    public static final String GAME_ID = "game_id";
    public static final String LAST_STEP = "last_step";

    public static String getGameRequestUrl() {
        return NetworkUtil.buildUrl("get_game.php");
    }

    public static String getAddCityToGameUrl() {
        return NetworkUtil.buildUrl("game_add_city.php");
    }

    public static String getOnlineDataUrl() {
        return NetworkUtil.buildUrl("get_online_data.php");
    }

    public static String getGameStepsUrl() {
        return NetworkUtil.buildUrl("get_game_steps.php");
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
        Game game = new Game();
        game.setId(Long.valueOf(str));
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

    public static void addCityToGame(GameCity city, Game game, User user) {
        HttpClient httpClient = new HttpClient();
        httpClient.addParameter(UserUtil.USER_TOKEN, user.getToken());
        httpClient.addParameter(GameUtil.GAME_ID, game.getId());
        httpClient.addParameter(CityUtil.CITY_ID, city.getServerID());
        httpClient.addParameter(CityUtil.NEW_CITY, city.isNewCity());
        httpClient.execute(getAddCityToGameUrl());
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

    public static void requestGameSteps(CoActivity coActivity, Game game, User user) {
        HttpClient httpClient = new HttpClient();
        httpClient.setCoActivity(coActivity);
        httpClient.setRequestGameSteps(true);
        httpClient.addParameter(UserUtil.USER_TOKEN, user.getToken());
        httpClient.addParameter(GAME_ID, game.getId());
        httpClient.addParameter(LAST_STEP, game.getLastStep());
        httpClient.execute(getGameStepsUrl());
    }

    public static List<GameStep> parseSteps(String result) {
        List<GameStep> gameSteps = new ArrayList<GameStep>();
        if(StringUtil.notEmpty(result)) {
            String[] steps = result.split(NetworkUtil.SEPARATOR);
            for(String step: steps) {
                GameStep gameStep = new GameStep();
                String[] data_mass = step.split(NetworkUtil.SEPARATOR_DATA);
                for(String data: data_mass) {
                    String[] values = data.split(NetworkUtil.SEPARATOR_DATA_VALUE);
                    String key = values[0];
                    String value = values[1];
                    if("s".equals(key)) {
                        gameStep.setStep(Long.valueOf(value));
                    } else if("t".equals(key)) {
                        gameStep.setType(value);
                    } else if("u".equals(key)) {
                        gameStep.setUserId(Long.valueOf(value));
                    } else if("o".equals(key)) {
                        gameStep.setValue(value);
                    }
                }
                gameSteps.add(gameStep);
            }
        }
        return gameSteps;
    }

    public static void addStepsToGame(CoActivity coActivity, List<GameStep> gameSteps, Game game) {
        for(GameStep gameStep: gameSteps) {
            addStepToGame(coActivity, gameStep, game);
        }
    }

    private static void addStepToGame(CoActivity coActivity, GameStep gameStep, Game game) {
        String message = "";
        String type = gameStep.getType();
        String value = gameStep.getValue();
        User user = game.getUser(gameStep.getUserId());
        if (GameStep.TYPE_ADD_CITY.equals(type) || GameStep.TYPE_ADD_NEW_CITY.equals(type)) {
            GameCity gameCity = initGameCity(value);
            if (GameStep.TYPE_ADD_NEW_CITY.equals(type)) {
                gameCity.setNewCity(true);
            }
            game.addCity(gameCity);
            coActivity.showLastCity();
            if(user != null) {
                message = coActivity.getString(R.string.game_hint_add_city, user.getLogin(), gameCity.getName());
            }
        } else if(GameStep.TYPE_CONNECT_USER.equals(type)) {
            user = initUser(value);
            game.addUser(user);
            if(user != null) {
                message = coActivity.getString(R.string.game_hint_connect_user, user.getLogin());
            }
            coActivity.showUsers();
        }
        game.setLastStep(gameStep.getStep());
        if(StringUtil.notEmpty(message)) {
            coActivity.printShortMessage(message);
        }
    }

    private static GameCity initGameCity(String objectData) {
        GameCity gameCity = new GameCity();
        String[] fields = objectData.split(NetworkUtil.SEPARATOR_DATA_VALUE_FIELD);
        for(String field: fields) {
            String[] values = field.split(NetworkUtil.SEPARATOR_DATA_VALUE_FIELD_VALUE);
            String key = values[0];
            String value = values[1];
            if("i".equals(key)) {
                gameCity.setId(Long.valueOf(value));
            } else if("n".equals(key)) {
                gameCity.setName(value);
            } else if("la".equals(key)) {
                gameCity.setLatitude(Double.valueOf(value));
            } else if("lo".equals(key)) {
                gameCity.setLongitude(Double.valueOf(value));
            }
        }
        return gameCity;
    }

    private static User initUser(String objectData) {
        User user = new User();
        String[] fields = objectData.split(NetworkUtil.SEPARATOR_DATA_VALUE_FIELD);
        for(String field: fields) {
            String[] values = field.split(NetworkUtil.SEPARATOR_DATA_VALUE_FIELD_VALUE);
            String key = values[0];
            String value = values[1];
            if("i".equals(key)) {
                user.setId(Long.valueOf(value));
            } else if("l".equals(key)) {
                user.setLogin(value);
            } else if("s".equals(key)) {
                user.setScore(Long.valueOf(value));
            }
        }
        return user;
    }
}
