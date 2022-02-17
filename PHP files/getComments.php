<?php

session_start();
require_once './connection.php';
include_once './functions.php';

$ANNOUNCEMENTID = $_POST["announcement_id"];

$GET = getComments($ANNOUNCEMENTID,  $connect);
$CON = $GET != 204 && $GET != 203;

$RESPONSE = [
    "message" => $CON ? "Success" : "Failed",
    "status" => $CON ? 202 : 204,
    "success" => $CON,
    "object" => $CON ? $GET : []
];

echo json_encode($RESPONSE);
