<?php
include_once "db_connect.php"; 

$user_token = $_GET['user_token']; 

$result = ""; 
if(strlen("$user_token") > 0) {
    $r = mysql_query("SELECT ID FROM USER_TOKEN WHERE TOKEN = '$user_token'");
    if( mysql_num_rows($r) > 0 ) {
        include_once "update_user_token.php"; 
        $result = "TRUE";
    }
}

if(strlen("$result") == 0) {
    $user_name = $_GET['user_name'];
    if(strlen("$user_name") > 0) {
        $user_name = str_replace("_", " ", $user_name);
        mysql_query("INSERT INTO USER (LOGIN) VALUES('$user_name')");
        $user_id = mysql_insert_id();
        $token = md5($user_name + date(time()) + mt_rand());
        mysql_query("INSERT INTO USER_TOKEN (USER_ID, TOKEN) VALUES($user_id, '$token')");
        $result = $token;
    } else {
        $result = "NULL";
    }
}

echo $result;
?>
