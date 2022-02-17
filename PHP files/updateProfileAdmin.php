try {
    $USER_OBJECT = [
        "id" => $_POST["id"],
        "email" => $_POST["email"],
        "firstname" => $_POST["firstname"],
        "lastname" => $_POST["lastname"],
        "birthdate" => $_POST["birthdate"],
        "age" => calculateAge($_POST["birthdate"]),
        "gender" => $_POST["gender"]
    ];

    $EDIT = updateProfile($USER_OBJECT, $connect, true);
    $IS = $EDIT != 203 && $EDIT != 204;
} catch (FirstException | SecondException $e) {
}

if ($EDIT == 203) {
    $MESSAGE = "Failed, Account not found!";
} else if ($EDIT == 204) {
    $MESSAGE = "Something not right, Please try again!";
} else {
    $MESSAGE = "Account updated Successfully!";
}

$RESPONSE = [
    "message" => $MESSAGE,
    "status" => $IS ? 202 : $EDIT,
    "success" => $IS,
    "object" => $EDIT
];

echo json_encode($RESPONSE);
