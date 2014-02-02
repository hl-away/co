<?php
include_once "db_connect.php"; 

$city_name = $_POST['city_name'];
$latitude = $_POST['latitude'];;
$longitude = $_POST['longitude'];
$country_name = $_POST['country_name'];
$country_code = $_POST['country_code'];

$country_id = 0;
if(strlen($country_code) > 0) {
    $r = mysql_query("SELECT ID FROM COUNTRY WHERE CODE = '$country_code'");
    if( mysql_num_rows($r) > 0 ) {
        $country_id = mysql_result($r,0,0);
    } else {
        mysql_query("INSERT INTO COUNTRY (NAME, CODE) VALUES('$country_name', '$country_code')");
        $country_id = mysql_insert_id();
    }
}
    
if(strlen("$city_name") > 0 && strlen("$latitude") > 0 && strlen("$longitude") > 0 && $country_id > 0) {
    mysql_query("UPDATE CITY SET LATITUDE = $latitude, LONGITUDE = $longitude, COUNTRY_ID = $country_id WHERE NAME = '$city_name' AND LATITUDE = 0 AND LONGITUDE = 0");
} else {
    echo "NULL";
}
?>
