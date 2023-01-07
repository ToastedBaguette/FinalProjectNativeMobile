<?php
error_reporting(E_ERROR | E_PARSE);
$c = new mysqli("localhost", "native_160420041", "ubaya", "native_160420041");
$arr=[];

if($c->connect_errno) {
    $arr = array(
        "result" => "ERROR", 
        "message" => "Failed to connect DB"
    );
    echo json_encode($arr);
    die();
}

    $firstname =  $_POST['firstname'];
    $lastname = $_POST['lastname'];
    $privacy = $_POST['privacySet'];
    $idusers = $_POST['iduser'];

    $sql = "UPDATE users SET first_name = ?, last_name = ?, privacy_setting = ? where idusers=?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("ssii", $firstname , $lastname, $privacy, $idusers);
    $stmt->execute();

    if($stmt-> affected_rows <= 0){
        $arr = ["result" => "FAILED"];
    }
    else{
        $arr = ["result" => "OK" ];
    }    

    echo json_encode($arr);
?>