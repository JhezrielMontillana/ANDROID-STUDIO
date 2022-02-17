<?php

$dbhost = "localhost";
$dbuser = "id18376696_cc107group2dbuname";
$dbpass = "9wxXxI3?A/o$=CbW";
$dbname = "id18376696_cc107group2db";

$connect = new PDO("mysql:host=" . $dbhost . ";dbname=" . $dbname, $dbuser, $dbpass);

if ($connect) {
    // echo "Successfully connected to the database!";
} else {
    echo "There's an error while connecting to the database!";
}
