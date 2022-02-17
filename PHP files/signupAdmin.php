<?php

session_start();
require_once './connection.php';
include_once './functions.php';

$USER_OBJECT = [
    "email" => $_POST["email"],
    "password" => $_POST["password"],
    "firstname" => $_POST["firstname"],
    "lastname" => $_POST["lastname"],
    "birthdate" => $_POST["birthdate"],
    "age" => calculateAge($_POST["birthdate"]),
    "gender" => $_POST["gender"]
];

$SIGNUP = signUp($USER_OBJECT, $connect, true);

$MESSAGE = "";

if ($SIGNUP == 202) {
    $MESSAGE = "Account created Successfully!";
} else if ($SIGNUP == 203) {
    $MESSAGE = "Failed, Email is already exist!";
} else {
    $MESSAGE = "Something not right, Please try again!";
}

$RESPONSE = [
    "message" => $MESSAGE,
    "status" => $SIGNUP,
    "success" => $SIGNUP == 202,
    "object" => $USER_OBJECT
];

echo json_encode($RESPONSE);
