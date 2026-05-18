<?php
require "db.php";
header("Content-Type: application/json");

$user_id    = $_POST['user_id'] ?? null;
$product_id = $_POST['product_id'] ?? null;

if (!$user_id || !$product_id) {
    echo json_encode(["success" => false, "error" => "Missing parameters"]);
    exit;
}

// Check if already favorited
$check = $conn->prepare(
    "SELECT id FROM favorites WHERE user_id = ? AND product_id = ?"
);
$check->bind_param("ii", $user_id, $product_id);
$check->execute();
$result = $check->get_result();

if ($result->num_rows > 0) {
    // REMOVE favorite
    $delete = $conn->prepare(
        "DELETE FROM favorites WHERE user_id = ? AND product_id = ?"
    );
    $delete->bind_param("ii", $user_id, $product_id);
    $delete->execute();

    echo json_encode([
        "success" => true,
        "is_favorite" => false
    ]);
} else {
    // ADD favorite
    $insert = $conn->prepare(
        "INSERT INTO favorites (user_id, product_id) VALUES (?, ?)"
    );
    $insert->bind_param("ii", $user_id, $product_id);
    $insert->execute();

    echo json_encode([
        "success" => true,
        "is_favorite" => true
    ]);
}
