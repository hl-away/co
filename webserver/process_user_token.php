<?php
$user_token = $_POST['user_token'];
$user_id = 0;
if(strlen("$user_token") == 0) {
    $user_token = $_GET['user_token'];
}
if(strlen("$user_token") > 0) {
    $r = mysql_query("SELECT USER_ID FROM USER_TOKEN WHERE TOKEN = '$user_token'");
    if( mysql_num_rows($r) > 0 ) {
        include_once "update_user_token.php";
        $user_id = mysql_result($r,0,0);
    }
}