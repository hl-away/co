package com.hlaway.co;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hlaway.co.db.DatabaseManager;
import com.hlaway.co.domain.City;
import com.hlaway.co.domain.Game;
import com.hlaway.co.domain.GameCity;
import com.hlaway.co.domain.User;
import com.hlaway.co.util.*;

import java.util.ArrayList;
import java.util.Map;

public class CoActivity extends MainActivity {
    private GoogleMap map;
    private EditText cityNameEdit;
    private Button addCityButton;
    private Game game;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        DatabaseManager.init(this);
        CityUtil.setCtx(this);

/*
        *//*if(user != null) {
            UserUtil.requestUserToken(this, getString(R.string.hint_connect_to_server), user);
        } else {
        }*/
        initUser(savedInstanceState);
        GameUtil.requestGame(this, getString(R.string.hint_connect_to_game), user);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        cityNameEdit = (EditText) findViewById(R.id.cityName);
        addCityButton = (Button) findViewById(R.id.addMap);

        addListenerOnButton();
    }

    private void initUser(Bundle savedInstanceState) {
        if(user == null) {
            if(savedInstanceState != null) {
                user = (User) savedInstanceState.get("user");
            } else {
                user = UserUtil.getUserFromDB();
                if(user == null) {
                    user = UserUtil.createNewUser();
                }
            }
        }
    }

    public void initGame(String result) {
        if( StringUtil.notEmpty(result) ) {
            game = GameUtil.parseGameFromStr(result);
            showGameCities();
            showUsers();
        }
        //UserUtil.requestUsersCount(this, getString(R.string.hint_load_users_count), (TextView) findViewById(R.id.usersCount));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable("game", game);
    }

    public void addListenerOnButton() {
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = String.valueOf(CoActivity.this.cityNameEdit.getText());
                if (cityName != null && !cityName.isEmpty()) {
                    City city = CityUtil.getCityFromDB(cityName);
                    if (CityUtil.isEmpty(city)) {
                        getCityFromServer(cityName);
                    } else {
                        addCityToGame((GameCity) city);
                    }
                } else {
                    printMessage(getString(R.string.hint_write_city_name));
                }
            }
        });
    }

    private void getCityFromServer(String cityName) {
        CityUtil.getCityFromServer(cityName, this);
    }

    private void showGameCities() {
        for(int i = 0; i < game.getCities().size(); i++) {
            showCity(i);
        }
    }

    private void showLastCity() {
        showCity(game.getCities().size() - 1);
    }

    private void showCity(int currentCityId) {
        City currentCity = game.getCities().get(currentCityId);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(currentCity.getPosition())
                .title(currentCity.getName());

        if(currentCityId > 0) {
            City previousCity = game.getCities().get(currentCityId - 1);
            map.addPolyline(new PolylineOptions()
                    .add(currentCity.getPosition(), previousCity.getPosition())
                    .width(2)
                    .color(Color.argb(255, 0, 0, 255)));
            /*if(alpha + alphaStep < 255) {
                alpha += 15;
            }*/
            markerOptions.snippet(CityUtil.getCitySnippet(currentCity, previousCity));
        }
        Marker marker = map.addMarker(markerOptions);
        //marker.setAlpha(0.8f);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
        marker.showInfoWindow();

        CameraUpdate center = CameraUpdateFactory.newLatLng(currentCity.getPosition());
        map.moveCamera(center);
        cityNameEdit.setText(StringUtil.getLastLetter(currentCity.getName()).toUpperCase());

        showCities();
    }

    public void setCityFromServer(String cityStr) {
        GameCity gameCity = CityUtil.parseCityFromStr(cityStr);
        if (gameCity != null) {
            if(gameCity.getLatitude() == 0 && gameCity.getLongitude() == 0) {
                City city = CityUtil.initCity(gameCity.getName());
                CityUtil.saveCityInServer(city);
                CityUtil.saveCityInDB(city);
                gameCity.setLatitude(city.getLatitude());
                gameCity.setLongitude(city.getLongitude());
                gameCity.setFoundCity(true);
            }
            addCityToGame(gameCity);
        } else {
            String cityName = StringUtil.normCityName(String.valueOf(cityNameEdit.getText()));
            printMessage(String.format(getString(R.string.hint_city_not_exist), cityName));
        }
    }

    private boolean addCityToGame(GameCity city) {
        if( GameUtil.isCityInGame(game, city) ) {
            printMessage(getString(R.string.hint_already_called, city.getName()));
        } else {
            /*City lastCity = game.getLastCity();
            if(lastCity != null) {
                user.setScore(user.getScore() + CityUtil.getDistance(city, game.getLastCity()));
                showUsers();
            }*/
            game.addCity(city);
            showLastCity();
            printMessage(getString(R.string.hint_city_added, city.getName()));
            GameUtil.addCityToGame(city, game, user);
            return true;
        }
        return false;
    }

    private void showUsers() {
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (User user: game.getUsers()) {
            data.add(user.getViewMap());
        }
        int[] ids = { R.id.userName, R.id.userScore };
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_user_item, User.getParametersList(), ids);

        ListView lv = (ListView) findViewById(R.id.gameUsers);
        lv.setAdapter( adapter );
    }

    private void showCities() {
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (City city: game.getCities()) {
            data.add(city.getViewMap());
        }
        int[] ids = { R.id.cityName };
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_city_item, City.getParametersList(), ids);

        ListView lv = (ListView) findViewById(R.id.gameCities);
        lv.setAdapter(adapter);
    }
}
