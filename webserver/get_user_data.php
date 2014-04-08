<?php
include_once "db_connect.php";
include_once "process_user_token.php";
   
if($user_id > 0) {
    $r = mysql_query("SELECT SCORE FROM USER WHERE ID = '$user_id'");
    if( mysql_num_rows($r) > 0 ) {
        $score = mysql_result($r,0,0);
        $r = mysql_query("SELECT COUNT(*) FROM GAME_STEP WHERE TYPE = 'connect_user' AND OBJECT_ID = '$user_id'");
        $games_count = mysql_result($r,0,0);
        echo $score ."|0";
        
    } else {
        echo "NULL";
    }
} else {
    echo "NULL";
}
