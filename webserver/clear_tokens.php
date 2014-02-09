<?php
include_once "db_connect.php"; 

$time_str = date('Y-m-d H:i:s', time() - 15 * 60);
mysql_query("DELETE FROM USER_TOKEN WHERE LAST_REQUEST < '$time_str'");
