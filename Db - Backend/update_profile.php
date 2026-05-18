<?php
require "db.php";

$user_id = $_POST['user_id'];
$name = $_POST['name'];
$email = $_POST['email'];

$sql = "UPDATE users SET name=?, email=? WHERE id=?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ssi", $name, $email, $user_id);

echo json_encode(["success" => $stmt->execute()]);
?>
