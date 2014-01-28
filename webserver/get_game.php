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
    
if($user_id > 0) {
    $game_id = 0;
    //try to find exist game with current user
    $r = mysql_query("SELECT GAME.id FROM GAME, GAME_USER WHERE GAME.status = 0 AND GAME.id = GAME_USER.game_id AND GAME_USER.user_id = $user_id LIMIT 1");
    if( mysql_num_rows($r) > 0) {
        $game_id = mysql_result($r,0,0);
    } else {
        //try to find game with vacant user place
        $r = mysql_query("SELECT GAME.id FROM GAME WHERE GAME.status = 0 AND GAME.max_users > (SELECT COUNT(*) FROM GAME_USER WHERE GAME_USER.game_id = GAME.id AND GAME_USER.user_id != $user_id) LIMIT 1");
        if( mysql_num_rows($r) > 0) {
            $game_id = mysql_result($r,0,0);
            //add current user to game
            mysql_query("INSERT INTO GAME_USER (GAME_ID, USER_ID) VALUES($game_id, $user_id)");
        } else {
            //create new game
            mysql_query("INSERT INTO GAME (MAX_USERS) VALUES(5)");
            $game_id = mysql_insert_id();
            //add current user to game
            mysql_query("INSERT INTO GAME_USER (GAME_ID, USER_ID) VALUES($game_id, $user_id)");
            //add first city to game
            $r = mysql_query("SELECT ID FROM CITY WHERE LATITUDE <> 0 AND LONGITUDE <> 0");
            $first_city_id = mysql_result($r, rand(1, mysql_num_rows($r)),0);
            mysql_query("INSERT INTO GAME_CITY (GAME_ID, CITY_ID) VALUES($game_id, $first_city_id)");
        }
    }
    
    if( $game_id > 0 ) {
        //print game data
        echo $game_id ."|";  
        $r = mysql_query("SELECT USER.ID, USER.LOGIN, USER.SCORE FROM GAME_USER, USER WHERE GAME_USER.GAME_ID = $game_id AND GAME_USER.USER_ID = USER.ID");  
        for($i = 0; $row = mysql_fetch_assoc($r); $i++) {
            echo "u&" .$row[ID] ."&" .$row[LOGIN] ."&" .$row[SCORE] . "|";
        }
        $r = mysql_query("SELECT ID, NAME, LATITUDE, LONGITUDE FROM GAME_CITY, CITY WHERE GAME_ID = $game_id AND CITY_ID = ID");  
        for($i = 0; $row = mysql_fetch_assoc($r); $i++) {
            if($i != 0) echo "|";
            echo "c&" .$row[ID] ."&" .$row[NAME]."&" .$row[LATITUDE] ."&" .$row[LONGITUDE];
        }  
    } else {
        echo "NULL";
    }
} else {
    echo "NULL";
}
?>
