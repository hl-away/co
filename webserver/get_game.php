<?php
include_once "db_connect.php";
include_once "process_user_token.php";

if ($user_id > 0) {
    $game_id = 0;
    //try to find exist game with current user
    $r = mysql_query("SELECT GAME.id FROM GAME, GAME_STEP WHERE GAME.status = 0 AND GAME.id = GAME_STEP.game_id AND GAME_STEP.type = 'connect_user' AND GAME_STEP.object_id = $user_id LIMIT 1");
    if (mysql_num_rows($r) > 0) {
        $game_id = mysql_result($r, 0, 0);
    } else {
        //try to find game with vacant user place
        $r = mysql_query("SELECT GAME.id FROM GAME WHERE GAME.status = 0 AND GAME.max_users > (SELECT COUNT(*) FROM GAME_STEP WHERE GAME_STEP.game_id = GAME.id AND GAME_STEP.type = 'connect_user' AND GAME_STEP.object_id != $user_id) LIMIT 1");
        if (mysql_num_rows($r) > 0) {
            $game_id = mysql_result($r, 0, 0);
        } else {
            //create new game
            mysql_query("INSERT INTO GAME (MAX_USERS) VALUES(5)");
            $game_id = mysql_insert_id();
            //add first city to game
            $r = mysql_query("SELECT ID FROM CITY WHERE LATITUDE <> 0 AND LONGITUDE <> 0");
            $first_city_id = mysql_result($r, rand(1, mysql_num_rows($r)), 0);
            mysql_query("INSERT INTO GAME_STEP (GAME_ID, USER_ID, STEP, TYPE, OBJECT_ID) VALUES($game_id, 0, 1, 'add_city', $first_city_id)");
        }
        //add current user to game
        mysql_query("INSERT INTO GAME_STEP (GAME_ID, USER_ID, STEP, TYPE, OBJECT_ID) VALUES($game_id, $user_id, 2, 'connect_user', $user_id)");
    }

    if ($game_id > 0) {
        //print game data
        echo $game_id . "|";
        $r = mysql_query("SELECT USER.id, USER.login, USER.score FROM GAME_STEP, USER WHERE GAME_STEP.game_id = $game_id AND GAME_STEP.type = 'connect_user' AND GAME_STEP.object_id = USER.id");
        for ($i = 0; $row = mysql_fetch_assoc($r); $i++) {
            echo "u&" . $row['ID'] . "&" . $row['LOGIN'] . "&" . $row['SCORE'] . "|";
        }
        $r = mysql_query("SELECT ID, NAME, LATITUDE, LONGITUDE FROM GAME_STEP, CITY WHERE GAME_STEP.game_id = $game_id AND (GAME_STEP.type = 'add_city' OR GAME_STEP.type = 'add_new_city') AND GAME_STEP.object_id = CITY.id");
        for ($i = 0; $row = mysql_fetch_assoc($r); $i++) {
            if ($i != 0) echo "|";
            echo "c&" . $row['ID'] . "&" . $row['NAME'] . "&" . $row['LATITUDE'] . "&" . $row['LONGITUDE'];
        }
    } else {
        echo "ERROR";
    }
} else {
    echo "ERROR";
}
