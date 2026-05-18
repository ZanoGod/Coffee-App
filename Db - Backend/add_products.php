<?php
header("Content-Type: application/json");
include "db.php";

$name = $_POST['name'];
$price = $_POST['price'];
$rating = $_POST['rating'];
$image = $_POST['image'];

$sql = "INSERT INTO products (name, price, rating, image)
        VALUES ('$name', '$price', '$rating', '$image')";

if ($conn->query($sql)) {
    echo json_encode(["status" => "success"]);
} else {
    echo json_encode(["status" => "error"]);
}
?>
