package com.hlaway.co.network;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import com.hlaway.co.AuthorizationActivity;
import com.hlaway.co.CoActivity;
import com.hlaway.co.StartGameActivity;
import com.hlaway.co.util.NetworkUtil;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * User: hl-away
 * Date: 22.11.13
 */
public class HttpClient extends AsyncTask<String, Void, String> {
    private TextView textView;
    private ProgressDialog progress;
    private CoActivity coActivity;
    private AuthorizationActivity authorizationActivity;
    private StartGameActivity startGameActivity;
    private List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    private boolean requestCity = false;
    private boolean requestUserToken = false;
    private boolean requestUserData = false;
    private boolean requestGame = false;
    private boolean doLoginOrRegistration = false;
    private boolean requestOnlineData = false;

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }

    public void setCoActivity(CoActivity coActivity) {
        this.coActivity = coActivity;
    }

    public void setAuthorizationActivity(AuthorizationActivity authorizationActivity) {
        this.authorizationActivity = authorizationActivity;
    }

    public void setStartGameActivity(StartGameActivity startGameActivity) {
        this.startGameActivity = startGameActivity;
    }

    public void setParameters(List<NameValuePair> parameters) {
        this.parameters = parameters;
    }

    public void setRequestCity(boolean requestCity) {
        this.requestCity = requestCity;
    }

    public void setRequestUserToken(boolean requestUserToken) {
        this.requestUserToken = requestUserToken;
    }

    public void setRequestUserData(boolean requestUserData) {
        this.requestUserData = requestUserData;
    }

    public void setRequestGame(boolean requestGame) {
        this.requestGame = requestGame;
    }

    public void setDoLoginOrRegistration(boolean doLoginOrRegistration) {
        this.doLoginOrRegistration = doLoginOrRegistration;
    }

    public void setRequestOnlineData(boolean requestOnlineData) {
        this.requestOnlineData = requestOnlineData;
    }

    protected String doInBackground(String... urls) {
        return NetworkUtil.getUrl(urls[0], parameters);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute( result );
        if(progress != null) {
            progress.dismiss();
        }
        if(textView != null) {
            textView.setText(result);
        }

        if(coActivity != null) {
            if(requestCity) {
                coActivity.setCityFromServer(result);
            } else if(requestGame) {
                coActivity.initGame(result);
            }
        }

        if(authorizationActivity != null) {
            if(requestUserToken) {
                authorizationActivity.initUserToken(result);
            } else if(doLoginOrRegistration) {
                authorizationActivity.initLoginOrRegistration(result);
            }
        }

        if(startGameActivity != null) {
            if(requestUserData) {
                startGameActivity.initUserData(result);
            } else if(requestOnlineData) {
                startGameActivity.initOnlineData(result);
            }
        }
    }
}
