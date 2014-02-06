<?php
include_once "db_connect.php";

$user_token = $_POST['user_token'];
$user_id = 0;
if(strlen("$user_token") > 0) {
    $r = mysql_query("SELECT USER_ID FROM USER_TOKEN WHERE TOKEN = '$user_token'");
    if( mysql_num_rows($r) > 0 ) {
        include_once "update_user_token.php"; 
        $user_id = mysql_result($r,0,0);
    }
}
$city_id = $_POST['city_id'];
$game_id = $_POST['game_id'];
   
if($user_id > 0 && strlen("$city_id") > 0 && strlen("$game_id") > 0) {
    $r = mysql_query("SELECT ID FROM CITY WHERE ID = $city_id");
    if( mysql_num_rows($r) > 0 ) {
        $step_type = "add_city";
        $new_city = $_POST['new_city'];
        if( $new_city == 'true' ) {
            $step_type = "add_new_city";
        }
        $r = mysql_query("SELECT * FROM GAME WHERE ID = $game_id AND STATUS = 0");
        if( mysql_num_rows($r) > 0 ) {
            $r = mysql_query("SELECT COUNT(*) FROM GAME_STEP WHERE GAME_ID = $game_id");
            $step_str = mysql_result($r,0,0);
            $step = 1 + ((int) $step_str );
            mysql_query("INSERT INTO GAME_STEP (GAME_ID, USER_ID, STEP, STEP_TYPE, OBJECT_ID) VALUES($game_id, $user_id, $step, '$step_type', $city_id)");
            echo "TRUE";
        }
    } else {
        echo "NULL";
    }
} else {
    echo "NULL";
}
?>
