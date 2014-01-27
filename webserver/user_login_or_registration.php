<?php
include_once "db_connect.php"; 
$user_login = $_POST['user_login']; 
$user_password = $_POST['user_password'];

if(strlen("$user_login") > 0 && strlen("$user_password") > 0) {
    if(strlen("$user_password") < 5) {
        echo "ERROR=SHORT_PASSWORD";
    } else {
        $r = mysql_query("SELECT PASSWORD, ID FROM USER WHERE LOGIN = '$user_login'");
        $user_id = 0;
        if(mysql_num_rows($r) > 0) {
            $password = mysql_result($r,0,0);
            if($password == $user_password) {
                $user_id = mysql_result($r,0,1);
            } else {
                echo "ERROR=INCORRECT_PASSWORD";
            }
        } else {
            mysql_query("INSERT INTO USER (LOGIN, PASSWORD) VALUES('$user_login', '$user_password')");
            $user_id = mysql_insert_id();
        }
        
        if($user_id != 0) {
            $r = mysql_query("SELECT TOKEN FROM USER_TOKEN WHERE USER_ID = $user_id");
            if(mysql_num_rows($r) > 0) {
                $user_token = mysql_result($r,0,0);
                include "update_user_token.php"; 
            } else {
                $user_token = md5($user_login + date(time()) + mt_rand());
                mysql_query("INSERT INTO USER_TOKEN (USER_ID, TOKEN) VALUES($user_id, '$user_token')");
            }
            
            echo $user_token;
        }
    }
} else {
    echo "ERROR=EMPTY_PARAMETERS";
}
?>
