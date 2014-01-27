<?php
include_once "db_connect.php"; 

$user_token = $_GET['user_token']; 
$user_id = 0;
if(strlen("$user_token") > 0) {
    $r = mysql_query("SELECT USER_ID FROM USER_TOKEN WHERE TOKEN = '$user_token'");
    if( mysql_num_rows($r) > 0 ) {
        include_once "update_user_token.php"; 
        $user_id = mysql_result($r,0,0);
    }
}
$city_id = $_GET['city_id'];
   
if($user_id > 0 && strlen("$city_id") > 0) {
    $r = mysql_query("SELECT * FROM CITY WHERE ID = $city_id");
    if( mysql_num_rows($r) > 0 ) {
        $game_id = $_GET['game_id'];
        $r = mysql_query("SELECT * FROM GAME WHERE ID = $game_id AND STATUS = 0");
        if( mysql_num_rows($r) > 0 ) {
            mysql_query("INSERT INTO GAME_CITY (GAME_ID, CITY_ID, USER_ID) VALUES($game_id, $city_id, $user_id)");
            echo "TRUE";
        }
    } else {
        echo "NULL";
    }
} else {
    echo "NULL";
}
?>
