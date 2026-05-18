<?php
require "db.php";
header("Content-Type: application/json");

$user_id    = $_POST['user_id'] ?? null;
$product_id = $_POST['product_id'] ?? null;

if (!$user_id || !$product_id) {
    echo json_encode(["success" => false]);
    exit;
}

$sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ii", $user_id, $product_id);

echo json_encode(["success" => $stmt->execute()]);
