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

  $c -> set_charset("UTF8");//beberapa browser ada yang error klo perintah ini diset
  $sql = "SELECT * FROM memes ORDER BY idmemes DESC";
  $result =$c -> query($sql);

  
  if($result->num_rows > 0){
    $memes = [];
    while ($obj = $result->fetch_assoc()) {
      $memes[] = $obj; 
    }
    $arr = array(
      'result' => 'OK', 
      'data' => $memes
    );
  }else{
    $arr = array(
      'result' => 'ERROR', 
      'message' => 'No data found'
    );
  }

  echo json_encode($arr);
?>