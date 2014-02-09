<?php
include_once "db_connect.php";
include_once "process_user_token.php";
   
if(strlen("$user_id") > 0) {
    $r = mysql_query("SELECT SCORE FROM USER WHERE ID = '$user_id'");
    if( mysql_num_rows($r) > 0 ) {
        $score = mysql_result($r,0,0);
        $games_count = 0;
        $r = mysql_query("SELECT COUNT(*) FROM GAME_STEP WHERE TYPE = 'connect_user' OBJECT_ID = '$user_id'");
        if( mysql_num_rows($r) > 0 ) {
            $games_count = mysql_result($r,0,0);
        }
        echo $score ."|" .$games_count;
        
    } else {
        echo "NULL";
    }
} else {
    echo "NULL";
}
