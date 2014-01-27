<?php
$host = "localhost";
$user = "362420";
$pass = "hl-away13311331";
$db = "362420";

$id = mysql_connect($host,$user,$pass);
mysql_select_db($db);   
mysql_query("SET NAMES utf8",$id);
?>