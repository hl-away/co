package com.hlaway.co;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.hlaway.co.util.NetworkUtil;

/**
 * User: hl-away
 * Date: 28.12.13
 */
public class MainActivity extends FragmentActivity {
    protected boolean isNetworkAvailable() {
        return NetworkUtil.isNetworkAvailable(this);
    }

    protected void printMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void openLauncher() {
        Intent intent = new Intent(this, LauncherActivity.class);
        startActivity(intent);
        printMessage(getString(R.string.launcher_hint_internet_connection));
        finish();
    }
}
