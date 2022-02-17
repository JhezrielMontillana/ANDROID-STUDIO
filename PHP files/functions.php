<?php


function getUserType($admin)
{
    return $admin ? "admins" : "users";
}

function signUp($user, $connect, $admin = false)
{
    $email = $user["email"];
    $password = $user["password"];
    $firstname = $user["firstname"];
    $lastname = $user["lastname"];
    $birthdate = $user["birthdate"];
    $age = $user["age"];
    $gender  = $user["gender"];
    $passwordHash = md5($password);
    $type = getUserType($admin);
    $exist = isEmailExist($email, $connect, $admin);
    $exist2 = isEmailExist($email, $connect, !$admin);

    if (!$exist && !$exist2) {
        $query = "INSERT INTO " . $type . "(email, password, firstname, lastname, birthdate, age, gender) VALUES 
        ('$email','$passwordHash', '$firstname', '$lastname', '$birthdate','$age', '$gender')";
        $stmt = $connect->prepare($query);

        if ($stmt->execute()) {
            return 202;
        } else {
            return 204;
        }
    } else {
        return 203;
    }
}

function signIn($email, $password, $connect, $admin = false)
{
    $passwordHash = md5($password);
    $query = "SELECT * FROM " . getUserType($admin) . " WHERE email ='$email' AND password='$passwordHash' LIMIT 1";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $count = $stmt->rowCount();
    $rows = $stmt->fetchAll();

    if ($count) {
        foreach ($rows as $row) {
            $userObj = [
                "id" => $row["id"],
                "email" => $row["email"],
                "firstname" => $row["firstname"],
                "lastname" => $row["lastname"],
                "birthdate" => $row["birthdate"],
                "age" => $row["age"],
                "gender" => $row["gender"]
            ];

            return $userObj;
        }
    } else {
        return null;
    }
}

function updateProfile($user, $connect, $admin = false)
{
    $id = $user["id"];
    $email = $user["email"];

    if (isEmailExist($email, $connect, $admin)) {
        $information = getUserInformation($id, $email, $connect, $admin);
        $statement = "";

        foreach ($information as $key => $value) {
            $ignore = ["password", "email", "id", "date_created"];

            if ($value != $user[$key]) {
                if (!in_array($key, $ignore)) {
                    $nval = $user[$key];
                    $statement .= $key . "='$nval',";
                }
            }
        }

        $statement = rtrim($statement, ", ");
        $query = "UPDATE " . getUserType($admin) . " SET " . $statement . " WHERE id='$id' AND email='$email' ";
        $stmt = $connect->prepare($query);

        if ($stmt->execute()) {
            return getUserInformation($id, $email, $connect, $admin);
        } else {
            return 204;
        }
    } else {
        return 203;
    }
}

function insertEvent($adminID, $adminEmail, $event, $connect)
{
    $title = $event["title"];
    $description = $event["description"];
    $location = $event["location"];
    $fromDate = $event["fromDate"];
    $toDate = $event["toDate"];
    $fromTime = $event["fromTime"];
    $toTime = $event["toTime"];
    $color = $event["color"];
    $peoples = $event["peoples"];

    $query = "INSERT INTO events (adminID, adminEmail, title, description, location, fromDate, toDate, fromTime, toTime, color, peoples) 
                         VALUES  ('$adminID', '$adminEmail','$title','$description','$location','$fromDate','$toDate','$fromTime','$toTime','$color','$peoples')";
    $stmt = $connect->prepare($query);

    if ($stmt->execute()) {
        return 202;
    } else {
        return 204;
    }
}

function insertAnnouncement($adminID, $adminEmail, $announcement, $connect)
{
    $title = $announcement["title"];
    $description = $announcement["description"];
    $query = "INSERT INTO announcements (adminID, adminEmail, title, description) 
                         VALUES  ('$adminID', '$adminEmail','$title','$description')";
    $stmt = $connect->prepare($query);

    if ($stmt->execute()) {
        return 202;
    } else {
        return 204;
    }
}

function addHeart($id, $email, $announcementID, $addOremove, $connect)
{
    $query = "";

    if ($addOremove == "add") {
        if (!checkAlreadyHeart($id, $email, $announcementID, $connect)) {
            $query = "INSERT INTO announcementHearts(id, email, announcement_id) VALUES('$id','$email','$announcementID')";
        }
    } else {
        $query = "DELETE FROM announcementHearts WHERE id ='$id' AND email='$email' AND announcement_id='$announcementID'";
    }

    $stmt = $connect->prepare($query);

    if ($stmt->execute()) {
        return 202;
    } else {
        return 204;
    }
}

function addComment($id, $email, $announcementID, $comment, $connect)
{
    $query = "INSERT INTO comments(id,email, announcement_id, comment) VALUES ('$id','$email', '$announcementID','$comment')";
    $stmt = $connect->prepare($query);

    if ($stmt->execute()) {
        return 202;
    } else {
        return 204;
    }
}

function getComments($announcementID, $connect)
{
    $query = "SELECT * FROM comments WHERE announcement_id='$announcementID'";
    $stmt = $connect->prepare($query);

    if ($stmt->execute()) {
        $all = $stmt->fetchall();
        $count = $stmt->rowCount();
        $output = [];

        if ($count > 0) {
            foreach ($all as $row) {
                array_push($output, getCommentInformation($row["comment_id"], $row["announcement_id"], $connect));
            }
            return $output;
        } else {
            return 203;
        }
    } else {
        return 204;
    }
}

function getHearts($announcementID, $connect)
{
    $query = "SELECT * FROM announcementHearts WHERE announcement_id='$announcementID'";
    $stmt = $connect->prepare($query);

    if ($stmt->execute()) {
        $all = $stmt->fetchall();
        $count = $stmt->rowCount();

        if ($count) {
            return $all;
        } else {
            return 203;
        }
    } else {
        return 204;
    }
}

function checkAlreadyHeart($id, $email, $announcementID, $connect)
{
    $query = "SELECT * FROM announcementHearts WHERE id='$id' AND email='$email' AND announcement_id='$announcementID' LIMIT 1";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $count = $stmt->rowCount();
    return $count > 0;
}

function getEvents($lastId, $connect)
{
    $query = "SELECT * FROM events WHERE event_id > '" . $lastId . "' ORDER BY event_id DESC";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $rows = $stmt->fetchall();
    $count = $stmt->rowCount();
    $outputs = array();

    if ($count) {
        foreach ($rows as $row) {
            array_push($outputs, getEventInformation($row["event_id"], $connect));
        }
        return $outputs;
    } else {
        return null;
    }
}


function getAnnouncements($session, $connect)
{
    $query = "SELECT * FROM announcements ORDER BY announcement_id DESC";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $rows = $stmt->fetchall();
    $count = $stmt->rowCount();
    $outputs = array();

    if ($count) {
        foreach ($rows as $row) {
            array_push($outputs, getAnnouncementInformation($session, $row["announcement_id"], $connect));
        }
        return $outputs;
    } else {
        return null;
    }
}




function isEmailExist($email, $connect, $admin = false)
{
    $query = "SELECT * FROM " . getUserType($admin)  . " WHERE email='$email' LIMIT 1";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $count = $stmt->rowCount();

    return $count > 0;
}

function getAnnouncementInformation($session, $announcementID, $connect)
{
    $sessionID = $session["id"];
    $sessionEmail = $session["email"];
    $query = "SELECT * FROM announcements WHERE announcement_id='$announcementID' LIMIT 1";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $rows = $stmt->fetchall();
    $count = $stmt->rowCount();

    if ($count) {
        foreach ($rows as $row) {
            $total = getHearts($row["announcement_id"], $connect);
            $totalComments = getComments($row["announcement_id"], $connect);
            $eventObj = [
                "announcement_id" => $row["announcement_id"],
                "adminID" => $row["adminID"],
                "adminEmail" => $row["adminEmail"],
                "title" => $row["title"],
                "description" => $row["description"],
                "dateCreated" => $row["dateCreated"],
                "admin" => getUserInformation($row["adminID"], $row["adminEmail"], $connect, true),
                "hearted" => checkAlreadyHeart($sessionID, $sessionEmail, $row["announcement_id"], $connect),
                "totalHearts" => ($total == 203 || $total == 204) ? [] : $total,
                "comments" => ($totalComments == 203 || $totalComments == 204) ? [] : $totalComments
            ];

            return $eventObj;
        }
    } else {
        return null;
    }
}



function getEventInformation($eventID, $connect)
{
    $query = "SELECT * FROM events WHERE event_id='$eventID' LIMIT 1";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $rows = $stmt->fetchall();
    $count = $stmt->rowCount();

    if ($count) {
        foreach ($rows as $row) {
            $eventObj = [
                "event_id" => $row["event_id"],
                "adminID" => $row["adminID"],
                "adminEmail" => $row["adminEmail"],
                "title" => $row["title"],
                "description" => $row["description"],
                "fromDate" => $row["fromDate"],
                "toDate" => $row["toDate"],
                "fromTime" => $row["fromTime"],
                "toTime" => $row["toTime"],
                "location" => $row["location"],
                "color" => $row["color"],
                "peoples" => $row["peoples"],
                "admin" => getUserInformation($row["adminID"], $row["adminEmail"], $connect, true)
            ];

            return $eventObj;
        }
    } else {
        return null;
    }
}

function getUserInformation($id, $email, $connect, $admin = false)
{
    $query = "SELECT * FROM " . getUserType($admin) . " WHERE id='$id'AND email='$email' LIMIT 1 ";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $rows = $stmt->fetchall();
    $count = $stmt->rowCount();

    if ($count) {
        foreach ($rows as $row) {
            $userObj = [
                "id" => $row["id"],
                "email" => $row["email"],
                "firstname" => $row["firstname"],
                "lastname" => $row["lastname"],
                "birthdate" => $row["birthdate"],
                "age" => $row["age"],
                "gender" => $row["gender"]
            ];

            return $userObj;
        }
    } else {
        return null;
    }
}

function getCommentInformation($commentID, $announcementID, $connect)
{
    $query = "SELECT * FROM comments WHERE comment_id='$commentID' AND announcement_id='$announcementID' LIMIT 1";
    $stmt = $connect->prepare($query);
    $stmt->execute();
    $rows = $stmt->fetchall();
    $count = $stmt->rowCount();

    if ($count) {
        foreach ($rows as $row) {
            $admin = getUserInformation($row["id"], $row["email"], $connect, true);
            $user = getUserInformation($row["id"], $row["email"], $connect, false);

            return [
                "comment_id" => $row["comment_id"],
                "id" => $row["id"],
                "announcement_id" => $row["announcement_id"],
                "email" => $row["email"],
                "comment" => $row["comment"],
                "date_created" => $row["date_created"],
                "user" => $admin ?? $user
            ];
        }
    }

    return null;
}

function calculateAge($birthDate)
{
    $birthDate = explode("/", $birthDate);
    $age = (date("md", date("U", mktime(0, 0, 0, $birthDate[0], $birthDate[1], $birthDate[2]))) > date("md")
        ? ((date("Y") - $birthDate[2]) - 1)
        : (date("Y") - $birthDate[2]));

    return (int)$age;
}
