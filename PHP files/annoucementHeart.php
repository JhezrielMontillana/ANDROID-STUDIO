<?php

session_start();
require_once './connection.php';
include_once './functions.php';

$ANNOUNCEMENTID = $_POST["announcement_id"];
$ADMINID = $_POST["adminID"];
$ADMINEMAIL = $_POST["adminEmail"];
$ADDORREMOVE = $_POST["addOrRemove"];

$HEART = addHeart($ADMINID, $ADMINEMAIL, $ANNOUNCEMENTID, $ADDORREMOVE,  $connect);

$RESPONSE = [
    "message" => $HEART == 202 ? $ADDORREMOVE == "add" ? "Heart added" : "Heart removed" : "Failed",
    "status" => $HEART,
    "success" => $HEART == 202,
    "object" => $HEART
];

echo json_encode($RESPONSE);
