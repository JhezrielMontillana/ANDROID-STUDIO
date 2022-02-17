<?php
session_start();
require_once './connection.php';
include_once './functions.php';


$SESSION = [
    "id" => $_POST["id"],
    "email" => $_POST["email"]
];

$ANNOUNCEMENT = getAnnouncements($SESSION, $connect);

$RESPONSE = [
    "message" => $ANNOUNCEMENT != null ? "Success" : "Failed",
    "status" => $ANNOUNCEMENT  != null ? 202 : 204,
    "success" => $ANNOUNCEMENT != null,
    "object" => $ANNOUNCEMENT != null ? $ANNOUNCEMENT : []
];

echo json_encode($RESPONSE);
