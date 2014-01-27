<?php
include_once "db_connect.php"; 
$r = mysql_query("SELECT COUNT(ID) FROM CITY");
$cities_count = mysql_result($r,0,0);
echo "$cities_count";
?>
