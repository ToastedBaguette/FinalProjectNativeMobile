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
    $users_id = $_POST['users_id'];
    $sql = "SELECT * FROM `memes` WHERE `users_id` = $users_id";
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
?>
