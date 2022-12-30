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
    $username = $_POST['username'];
    $sql = "SELECT `idusers` FROM `users` WHERE `username` = $username";
    $result = $c->query($sql);

    if($result->num_rows>0){
        $memes = [];
        while ($obj = $result -> fetch_object()) {
            $memes[] = $obj;
        }
        $arr = array(
            "result" => "OK", 
            "data" => $memes
        );
    }else{
        $arr = array(
            "result" => "ERROR", 
            "message" => "No Data Found"
        );
    }
    echo json_encode($arr);
    return $result;
?>
