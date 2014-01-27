<?php
include_once "db_connect.php"; 
$query = mysql_query("SELECT * FROM USER");
$users_count = mysql_num_rows($query);
echo "$users_count";
?>
