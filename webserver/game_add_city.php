<?php
include_once "db_connect.php";
include_once "process_user_token.php";

$city_id = $_POST['city_id'];
$game_id = $_POST['game_id'];
   
if($user_id > 0 && strlen("$city_id") > 0 && strlen("$game_id") > 0) {
    $r = mysql_query("SELECT ID FROM CITY WHERE ID = $city_id");
    if( mysql_num_rows($r) > 0 ) {
        $type = "add_city";
        $new_city = $_POST['new_city'];
        if( $new_city == 'true' ) {
            $type = "add_new_city";
        }
        $r = mysql_query("SELECT * FROM GAME WHERE ID = $game_id AND STATUS = 0");
        if( mysql_num_rows($r) > 0 ) {
            $r = mysql_query("SELECT COUNT(ID) FROM GAME_STEP WHERE GAME_ID = $game_id");
            $step_str = mysql_result($r,0,0);
            $step = 1 + ((int) $step_str );
            mysql_query("INSERT INTO GAME_STEP (GAME_ID, USER_ID, STEP, TYPE, OBJECT_ID) VALUES($game_id, $user_id, $step, '$type', $city_id)");
            echo "TRUE";
        }
    } else {
        echo "NULL";
    }
} else {
    echo "NULL";
}
