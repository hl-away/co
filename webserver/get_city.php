<?php
include_once "db_connect.php"; 

$city_name = $_POST['city_name'];
if(strlen("$city_name") == 0) 
    $city_name = $_GET['city_name'];
   
if(strlen("$city_name") > 0) {
    $city_name = str_replace("_", " ", $city_name);
    $r = mysql_query("SELECT ID, NAME, LATITUDE, LONGITUDE FROM CITY WHERE NAME = '$city_name'");
    if( mysql_num_rows($r) > 0 ) {
        $row = mysql_fetch_assoc($r);
        echo $row["ID"] ."|" .$row["NAME"] ."|" .$row["LATITUDE"] ."|" .$row["LONGITUDE"];
    } else {
        echo "NULL";
    }
} else {
    echo "NULL";
}
?>
