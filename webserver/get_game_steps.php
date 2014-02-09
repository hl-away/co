<?php
/**
 * User: hl-away
 * Date: 08.02.14
 */
include_once "db_connect.php";
include_once "process_user_token.php";

$game_id = $_POST['game_id'];
if( strlen($game_id) == 0 ) {
    $game_id = $_GET['game_id'];
}
$last_step = $_POST['last_step'];
if( strlen($last_step) == 0 ) {
    $last_step = $_GET['last_step'];
}
if( strlen($game_id) > 0 && strlen($last_step) > 0) {
    $r = mysql_query("SELECT * FROM GAME_STEP WHERE GAME_ID = $game_id and STEP > $last_step");
    if( mysql_num_rows($r) > 0) {
        for($i = 0; $row = mysql_fetch_assoc($r); $i++) {
            if($i != 0) echo "|";
            $step = $row['STEP'];
            $step_type = $row['STEP_TYPE'];
            $object_id = $row['OBJECT_ID'];
            $user_id = $row['USER_ID'];
            $object_data = "";
            if( $step_type == "add_city" || $step_type == "add_new_city" ) {
                $r_c = mysql_query("SELECT ID, NAME, LATITUDE, LONGITUDE FROM CITY WHERE ID = $object_id");
                if( mysql_num_rows($r_c) > 0 ) {
                    $object_data = "i:" .mysql_result($r_c,0,0) ."#n:" .mysql_result($r_c,0,1)
                        ."#la:" .mysql_result($r_c,0,2) ."#lo:" .mysql_result($r_c,0,3);
                }
            }
            echo "s:" .$step ."&t:" .$step_type ."&u:" .$user_id ."&o:" .$object_data;
        }
    } else {
        echo "EMPTY";
    }
} else {
    echo "ERROR";
}


