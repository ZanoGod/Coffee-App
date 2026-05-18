<?php
require 'db.php';

$category = $_GET['category'] ?? null;

if ($category) {
    $stmt = $conn->prepare(
        "SELECT id, name, price, rating, image, category
         FROM products
         WHERE category = ?
         ORDER BY rating DESC"
    );
    $stmt->bind_param("s", $category);
} else {
    // Optional: fallback
    $stmt = $conn->prepare(
        "SELECT id, name, price, rating, image, category
         FROM products
         ORDER BY rating DESC"
    );
}

$stmt->execute(); 
$result = $stmt->get_result();

$products = [];
while ($row = $result->fetch_assoc()) {
    $products[] = $row;
}

echo json_encode($products);
