<?php
error_reporting(E_ERROR | E_PARSE);
$c = new mysqli("localhost", "native_160420041", "ubaya", "native_160420041");

if($c->connect_errno) {
$arr = array("result" => "ERROR",
 "message" => "Failed to Connect");
echo json_encode($arr);
die();
}

if(isset($_POST['idmemes'])&&isset($_POST['idusers'])) {
  
  $idmemes = (int) $_POST['idmemes'];
  $idusers = (int) $_POST['idusers'];

  $sql = "SELECT * FROM `memes_likes` WHERE `idmemes`= ? AND  `idusers`= ?";
  $stmt = $c->prepare($sql);
  $stmt->bind_param("ii", $idmemes, $idusers);
  $stmt->execute();
  $result = $stmt->get_result();

  if($result->num_rows > 0){
    $arr = array("result" => "LIKED",
    "message" => "Meme liked");
    echo json_encode($arr);
  }
  else {
    $arr = array("result" => "NOLIKE", 
    "message" => "Meme had no like");
    echo json_encode($arr);
  }
} 
?>