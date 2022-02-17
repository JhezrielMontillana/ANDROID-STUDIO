<?php

session_start();
require_once './connection.php';
include_once './functions.php';

$ANNOUNCEMENT_OBJECT  = [
    "title" => $_POST["title"],
    "description" => $_POST["description"],
];

$INSERT = insertAnnouncement($_POST["adminID"], $_POST["adminEmail"],  $ANNOUNCEMENT_OBJECT, $connect);

$RESPONSE = [
    "message" => $INSERT == 202 ? "Announcement Success!" : "Announcement Insertion Failed!",
    "status" => $INSERT,
    "success" => $INSERT == 202,
    "object" => []
];

echo json_encode($RESPONSE);
