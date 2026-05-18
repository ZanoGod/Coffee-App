<?php
require "db.php";
header("Content-Type: application/json");

$user_id = $_GET['user_id'] ?? null;
if (!$user_id) {
    echo json_encode([]);
    exit;
}

$sql = "SELECT product_id FROM favorites WHERE user_id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$favIds = [];
while ($row = $result->fetch_assoc()) { 
    $favIds[] = (int)$row['product_id'];
}

echo json_encode($favIds);
