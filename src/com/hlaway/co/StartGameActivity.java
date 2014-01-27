package com.hlaway.co;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.hlaway.co.domain.OnlineData;
import com.hlaway.co.domain.User;
import com.hlaway.co.util.GameUtil;
import com.hlaway.co.util.NetworkUtil;
import com.hlaway.co.util.UserUtil;

/**
 * User: hl-away
 * Date: 28.12.13
 */
public class StartGameActivity extends MainActivity {
    private Button startGameButton;
    private Button openStarsButton;
    private Button openAccountButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);

        user = UserUtil.getUserFromDB();
        UserUtil.requestUserData(this, getString(R.string.start_game_progress_request_user_data), user.getToken());
        GameUtil.requestOnlineData(this, getString(R.string.start_game_progress_request_online_data));

        TextView helloText = (TextView) findViewById(R.id.startGameText);
        helloText.setText(String.format(getString(R.string.start_game_text), user.getLogin()));

        startGameButton = (Button) findViewById(R.id.startGameButton);
        openStarsButton = (Button) findViewById(R.id.openStarsButton);
        openAccountButton = (Button) findViewById(R.id.openAccountButton);
        addListenerToButtons();
    }

    public void initUserData(String result) {
        UserUtil.parseStrToUserDate(user, result);
        displayUserData();
    }

    private void displayUserData() {
        if(UserUtil.isNotEmpty(user)) {
            //((TextView) findViewById(R.id.currentUserName)).setText(user.getLogin());
            //((TextView) findViewById(R.id.currentUserScore)).setText(String.valueOf(user.getScore()));
        }
    }

    public void addListenerToButtons() {
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playGame()) {
                    openLauncher();
                }
            }
        });
    }

    private boolean playGame() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, CoActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void initOnlineData(String result) {
        OnlineData onlineData = GameUtil.parseOnlineData(result);
        showOnlineData(onlineData);
    }

    private void showOnlineData(OnlineData onlineData) {
        ((TextView) findViewById(R.id.gamesOnlineCount)).setText(onlineData.getGamesCount());
        ((TextView) findViewById(R.id.usersOnlineCount)).setText(onlineData.getUsersCount());
    }
}
