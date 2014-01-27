<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />                     
<title>Hello, this is Android and MySQL!</title>
</head>
<body>
<?php
include_once "db_connect.php"; 
$result = mysql_query("SELECT * FROM USER");
$rows = mysql_num_rows($result);
$fields = mysql_num_fields($result);
echo "<center><h2>Общее количество пользователей: $rows</h2></center>";
?>

<table align="center">
<tr>
<td>ID</td>
<td>LOGIN</td>
<td>PASSWORD</td>
<td>REG_DATE</td>
<td>SCORE</td>
</tr>
<?php
for($i=0; $row=mysql_fetch_assoc($result); $i++)
{
    echo "<tr>";
    echo "<td>$row[ID]</td>";
    echo "<td>$row[LOGIN]</td>";
    echo "<td>$row[PASSWORD]</td>";
    echo "<td>$row[REG_DATE]</td>";
    echo "<td>$row[SCORE]</td>";
    echo "</tr>";
}  
?>
</table>
</body>
</html>
