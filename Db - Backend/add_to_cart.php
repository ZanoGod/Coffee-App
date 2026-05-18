<?php
require "db.php";
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json");

$user_id      = $_POST['user_id'] ?? null;
$product_id   = $_POST['product_id'] ?? null;
$product_name = $_POST['product_name'] ?? null;
$image_url    = $_POST['image_url'] ?? null;
$price        = $_POST['price'] ?? null;

if (!$user_id || !$product_id || !$product_name || !$image_url || !$price) {
    echo json_encode([
        "success" => false,
        "error" => "Missing parameters"
    ]);
    exit;
}

$sql = "
    INSERT INTO cart (user_id, product_id, product_name, image_url, price, quantity)
    VALUES (?, ?, ?, ?, ?, 1)
    ON DUPLICATE KEY UPDATE quantity = quantity + 1
";

$stmt = $conn->prepare($sql);

if (!$stmt) {
    echo json_encode([
        "success" => false,
        "error" => $conn->error
    ]);
    exit;
}

/*
  Types:
  i = int
  i = int
  s = string
  s = string
  d = double
*/
$stmt->bind_param(
    "iissd",
    $user_id,
    $product_id,
    $product_name,
    $image_url,
    $price
);

if ($stmt->execute()) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode([
        "success" => false,
        "error" => $stmt->error
    ]);
}
