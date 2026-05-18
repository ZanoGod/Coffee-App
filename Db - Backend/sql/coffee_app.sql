-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Feb 16, 2026 at 07:26 AM
-- Server version: 8.0.44-35
-- PHP Version: 8.3.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vadbrqte_coffee_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
  `id` int NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` varchar(50) COLLATE utf8mb4_general_ci DEFAULT 'Admin',
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`id`, `name`, `username`, `role`, `password`, `created_at`) VALUES
(1, 'ADMIN', 'admin', 'Admin', '$2y$10$tNElKx5a1AVm6IrR79F3n.smOx6K2DhgXwQoN5OM1Qx6/VbGH.f6q', '2026-01-20 04:49:46'),
(2, 'ZANO', 'zano', 'Admin', '$2y$10$P0IbTRbAiRv1vqY90SJyp.Y1tAp.y9V8peyQEM9TkHMFXgvv7HcdG', '0000-00-00 00:00:00'),
(3, 'John', 'john', 'Admin', '$2y$10$q09xRUw1hFdeJ.tUtPfAQOzT0VV0f8jqVH1S60Nf9CS/.POBu6ZW2', '2026-01-22 13:45:56'),
(4, 'David', 'david', 'Super Admin', '$2y$10$MrN9gDdTmxZEmmD04zwWVOCmUqj2AbWScs2rsge6v15oKaHFvmKrm', '2026-01-22 13:47:20'),
(5, 'Jack', 'jack', 'Editor', '$2y$10$9c2zSaJqb3vQQkDc5YIW3eGo/nQ2lgk4wWy5h.Cn12kqzZhYBbjvS', '2026-01-22 13:48:00');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `product_name` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--

CREATE TABLE `favorites` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `product_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `favorites`
--

INSERT INTO `favorites` (`id`, `user_id`, `product_id`, `product_name`, `image_url`, `price`, `created_at`) VALUES
(83, 1, 22, NULL, NULL, NULL, '2026-01-20 09:44:30'),
(84, 1, 21, NULL, NULL, NULL, '2026-01-20 09:50:57'),
(85, 1, 38, NULL, NULL, NULL, '2026-01-20 09:50:59'),
(86, 1, 23, NULL, NULL, NULL, '2026-01-20 09:51:31'),
(104, 3, 11, NULL, NULL, NULL, '2026-01-22 15:37:12'),
(116, 3, 18, NULL, NULL, NULL, '2026-01-22 15:42:45'),
(117, 3, 22, NULL, NULL, NULL, '2026-01-22 15:42:47'),
(118, 3, 24, NULL, NULL, NULL, '2026-01-22 15:42:48'),
(119, 3, 28, NULL, NULL, NULL, '2026-01-22 15:42:49'),
(120, 3, 26, NULL, NULL, NULL, '2026-01-22 15:42:51'),
(121, 3, 19, NULL, NULL, NULL, '2026-01-22 15:42:55'),
(122, 3, 13, NULL, NULL, NULL, '2026-01-22 15:57:53'),
(123, 3, 17, NULL, NULL, NULL, '2026-01-23 09:25:23'),
(124, 3, 3, NULL, NULL, NULL, '2026-01-23 10:41:44'),
(127, 8, 3, NULL, NULL, NULL, '2026-02-15 14:43:46');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `product_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_general_ci DEFAULT 'pending',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `quantity` int NOT NULL DEFAULT '1',
  `total_price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `product_id`, `product_name`, `price`, `status`, `created_at`, `quantity`, `total_price`) VALUES
(17, 3, 5, 'Choco Elephant Delight', 5.50, 'completed', '2026-01-22 05:36:23', 1, 5.50),
(19, 3, 19, 'Ice Caramel Matcha Latte', 5.40, 'completed', '2026-01-22 15:05:19', 1, 5.40),
(21, 3, 13, 'Iced Brown Sugar Espresso', 5.00, 'completed', '2026-01-22 15:23:42', 1, 5.00),
(23, 3, 15, 'Iced Caramel Macchiato', 5.20, 'completed', '2026-01-22 15:23:42', 1, 5.20),
(24, 3, 11, 'Iced Caramel Latte', 4.80, 'completed', '2026-01-22 15:23:42', 1, 4.80),
(25, 3, 12, 'Iced Latte', 4.50, 'completed', '2026-01-22 15:23:42', 1, 4.50),
(26, 3, 20, 'Ice Signature Coffee', 3.80, 'completed', '2026-01-22 15:23:42', 1, 3.80),
(27, 3, 3, 'Latte', 4.50, 'completed', '2026-01-22 16:39:45', 2, 9.00),
(28, 3, 13, 'Iced Brown Sugar Espresso', 5.00, 'completed', '2026-01-22 16:39:45', 1, 5.00),
(29, 3, 16, 'Ice Matcha Latte', 4.90, 'completed', '2026-01-22 16:39:45', 1, 4.90),
(30, 3, 24, 'Cheese Cake', 5.80, 'completed', '2026-01-22 16:39:45', 1, 5.80),
(31, 3, 3, 'Latte', 4.50, 'completed', '2026-01-23 08:54:01', 1, 4.50),
(32, 3, 5, 'Choco Elephant Delight', 5.50, 'completed', '2026-01-23 08:54:01', 1, 5.50),
(33, 3, 29, 'Chocolate Cake ', 5.00, 'completed', '2026-01-23 08:54:01', 1, 5.00),
(34, 3, 3, 'Latte', 4.50, 'completed', '2026-01-23 10:01:20', 1, 4.50),
(35, 3, 21, 'Strawberry Shortcake', 6.50, 'completed', '2026-02-02 13:12:00', 1, 6.50),
(36, 3, 25, 'Pineapple Upside Down Cake', 6.20, 'completed', '2026-02-02 13:12:00', 1, 6.20),
(37, 3, 13, 'Iced Brown Sugar Espresso', 5.00, 'completed', '2026-02-08 10:37:31', 1, 5.00),
(38, 3, 12, 'Iced Latte', 4.50, 'completed', '2026-02-08 10:37:31', 1, 4.50),
(39, 3, 24, 'Cheese Cake', 5.80, 'completed', '2026-02-08 10:37:31', 1, 5.80),
(40, 7, 3, 'Latte', 4.50, 'completed', '2026-02-11 17:35:44', 1, 4.50),
(41, 7, 7, 'Double Love Mocha', 5.10, 'completed', '2026-02-11 17:35:44', 1, 5.10),
(42, 7, 12, 'Iced Latte', 4.50, 'completed', '2026-02-11 17:35:44', 1, 4.50),
(43, 7, 20, 'Ice Signature Coffee', 3.80, 'completed', '2026-02-11 17:35:44', 1, 3.80),
(44, 7, 24, 'Cheese Cake', 5.80, 'completed', '2026-02-11 17:35:44', 1, 5.80),
(45, 8, 1, 'Cappuccino', 4.00, 'completed', '2026-02-15 14:44:15', 1, 4.00),
(46, 8, 3, 'Latte', 4.50, 'completed', '2026-02-15 14:44:15', 1, 4.50),
(47, 8, 18, 'Ice Strawberry Matcha', 5.30, 'completed', '2026-02-15 14:44:15', 1, 5.30),
(48, 8, 24, 'Cheese Cake', 5.80, 'completed', '2026-02-15 14:44:15', 1, 5.80);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `category` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `rating` double DEFAULT NULL,
  `image` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `price`, `category`, `rating`, `image`) VALUES
(1, 'Cappuccino', 4, 'hot', 5, 'cappuccino.png'),
(2, 'Americano', 3.75, 'hot', 3.6, 'americano.png'),
(4, 'Latte Heart Spiral', 4.9, 'hot', 4.8, 'latte_heart_spiral.png'),
(5, 'Choco Elephant Delight', 5.5, 'hot', 4.9, 'choco_elephant_delight.png'),
(6, 'Vanilla Dust Latte', 4.6, 'hot', 4.6, 'vanilla_dust_latte.png'),
(7, 'Double Love Mocha', 5.1, 'hot', 4.8, 'double_love_mocha.png'),
(8, 'Signature Rosetta Brew', 5.3, 'hot', 4.9, 'signature_rosetta_brew.png'),
(9, 'Iced Americano', 4.2, 'ice', 4.5, 'ice-americano.jpg'),
(10, 'Iced Mocha', 4.6, 'ice', 4.6, 'iced-mocha.jpg'),
(11, 'Iced Caramel Latte', 4.8, 'ice', 4.7, 'iced_caramel_latte.jpg'),
(12, 'Iced Latte', 4.5, 'ice', 4.6, 'iced_caffe_latte.jpg'),
(13, 'Iced Brown Sugar Espresso', 5, 'ice', 4.8, 'iced_brownsugar_espresso.jpg'),
(14, 'Iced Shaken Espresso', 4.9, 'ice', 4.7, 'Iced Shaken Espresso.png'),
(15, 'Iced Caramel Macchiato', 5.2, 'ice', 4.8, 'Iced-Caramel-Macchiato.jpg'),
(16, 'Ice Matcha Latte', 4.9, 'matcha', 4.8, 'ice_matcha_latte.jpg'),
(17, 'Ice Vanilla Matcha', 5.1, 'matcha', 4.7, 'ice_vanilla_matcha.jpg'),
(18, 'Ice Strawberry Matcha', 5.3, 'matcha', 4.9, 'ice_strawberry_matcha.jpg'),
(19, 'Ice Caramel Matcha Latte', 5.4, 'matcha', 4.9, 'ice_caramel_matcha_latte.jpg'),
(20, 'Ice Signature Coffee', 3.8, 'ice', 4.4, 'iced_coffee.jpg'),
(21, 'Strawberry Shortcake', 6.5, 'cake', 4.8, 'strawberry-shortcake.png'),
(22, 'Tiramisu', 6, 'cake', 4.9, 'tiramisu.jpg'),
(23, 'Fluted Tube Pan Cake', 5.5, 'cake', 4.7, 'bundt-cake.jpg'),
(24, 'Cheese Cake', 5.8, 'cake', 4.9, 'cheesecake.jpg'),
(25, 'Pineapple Upside Down Cake', 6.2, 'cake', 4.6, 'pineapple-upside-down-cake.jpg'),
(26, 'Red Velvet Cake', 6, 'cake', 4.8, 'red-velvet-cake.png'),
(27, 'Carrot Cake', 5.5, 'cake', 4.7, 'carrot-cake.jpg'),
(28, 'Black Forest Cake', 6.3, 'cake', 4.9, 'black-forest-cake.jpg'),
(29, 'Chocolate Cake ', 5, 'cake', 4.8, 'chocolate_cake_slice.png'),
(30, 'Latte ', 5.5, 'Hot', 4.9, 'latte.png');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `address` text COLLATE utf8mb4_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `created_at`, `address`) VALUES
(1, 'testuser', 'test@example.com', '$2y$10$jMXmwdif1gX1mIgL2fzIaew3KXasLCyOWTDU13x3Llri/lII14L2y', '2026-01-15 03:57:34', NULL),
(2, 'Johnn', 'test11@gmail.com', '$2y$10$66gGAY/gQ0HwfiIwy3T2Geozh2.QExjk1cAFiZlVn0Md6VZSQS4r2', '2026-01-20 10:40:35', NULL),
(3, 'Zano', 'zin@gmail.com', '$2y$10$qGD7568wSOj6S06W8.Btuu/ePaW.i/Bpwcz.f/ec7uI3SxJYpSlzW', '2026-01-20 10:59:10', NULL),
(5, 'Zin Waiyan', 'zin1@gmail.com', '$2y$10$0gOMqehnKTLkqnSC4clOg.cVr5pD1xulpBBI4wR.S576.CO88IHbu', '2026-01-20 15:17:16', NULL),
(6, 'test', 'sfasdf@gamil.com', '$2y$10$x8HAirvSVvGJF7wbpDIikeSMHfcmuROOEo6J0tCVp/jQxFKenUtvy', '2026-01-20 15:20:38', NULL),
(7, 'Zin', 'zin123@gmail.com', '$2y$12$w9/kb3RONM/3gvEwT.28zeza/hmGqibgFS79ydUgB9w5BKfov662y', '2026-02-11 16:10:18', NULL),
(8, 'Zin Wai Yan Oo', 'zin12345@gmail.com', '$2y$12$OfXeBebuMT38EDnfkpL/dOxU5q/owVGsmhE/lPqATwWLDXW8U4Svm', '2026-02-15 14:42:35', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`product_id`);

--
-- Indexes for table `favorites`
--
ALTER TABLE `favorites`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_favorite` (`user_id`,`product_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=409;

--
-- AUTO_INCREMENT for table `favorites`
--
ALTER TABLE `favorites`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=130;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
