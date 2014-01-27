package com.hlaway.co;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.hlaway.co.db.DatabaseManager;
import com.hlaway.co.domain.User;
import com.hlaway.co.util.NetworkUtil;
import com.hlaway.co.util.StringUtil;
import com.hlaway.co.util.UserUtil;

/**
 * User: hl-away
 * Date: 28.12.13
 */
public class AuthorizationActivity extends MainActivity {
    private EditText loginEdit;
    private EditText passwordEdit;
    private CheckBox savePassword;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager.init(this);

        initLayout();
    }

    private void initLayout() {
        setContentView(R.layout.authorization);
        loginEdit = (EditText) findViewById(R.id.authorizationLogin);
        passwordEdit = (EditText) findViewById(R.id.authorizationPassword);
        savePassword = (CheckBox) findViewById(R.id.authorizationSavePassword);

        user = UserUtil.getUserFromDB();
        if(user != null) {
            if(StringUtil.notEmpty(user.getLogin())) {
                loginEdit.setText(user.getLogin());
            }
            if(StringUtil.notEmpty(user.getPassword())) {
                passwordEdit.setText(user.getPassword());
                savePassword.setChecked(true);
            }
        }
        addListenerToButtons();
    }

    public void initUserToken(String result) {
        if(!NetworkUtil.TRUE.equalsIgnoreCase(result)) {
            if(NetworkUtil.NULL.equalsIgnoreCase(result)) {
                initLayout();
            } else {
                user.setToken(result);
                UserUtil.saveUserInDB(user);
            }
        }
        startGame();
    }

    private void addListenerToButtons() {
        Button setUserNameButton = (Button) findViewById(R.id.authorizationLoginBtn);
        setUserNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = String.valueOf(loginEdit.getText());
                String password = String.valueOf(passwordEdit.getText());
                if(checkLogin(login) && checkPassword(password)) {
                    user = new User(login, password);
                    doLoginOrRegistration();
                }
            }
        });
    }

    private boolean checkLogin(String userName) {
        if(StringUtil.notEmpty(userName)) {
            return true;
        } else {
            loginEdit.setError(getString(R.string.authorization_hint_login_empty));
        }
        return false;
    }

    private boolean checkPassword(String password) {
        if(StringUtil.notEmpty(password)) {
            return true;
        } else {
            passwordEdit.setError(getString(R.string.authorization_hint_password_empty));
        }
        return false;
    }

    private void doLoginOrRegistration() {
        if(isNetworkAvailable()) {
            UserUtil.doLoginOrRegistration(this, getString(R.string.authorization_progress_login_or_registration), user);
        } else {
            openLauncher();
        }
    }

    public void initLoginOrRegistration(String result) {
        if(StringUtil.notEmpty(result)) {
            if(result.startsWith(NetworkUtil.ERROR)) {
                result = result.replace(NetworkUtil.ERROR, "");
                processError(result);
            } else {
                processLogin(result);
            }
        }
    }

    private void processError(String result) {
        if(result.equals(UserUtil.ERROR_SHORT_PASSWORD)) {
            passwordEdit.setError(getString(R.string.authorization_hint_password_short));
        } else if(result.equals(UserUtil.ERROR_EMPTY_PARAMETERS)) {
            checkLogin(user.getLogin());
            checkPassword(user.getPassword());
        } else if(result.equals(UserUtil.ERROR_INCORRECT_PASSWORD)) {
            passwordEdit.setError(getString(R.string.authorization_hint_password_incorrect));
        }
    }

    private void processLogin(String token) {
        user.setToken(token);
        if(!savePassword.isChecked()) {
            user.setPassword("");
        }
        UserUtil.saveUserInDB(user);
        if(!startGame()) {
            openLauncher();
        }
    }

    private boolean startGame() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, StartGameActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.authorization_dialog_exit_title))
                .setMessage(getString(R.string.authorization_dialog_exit_text))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.authorization_dialog_exit_yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                .setNegativeButton(getString(R.string.authorization_dialog_exit_no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
