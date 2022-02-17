<?php

session_start();
require_once './connection.php';
include_once './functions.php';

$EVENT_OBJECT  = [
    "title" => $_POST["title"],
    "description" => $_POST["description"],
    "fromDate" => $_POST["fromDate"],
    "toDate" => $_POST["toDate"],
    "fromTime" => $_POST["fromTime"],
    "toTime" => $_POST["toTime"],
    "location" => $_POST["location"],
    "color" => $_POST["color"],
    "peoples" => $_POST["peoples"]
];

$INSERT = insertEvent($_POST["adminID"], $_POST["adminEmail"],  $EVENT_OBJECT, $connect);

$RESPONSE = [
    "message" => $INSERT == 202 ? "Event Success!" : "Event Insertion Failed!",
    "status" => $INSERT,
    "success" => $INSERT == 202,
    "object" => []
];

echo json_encode($RESPONSE);
