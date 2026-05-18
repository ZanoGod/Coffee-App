<?php
require "db.php";

$cart_id = $_POST['cart_id'];

$sql = "DELETE FROM cart WHERE id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $cart_id);

echo $stmt->execute()
    ? json_encode(["success" => true])
    : json_encode(["success" => false]);
?>
