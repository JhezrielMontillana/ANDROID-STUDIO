<?php

session_start();
require_once './connection.php';
include_once './functions.php';

$EMAIL = $_POST["email"];
$PASSWORD = $_POST["password"];

$SIGNIN = signIn($EMAIL, $PASSWORD, $connect, true);

$RESPONSE = [
    "message" => $SIGNIN != null ? "Welcome Admin " . $SIGNIN["lastname"] : "Login Failed",
    "status" => $SIGNIN != null ? 202 : 204,
    "success" => $SIGNIN != null,
    "object" => $SIGNIN
];

echo json_encode($RESPONSE);
