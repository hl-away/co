<?php
include_once "db_connect.php";
$r = mysql_query("SELECT COUNT(ID) FROM GAME WHERE STATUS = 0");
$games_online_count = mysql_result($r,0,0);
$time_str = date('Y-m-d H:i:s', time() - 15 * 60 * 60);
$r = mysql_query("SELECT COUNT(USER_ID) FROM USER_TOKEN WHERE LAST_REQUEST > '$time_str'");
$users_online_count = mysql_result($r,0,0);
echo $games_online_count ."|" .$users_online_count;
?>
