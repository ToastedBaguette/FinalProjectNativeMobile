<?php
error_reporting(E_ERROR | E_PARSE);
$c = new mysqli("localhost", "root", "", "projectuasnmp");
$arr=[];

if($c->connect_errno) {
    $arr = array(
        "result" => "ERROR", 
        "message" => "Failed to connect DB"
    );
    echo json_encode($arr);
    die();
}

    $username =  $_POST['username'];
    $password = $_POST['password'];

    $sql = "select * from users where username=? and password=?";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("ss", $username, $password);
    $stmt->execute();
    $res = $stmt->get_result();

    $account = [];


    if($res -> num_rows <= 0){
        $arr = ["result" => "FAILED"];
    }
    else{
        $row = mysqli_fetch_assoc($res);
        $arr = ["data" => $row, "result" => "OK" ];
    }    

    echo json_encode($arr);
?>