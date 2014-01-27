package com.hlaway.co.util;

import android.app.ProgressDialog;
import android.widget.TextView;
import com.hlaway.co.AuthorizationActivity;
import com.hlaway.co.CoActivity;
import com.hlaway.co.StartGameActivity;
import com.hlaway.co.db.DatabaseManager;
import com.hlaway.co.domain.User;
import com.hlaway.co.network.HttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: hl-away
 * Date: 03.11.13
 */
public class UserUtil {
    public static final String USER_LOGIN = "user_login";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_TOKEN = "user_token";

    public static final String ERROR_SHORT_PASSWORD = "SHORT_PASSWORD";
    public static final String ERROR_EMPTY_PARAMETERS = "EMPTY_PARAMETERS";
    public static final String ERROR_INCORRECT_PASSWORD = "INCORRECT_PASSWORD";

    public static User createNewUser() {
        User user = new User("Guest " + new Random().nextInt(Integer.MAX_VALUE));
        saveUserInDB(user);
        return user;
    }

    public static User getUserFromDB() {
        return DatabaseManager.getInstance().getUser();
    }

    public static String getUserLoginOrRegistrationUrl() {
        return NetworkUtil.buildUrl("user_login_or_registration.php");
    }

    public static String getUsersCountUrl() {
        return NetworkUtil.buildUrl("users_online_count.php");
    }

    public static String getUserTokenUrl() {
        return NetworkUtil.buildUrl("get_user_token.php");
    }

    public static String getUserDataUrl() {
        return NetworkUtil.buildUrl("get_user_data.php");
    }

    public static boolean saveUserInDB(User user) {
        if (isEmpty(user)) return false;

        user = DatabaseManager.getInstance().createOrUpdateUser(user);
        return !isEmpty(user);
    }

    public static boolean isEmpty(User user) {
        return user == null;
    }

    public static boolean isNotEmpty(User user) {
        return !isEmpty(user);
    }

    public static void doLoginOrRegistration(AuthorizationActivity authorizationActivity, String message, User dummyUser) {
        HttpClient httpClient = new HttpClient();
        ProgressDialog progress = new ProgressDialog(authorizationActivity);
        progress.setMessage(message);
        progress.show();
        httpClient.setProgress(progress);
        httpClient.setAuthorizationActivity(authorizationActivity);
        httpClient.setDoLoginOrRegistration(true);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(USER_LOGIN, dummyUser.getLogin()));
        parameters.add(new BasicNameValuePair(USER_PASSWORD, dummyUser.getPassword()));
        httpClient.setParameters(parameters);
        httpClient.execute(getUserLoginOrRegistrationUrl());
    }

    public static void requestUserToken(AuthorizationActivity authorizationActivity, String message, User user) {
        HttpClient httpClient = new HttpClient();
        ProgressDialog progress = new ProgressDialog(authorizationActivity);
        progress.setMessage(message);
        progress.show();
        httpClient.setProgress(progress);
        httpClient.setAuthorizationActivity(authorizationActivity);
        httpClient.setRequestUserToken(true);
        String url = getUserTokenUrl();
        if(StringUtil.isEmpty(user.getToken())) {
            url = StringUtil.addParamToURL(url, USER_LOGIN, user.getLogin());
        } else {
            url = StringUtil.addParamToURL(url, USER_TOKEN, user.getToken());
        }
        httpClient.execute(url);
    }

    public static void requestUsersCount(CoActivity coActivity, String message, TextView userCountView) {
        HttpClient httpClient = new HttpClient();
        ProgressDialog progress = new ProgressDialog(coActivity);
        progress.setMessage(message);
        progress.show();
        httpClient.setProgress(progress);
        httpClient.setTextView(userCountView);
        httpClient.execute(UserUtil.getUsersCountUrl());
    }

    public static void requestUserData(StartGameActivity startGameActivity, String message, String userToken) {
        HttpClient httpClient = new HttpClient();
        ProgressDialog progress = new ProgressDialog(startGameActivity);
        progress.setMessage(message);
        progress.show();
        httpClient.setProgress(progress);
        httpClient.setStartGameActivity(startGameActivity);
        httpClient.setRequestUserData(true);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair(USER_TOKEN, userToken));
        httpClient.setParameters(parameters);
        httpClient.execute(getUserDataUrl());
    }

    public static void parseStrToUserDate(User user, String result) {
        String[] strings = result.split(NetworkUtil.SEPARATOR);
        String score = strings[0];
        String gamesCount = strings[1];
        user.setScore(StringUtil.notEmpty(score)? Long.valueOf(score): 0);
        user.setGamesCount(StringUtil.notEmpty(gamesCount)? Long.valueOf(gamesCount): 0);
    }
}
