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

if (!$user_id || !$product_id) {
    echo json_encode([
        "success" => false,
        "error" => "Missing parameters"
    ]);
    exit;
}

$sql = "
    INSERT IGNORE INTO favorites 
    (user_id, product_id, product_name, image_url, price)
    VALUES (?, ?, ?, ?, ?)
";

$stmt = $conn->prepare($sql);

if (!$stmt) {
    echo json_encode([
        "success" => false,
        "error" => $conn->error
    ]);
    exit;
}

$stmt->bind_param(
    "iissd",
    $user_id,
    $product_id,
    $product_name,
    $image_url,
    $price
);

if ($stmt->execute()) {
    echo json_encode([
        "success" => true,
        "added" => $stmt->affected_rows > 0
    ]);
} else {
    echo json_encode([
        "success" => false,
        "error" => $stmt->error
    ]);
}
