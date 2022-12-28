<?php

  error_reporting(E_ERROR|E_PARSE);//disable error

  $c = new mysqli("localhost", "root", "","projectuasnmp");
  $arr = [];

  if($c->connect_errno){
    $arr = array(
      'result' => 'ERROR', 
      'message' => 'failed to connect DB'
    );

    echo json_encode($arr);
    die();
  }
  
  $username = $_POST['username'];
  $first_name = $_POST['first_name'];
  $last_name = $_POST['last_name'];
  $password = $_POST['password'];
  $date_regist = $_POST['date_regist'];
  $avatar_img = $_POST['avatar_img'];
  
  $c -> set_charset("UTF8");//beberapa browser ada yang error klo perintah ini diset
  $sql = "INSERT INTO `users`(`username`, `first_name`, `last_name`, `password`, `date_regist`, `avatar_img`, `privacy_setting`) VALUES (?,?,?,?,?,?, 0)";
  $stmt = $c -> prepare($sql);
  $stmt->bind_param('ssssss', $username, $first_name, $last_name, $password, $date_regist, $avatar_img);
  $stmt->execute();

  $arr = array("result" => "OK",
  "message" => "Account registered successfully ");
   echo json_encode($arr);
?>