<?php
include_once "db_connect.php"; 

$user_id = $_POST['user_id'];
if(strlen("$user_id") == 0) 
    $user_id = $_GET['user_id'];

if($user_id > 0) {
    mysql_query("INSERT INTO GAME (INIT_USER_ID) VALUES('$user_id')");
    echo mysql_insert_id();
} else {
    echo 'NULL';
}
?>
