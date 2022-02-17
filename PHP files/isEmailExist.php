<?php
session_start();
require_once './connection.php';
include_once './functions.php';

$EMAIL  = $_POST['email'];
$ONADMIN = isEmailExist($EMAIL, $connect, true);
$ONUSER = isEmailExist($EMAIL, $connect, false);
$CHECK = $ONADMIN || $ONUSER;

$RESPONSE = [
    "message" => !$CHECK ? "Email is available" : "Email is already exist!",
    "status" => !$CHECK ? 202 : 204,
    "success" => $CHECK,
    "email" => $EMAIL
];

echo json_encode($RESPONSE);
