<?php
include_once "db_connect.php"; 

$user_id = $_POST['user_id'];
if(strlen("$user_id") == 0) {
    $user_token = $_POST['user_token']; 
    if(strlen("$user_token") > 0) {
        $r = mysql_query("SELECT USER_ID FROM USER_TOKEN WHERE TOKEN = '$user_token'");
        if( mysql_num_rows($r) > 0 ) {
            include_once "update_user_token.php"; 
            $user_id = mysql_result($r,0,0);
        }
    }
}
   
if(strlen("$user_id") > 0) {
    $r = mysql_query("SELECT SCORE FROM USER WHERE ID = '$user_id'");
    if( mysql_num_rows($r) > 0 ) {
        $score = mysql_result($r,0,0);
        $r = mysql_query("SELECT COUNT(*) FROM GAME_USER WHERE USER_ID = '$user_id'");
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

?>
