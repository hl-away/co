package com.hlaway.co;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.hlaway.co.util.NetworkUtil;

/**
 * User: hl-away
 * Date: 23.12.13
 */
public class LauncherActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        if(startAuthorization()) {
            finish();
        }

        addListenerToButtons();
    }

    public void addListenerToButtons() {
        Button launcherButton = (Button) findViewById(R.id.launcherButton);
        launcherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameIfCan();
            }
        });
    }

    private void startGameIfCan() {
        if(startAuthorization()) {
            finish();
        } else {
            printMessage(getString(R.string.launcher_hint_internet_connection));
        }
    }

    private boolean startAuthorization() {
        if(NetworkUtil.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
