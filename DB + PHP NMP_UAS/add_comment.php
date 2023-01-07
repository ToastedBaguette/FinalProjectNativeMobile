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
    $memes_id = $_POST['memes_id'];
    $content = $_POST['content'];
    $publish_date = $_POST['publish_date'];

    $sql = "INSERT INTO `meme_comments`(`users_id`, `memes_id`, `content`, `publish_date`) VALUES (?,?,?,?)";
    $stmt = $c->prepare($sql);
    $stmt->bind_param("iiss", $users_id, $memes_id, $content, $publish_date);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($stmt->affected_rows > 0) {
      $arr=["result"=>"OK"];
    } else {
      $arr= ["result"=>"ERROR"];
    }

    echo json_encode($arr);
?>