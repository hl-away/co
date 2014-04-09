package com.hlaway.co.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.List;

/**
 * User: hl-away
 * Date: 18.11.13
 */
public class NetworkUtil {
    public static final String SITE_URL = "http://hlaway.0fees.us/";
    public static final String NULL = "NULL";
    public static final String TRUE = "TRUE";
    public static final String ERROR = "ERROR=";
    public static final String SEPARATOR = "\\|";
    public static final String SEPARATOR_DATA = "\\&";
    public static final String SEPARATOR_DATA_VALUE = "\\:";
    public static final String SEPARATOR_DATA_VALUE_FIELD = "\\#";
    public static final String SEPARATOR_DATA_VALUE_FIELD_VALUE = "\\=";

    public static String buildUrl(String pageName) {
        return SITE_URL + pageName;
    }

    public static String getUrl(String url, List<NameValuePair> parameters) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(URLDecoder.decode(url.replaceAll(" ","_")));
        post.setHeader("Accept", "text/html");
        post.setHeader("Accept-Encoding", "deflate");
        post.setHeader("Accept-Charset", "UTF-8");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        try {
            if(parameters != null) {
                post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            }

            HttpResponse response = httpClient.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (ClientProtocolException ignored) {
        } catch (IOException ignored) {
        }
        return "";
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
