<?php
// $host = "localhost";
// $user = "root";
// $password = "";
// $database = "coffee_app";

// $conn = new mysqli($host, $user, $password, $database);

// if ($conn->connect_error) {
//     die(json_encode([
//         "status" => "error",
//         "message" => "Database connection failed"
//     ]));
// }

// Create a new MySQLi connection
// localhost → database server (correct for cPanel shared hosting)
// vadbrqte_zano → database username
// Zano@128078 → database password
// vadbrqte_coffee_app → database name

$conn = new mysqli(
    "localhost",
    "vadbrqte_zano",
    "Zano@128078",
    "vadbrqte_coffee_app"
);

// Check if the connection failed
if ($conn->connect_error) {
    // Stop script execution and show error message
    die("Database connection failed: " . $conn->connect_error);
}

// If no error, database connection is successful
