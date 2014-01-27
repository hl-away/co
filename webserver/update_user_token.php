<?php
include_once "db_connect.php"; 

$time_str = date('Y-m-d H:i:s', time());
if(strlen("$user_token") > 0) {
    mysql_query("UPDATE USER_TOKEN SET LAST_REQUEST = '$time_str' WHERE TOKEN = '$user_token'");
} else if($user_id != 0) {
    mysql_query("UPDATE USER_TOKEN SET LAST_REQUEST = '$time_str' WHERE USER_ID = $user_id");
}
include_once "clear_tokens.php"; 
?>
