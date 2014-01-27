<?php
include_once "db_connect.php"; 

$city_name = $_POST['city_name'];
if(strlen("$city_name") == 0) 
    $city_name = $_GET['city_name'];
$latitude = $_POST['latitude'];
if(strlen("$latitude") == 0) 
    $latitude = $_GET['latitude'];
$longitude = $_POST['longitude'];
if(strlen("$longitude") == 0) 
    $longitude = $_GET['longitude'];
    
    
if(strlen("$city_name") > 0 && strlen("$latitude") > 0 && strlen("$longitude") > 0) {    
    $city_name = str_replace("_", " ", $city_name);
    mysql_query("UPDATE CITY SET LATITUDE = $latitude, LONGITUDE = $longitude WHERE NAME = '$city_name' AND LATITUDE = 0 AND LONGITUDE = 0");
} else {
    echo "NULL";
}

?>
