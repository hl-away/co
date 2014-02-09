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
import com.hlaway.co.util.CityUtil;
import com.hlaway.co.util.GameUtil;
import com.hlaway.co.util.StringUtil;
import com.hlaway.co.util.UserUtil;

import java.util.ArrayList;
import java.util.Map;

public class CoActivity extends MainActivity {
    private GoogleMap map;
    private TextView gameCitiesTitle;
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

        initUser(savedInstanceState);
        GameUtil.requestGame(this, getString(R.string.hint_connect_to_game), user);

        initLayout();
        addListenerOnButton();
    }

    private void initLayout() {
        cityNameEdit = (EditText) findViewById(R.id.cityName);
        gameCitiesTitle = (TextView) findViewById(R.id.gameCitiesTitle);
        addCityButton = (Button) findViewById(R.id.addMap);
        initMap();
    }

    private void initMap() {
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.animateCamera( CameraUpdateFactory.zoomTo( 3.0f ) );
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
            showAllGameCities();
            showUsers();
            GameUtil.requestGameSteps(this, game, user);
        }
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
                        addCityToGame(new GameCity(city));
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

    public void initGameSteps(String result) {
        GameUtil.addStepsToGame(this, GameUtil.parseSteps(result), game);
        if(!game.isCurrentStep()) {
            GameUtil.requestGameSteps(this, game, user);
        }
    }

    private void showAllGameCities() {
        for(int i = 0; i < game.getCities().size()-1; i++) {
            showCity(i, false);
        }
        showLastCity();
    }

    public void showLastCity() {
        showCity(game.getCities().size() - 1);
    }

    private void showCity(int cityId) {
        showCity(cityId, true);
    }

    private void showCity(int cityId, boolean showMarker) {
        GameCity currentCity = game.getCities().get(cityId);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(currentCity.getPosition())
                .title(currentCity.getName());

        if(cityId > 0) {
            City previousCity = game.getCities().get(cityId - 1);
            map.addPolyline(new PolylineOptions()
                    .add(currentCity.getPosition(), previousCity.getPosition())
                    .width(2)
                    .color(Color.argb(100, 0, 0, 255)));
            /*if(alpha + alphaStep < 255) {
                alpha += 15;
            }*/
            markerOptions.snippet(CityUtil.getCitySnippet(currentCity, previousCity));
        }
        Marker marker = map.addMarker(markerOptions);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
        currentCity.setMarker(marker);
        if(showMarker) {
            showCityOnMap(currentCity);
        }
        //marker.setAlpha(0.8f);

        cityNameEdit.setText(StringUtil.getLastLetter(currentCity.getName()).toUpperCase());
        gameCitiesTitle.setText(getString(R.string.current_game_cities, game.getCities().size()));
        showCitiesTable();
    }

    private void showCityOnMap(GameCity city) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(city.getPosition());
        map.moveCamera(center);
        city.getMarker().showInfoWindow();
    }

    public void initCityFromServer(String cityStr) {
        GameCity gameCity = CityUtil.parseCityFromStr(cityStr);
        if (gameCity != null) {
            if(gameCity.getLatitude() == 0 && gameCity.getLongitude() == 0) {
                City city = CityUtil.initCity(gameCity.getName());
                CityUtil.saveCityInServer(city);
                CityUtil.saveCityInDB(city);
                gameCity.setLatitude(city.getLatitude());
                gameCity.setLongitude(city.getLongitude());
                gameCity.setNewCity(true);
            }
            addCityToGame(gameCity);
        } else {
            String cityName = StringUtil.normCityName(String.valueOf(cityNameEdit.getText()));
            printMessage(String.format(getString(R.string.hint_city_not_exist), cityName));
        }
    }

    private boolean addCityToGame(GameCity city) {
        int positionInGame = GameUtil.getCityInGamePosition(game, city);
        if( positionInGame != -1 ) {
            printMessage(getString(R.string.hint_already_called, city.getName(), positionInGame));
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

    public void showUsers() {
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (User user: game.getUsers()) {
            data.add(user.getViewMap());
        }
        int[] ids = { R.id.userName, R.id.userScore };
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_user_item, User.getParametersList(), ids);

        ListView lv = (ListView) findViewById(R.id.gameUsers);
        lv.setAdapter( adapter );
        lv.setClickable(false);
    }

    private void showCitiesTable() {
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (GameCity city: game.getCities()) {
            data.add(city.getViewMap());
        }
        int[] ids = { R.id.cityName, R.id.cityIcon };
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_city_item, GameCity.getParametersList(), ids);

        ListView citiesListView = (ListView) findViewById(R.id.gameCities);
        citiesListView.setAdapter(adapter);
        citiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                GameCity gameCity = game.getCities().get(position);
                showCityOnMap(gameCity);
            }
        });
    }
}
