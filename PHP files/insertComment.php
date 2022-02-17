<?php

session_start();
require_once './connection.php';
include_once './functions.php';

$ANNOUNCEMENTID = $_POST["announcement_id"];
$ID = $_POST["id"];
$EMAIL = $_POST["email"];
$COMMENT = $_POST["comment"];

$ADDCOMMENT = addComment($ID, $EMAIL, $ANNOUNCEMENTID, $COMMENT,  $connect);

$RESPONSE = [
    "message" => $ADDCOMMENT != 204 ? "Successfully Commented" : "Failed",
    "status" => $ADDCOMMENT != 204 ? 202 : 204,
    "success" => $ADDCOMMENT != 204,
    "object" => $ADDCOMMENT
];

echo json_encode($RESPONSE);
