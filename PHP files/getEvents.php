<?php
session_start();
require_once './connection.php';
include_once './functions.php';

$LASTEVENTID  = $_POST['last_event_id'];
$EVENTS = getEvents($LASTEVENTID ?? 0, $connect);

$RESPONSE = [
    "message" => $EVENTS != null ? "Success" : "Failed",
    "status" => $EVENTS  != null ? 202 : 204,
    "success" => $EVENTS != null,
    "object" => $EVENTS != null ? $EVENTS : []
];

echo json_encode($RESPONSE);
