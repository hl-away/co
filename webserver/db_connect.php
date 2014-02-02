<?php
$host = "localhost";
$user = "hlaway_co";
$pass = "dX93deLrLe";
$db = "hlaway_co";

$id = mysql_connect($host,$user,$pass);
mysql_select_db($db);
mysql_query("SET NAMES utf8",$id);
?>