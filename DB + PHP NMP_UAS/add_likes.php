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

  $sql = "UPDATE memes SET num_likes = num_likes+1 WHERE idmemes = ?";
  $stmt = $c->prepare($sql);
  $stmt->bind_param("i", $idmemes);
  $stmt->execute();

  $sql = "INSERT INTO `memes_likes`(`idmemes`, `idusers`) VALUES (?,?)";
  $stmt = $c->prepare($sql);
  $stmt->bind_param("ii", $idmemes, $idusers);
  $stmt->execute();

  if($stmt->affected_rows > 0){
    $arr = array("result" => "OK",
    "sql" => $sql,
    "message" => "likes updated");
    echo json_encode($arr);
  }
  else {
    $arr = array("result" => "ERROR", "message" => "id is required");
    echo json_encode($arr);
  }
} 
?>