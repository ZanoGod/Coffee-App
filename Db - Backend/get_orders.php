<?php
require "db.php";
header("Content-Type: application/json");

if (!isset($_GET['user_id'])) {
    echo json_encode([]);
    exit;
}

$user_id = intval($_GET['user_id']);

$sql = "
SELECT 
    user_id,
    product_name,
    quantity,
    total_price,
    status,
    created_at
FROM orders
WHERE user_id = ?
ORDER BY created_at DESC
";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$orders = [];

while ($row = $result->fetch_assoc()) {
    $key = $row['created_at']; // GROUP BY TIME

    if (!isset($orders[$key])) {
        $orders[$key] = [
            'id' => strtotime($row['created_at']), // fake order id
            'userId' => (int)$row['user_id'],
            'totalAmount' => 0.0,
            'status' => $row['status'],
            'orderDate' => $row['created_at'],
            'items' => []
        ];
    }

    $orders[$key]['totalAmount'] += (float)$row['total_price'];
    $orders[$key]['items'][] =
        $row['product_name'] . " x" . $row['quantity'];
}

$response = [];

foreach ($orders as $order) {
    $response[] = [
        'id' => $order['id'],
        'userId' => $order['userId'],
        'totalAmount' => round($order['totalAmount'], 2),
        'status' => $order['status'],
        'orderDate' => $order['orderDate'],
        'itemsSummary' => implode(", ", $order['items'])
    ];
}

echo json_encode($response);
$conn->close();
