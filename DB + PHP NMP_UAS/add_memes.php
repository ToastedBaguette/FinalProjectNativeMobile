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

    $image_url = $_POST['image_url'];
    $top_text = $_POST['top_text'];
    $bottom_text = $_POST['bottom_text'];
    $users_id = $_POST['users_id'];
    $num_likes = 0;

    $sql = "INSERT INTO `memes`(`image_url`, `top_text`, `bottom_text`, `num_likes`, `users_id`) VALUES (?, ?, ?, ?, ?)";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("sssii", $image_url, $top_text, $bottom_text, $num_likes, $users_id);
    $stmt->execute();
    

    $arr = array(
        "result" => "OK", 
        "message" => "berhasil tambah memes"
    );

    echo json_encode($arr);
?>