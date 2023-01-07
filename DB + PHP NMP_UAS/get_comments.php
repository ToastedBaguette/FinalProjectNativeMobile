<?php

  error_reporting(E_ERROR|E_PARSE);//disable error

  $c = new mysqli("localhost", "native_160420041", "ubaya", "native_160420041");
  $arr = [];

  if($c->connect_errno){
    $arr = array(
      'result' => 'ERROR', 
      'message' => 'failed to connect DB'
    );

    echo json_encode($arr);
    die();
  }
  $memes_id = $_POST['memes_id'];

  $c -> set_charset("UTF8");//beberapa browser ada yang error klo perintah ini diset
  $sql = "SELECT * FROM meme_comments INNER JOIN users ON meme_comments.users_id = users.idusers WHERE meme_comments.memes_id = ? ORDER BY meme_comments.publish_date DESC";
  $stmt = $c->prepare($sql);
  $stmt->bind_param("i", $memes_id);
  $stmt->execute();
  $result = $stmt->get_result();
  
  if($result->num_rows > 0){
    $comments = [];
    while ($obj = $result->fetch_assoc()) {
      $comments[] = $obj; 
    }
    $arr = array(
      'result' => 'OK', 
      'data' => $comments
    );
  }else{
    $arr = array(
      'result' => 'ERROR', 
      'message' => 'No data found'
    );
  }

  echo json_encode($arr);
?>