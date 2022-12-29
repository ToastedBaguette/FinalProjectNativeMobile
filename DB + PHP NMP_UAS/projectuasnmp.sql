-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 29, 2022 at 12:24 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projectuasnmp`
--

-- --------------------------------------------------------

--
-- Table structure for table `memes`
--

CREATE TABLE `memes` (
  `idmemes` int(11) NOT NULL,
  `image_url` varchar(100) NOT NULL,
  `top_text` varchar(45) NOT NULL,
  `bottom_text` varchar(45) NOT NULL,
  `num_likes` int(11) NOT NULL,
  `users_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `memes`
--

INSERT INTO `memes` (`idmemes`, `image_url`, `top_text`, `bottom_text`, `num_likes`, `users_id`) VALUES
(1, 'https://a.pinatafarm.com/2000x1124/b177c50844/guy-confused.jpg/m/522x0', 'WHAT', 'WHAT??', 100, 1);

-- --------------------------------------------------------

--
-- Table structure for table `meme_comments`
--

CREATE TABLE `meme_comments` (
  `idmeme_comments` int(11) NOT NULL,
  `users_id` int(11) NOT NULL,
  `memes_id` int(11) NOT NULL,
  `content` varchar(45) NOT NULL,
  `publish_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `idusers` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `date_regist` datetime NOT NULL,
  `avatar_img` varchar(100) NOT NULL,
  `privacy_setting` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`idusers`, `username`, `first_name`, `last_name`, `password`, `date_regist`, `avatar_img`, `privacy_setting`) VALUES
(1, 'johndoe', 'John', 'Doe', '1234', '2022-12-29 12:20:09', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDr1i25jqVHRFv8eBDwljZqXTSBVO60GRsiQ&usqp=CAU', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `memes`
--
ALTER TABLE `memes`
  ADD PRIMARY KEY (`idmemes`),
  ADD KEY `fk_memes_users_idx` (`users_id`);

--
-- Indexes for table `meme_comments`
--
ALTER TABLE `meme_comments`
  ADD PRIMARY KEY (`idmeme_comments`),
  ADD KEY `fk_meme_comments_users1_idx` (`users_id`),
  ADD KEY `fk_meme_comments_memes1_idx` (`memes_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`idusers`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `memes`
--
ALTER TABLE `memes`
  MODIFY `idmemes` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `meme_comments`
--
ALTER TABLE `meme_comments`
  MODIFY `idmeme_comments` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `idusers` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `memes`
--
ALTER TABLE `memes`
  ADD CONSTRAINT `fk_memes_users` FOREIGN KEY (`users_id`) REFERENCES `users` (`idusers`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `meme_comments`
--
ALTER TABLE `meme_comments`
  ADD CONSTRAINT `fk_meme_comments_memes1` FOREIGN KEY (`memes_id`) REFERENCES `memes` (`idmemes`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_meme_comments_users1` FOREIGN KEY (`users_id`) REFERENCES `users` (`idusers`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
