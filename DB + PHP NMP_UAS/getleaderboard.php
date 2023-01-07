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

  $c -> set_charset("UTF8");//beberapa browser ada yang error klo perintah ini diset
  $sql = "SELECT CONCAT(users.first_name, ' ', users.last_name) AS fullname, users.avatar_img, SUM(memes.num_likes) AS total_likes, users.privacy_setting
FROM users
LEFT JOIN memes ON users.idusers = memes.users_id
GROUP BY users.idusers
ORDER BY total_likes DESC";
  $result =$c -> query($sql);

  
  if($result->num_rows > 0){
    $leaderboard = [];
    while ($obj = $result->fetch_assoc()) {
      $leaderboard[] = $obj; 
    }
    $arr = array(
      'result' => 'OK', 
      'data' => $leaderboard 
    );
  }else{
    $arr = array(
      'result' => 'ERROR', 
      'message' => 'No data found'
    );
  }

  echo json_encode($arr);
?>