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

    $image_url = $_POST['image_url'];
    $top_text = $_POST['top_text'];
    $bottom_text = $_POST['bottom_text'];
    $users_id = $_POST['users_id'];
    $num_likes = 0;

    $sql = "INSERT INTO `memes`(`image_url`, `top_text`, `bottom_text`, `num_likes`, `users_id`) VALUES (?, ?, ?, ?, ?)";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("sssii", $image_url, $top_text, $bottom_text, $num_likes, $users_id);
    $stmt->execute();
    
    if ($stmt->affected_rows > 0) {
        $arr=["result"=>"OK"];
    } else {
    $arr= ["result"=>"error"];
    }
    echo json_encode($arr);
    $stmt->close();
    $conn->close();
?>