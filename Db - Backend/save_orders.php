<?php
require "db.php";
header("Content-Type: application/json");

$user_id = $_POST['user_id'] ?? null;
$products = json_decode($_POST['products'] ?? '', true);

if (!$user_id || empty($products)) {
    echo json_encode(["success" => false]);
    exit;
}

$sql = "INSERT INTO orders 
(user_id, product_id, product_name, quantity, price, total_price, status)
VALUES (?, ?, ?, ?, ?, ?, 'completed')";

$stmt = $conn->prepare($sql);

foreach ($products as $item) {

    $stmt->bind_param(
        "iisidd",
        $user_id,                 // i
        $item['product_id'],      // i
        $item['product_name'],    // s
        $item['quantity'],        // i
        $item['price'],           // d (unit price)
        $item['total_price']      // d
    );

    $stmt->execute();
}

echo json_encode(["success" => true]);
