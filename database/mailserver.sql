-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 27, 2022 at 04:12 PM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mailserver`
--

-- --------------------------------------------------------

--
-- Table structure for table `attachments`
--

CREATE TABLE `attachments` (
  `id` varchar(36) NOT NULL,
  `email_id` varchar(36) NOT NULL,
  `filepath` varchar(255) NOT NULL,
  `active` tinyint(4) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `attachments`
--

INSERT INTO `attachments` (`id`, `email_id`, `filepath`, `active`, `created_at`) VALUES
('a732e32f-0e2f-4e7b-a001-6712e80837d5', '9b36a229-e184-4fb4-bc63-8a505163a3fc', 'AnhNenWin10 (1).jpg', 1, '2022-11-25 23:51:57'),
('cadb3fac-a4bd-4986-8895-31174aaf86cf', '06560638-3d3e-4847-af1d-918d3c7a70d9', 'MAIL SERVER.docx', 1, '2022-11-27 14:18:50'),
('f8881af5-b12e-4f9a-9c19-b6173807e71e', 'caa566b1-c58a-4514-8582-da716c29fd0d', 'DO AN HTTTDN - HK 1 - NH 2022-2023.docx', 1, '2022-11-27 14:12:39');

-- --------------------------------------------------------

--
-- Table structure for table `emails`
--

CREATE TABLE `emails` (
  `id` varchar(36) NOT NULL,
  `sender_email` varchar(36) NOT NULL,
  `receiver_email` varchar(36) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `CC` varchar(255) NOT NULL,
  `BCC` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `status` varchar(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `size` float DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `emails`
--

INSERT INTO `emails` (`id`, `sender_email`, `receiver_email`, `subject`, `CC`, `BCC`, `content`, `status`, `created_at`, `deleted_at`, `size`) VALUES
('026fdae5-a4e8-476a-a81f-185c7ac072df', 'kietv@nhom50.com', 'thanhson@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:21', NULL, 0),
('02a3a91c-db06-4bbd-93cc-522613e1c757', 'kietv@nhom50.com', 'test2@nhom50.com', 'Xin chào', 'test@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      H&#7919;u Kh&#432;&#417;ng\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:26:23', NULL, 0),
('06560638-3d3e-4847-af1d-918d3c7a70d9', 'kietv@nhom50.com', 'thanhson@nhom50.com', 'Kiệt chào Sonw', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      Ki&#7879;t ch&#224;o s&#417;n lanaf n&#7919;a \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'READ', '2022-11-27 14:18:50', NULL, 0),
('0ae096c9-fcb8-4ade-a08f-52dae5db48c9', 'admin@nhom50.com', 'kietv@nhom50.com', 'Đây là email test admin guiwr', '', '', 'Chào các bạnnnn', 'DELETED', '2022-11-26 02:02:53', NULL, 0),
('0bd42ee4-ba87-44e1-8e44-200ed6dd1b78', 'test3@nhom50.com', 'test2@nhom50.com', 'Xin chào các bạn', 'kietv@nhom50.com,kietvo@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      &#272;&#226;y l&#224; final test \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-25 23:43:52', NULL, 0),
('1596b905-cfa0-4def-b94c-4bea07907a94', 'thanhson@nhom50.com', 'kietv@nhom50.com', 'Xin chào', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      Ch&#224;o Ki&#7879;t \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:22:00', NULL, 0),
('1a9bc9ef-fd0c-4b54-af77-6ad63755324f', 'admin@nhom50.com', 'test2@nhom50.com', 'Đây là email test admin guiwr', '', '', 'Chào các bạnnnn', 'INBOX', '2022-11-26 02:02:53', NULL, 0),
('1f4d6944-6f0e-4727-a9f5-85d5a3a2b293', 'test3@nhom50.com', 'test@nhom50.com', 'schedule 2', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      th&#7853;t kh&#244;ng nh&#7881; \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-25 23:51:00', NULL, 0),
('1fb1adef-841e-44c6-bcb1-f16eea7fcb25', 'test3@nhom50.com', 'test@nhom50.com', 'schedule 2', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      th&#7853;t kh&#244;ng nh&#7881; \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'READ', '2022-11-25 23:49:23', NULL, 0),
('22f70f42-75a5-46c8-85d7-dbff965c67d1', 'kietv@nhom50.com', 'test@nhom50.com', 'Xin chào', 'test@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      H&#7919;u Kh&#432;&#417;ng\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:26:23', NULL, 0),
('2380537c-bb9b-4139-8769-e08914669096', 'kietv@nhom50.com', 'test2@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:23', NULL, 0),
('385fdb27-9c30-4b01-acce-6796ea7c0712', 'admin@nhom50.com', 'test@nhom50.com', 'Đây là email test admin guiwr', '', '', 'Chào các bạnnnn', 'INBOX', '2022-11-26 02:02:53', NULL, 0),
('39118842-037e-4bbc-af0c-e93a1ec0dba0', 'test3@nhom50.com', 'test@nhom50.com', 'schedule 2', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      th&#7853;t kh&#244;ng nh&#7881; \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-25 23:52:11', NULL, 0),
('49f3d238-b8d6-48b5-93db-f75eae84b2d8', 'test3@nhom50.com', 'test@nhom50.com', 'Xin chào các bạn', 'kietv@nhom50.com,kietvo@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      &#272;&#226;y l&#224; final test \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'READ', '2022-11-25 23:43:52', NULL, 0),
('4f0c7f3e-e371-456e-acf7-2ac397196fcb', 'test1@nhom50.com', 'test@nhom50.com', 'Đaya là schedule', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'READ', '2022-11-25 23:50:00', NULL, 0),
('5948fb8b-a62f-4934-bfe0-2338ae52b994', 'kietv@nhom50.com', 'test1@nhom50.com', 'Xin chào', 'test@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      H&#7919;u Kh&#432;&#417;ng\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:26:23', NULL, 0),
('5e9f56ba-5063-415e-8bfa-b979fc2599c8', 'admin@nhom50.com', 'kietvo@nhom50.com', 'Đây là email test admin guiwr', '', '', 'Chào các bạnnnn', 'INBOX', '2022-11-26 02:02:53', NULL, 0),
('60ff60b0-ffc2-4ea7-b469-3832983e9237', 'kietv@nhom50.com', 'test@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:23', NULL, 0),
('79ea35e4-ee65-4cfe-9839-a9186869ab9e', 'kietv@nhom50.com', 'thanhson@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'READ', '2022-11-27 14:24:23', NULL, 0),
('9b36a229-e184-4fb4-bc63-8a505163a3fc', 'test3@nhom50.com', 'test@nhom50.com', 'schedule 2', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      th&#7853;t kh&#244;ng nh&#7881; \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-25 23:51:57', NULL, 0),
('9c26ad4c-f6ad-4c04-9d13-cd26d351b906', 'kietv@nhom50.com', 'test2@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:21', NULL, 0),
('b4c9e20a-3adf-4e5e-9267-46294a1b3a86', 'test3@nhom50.com', 'kietvo@nhom50.com', 'Xin chào các bạn', 'kietv@nhom50.com,kietvo@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      &#272;&#226;y l&#224; final test \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-25 23:43:52', NULL, 0),
('b90d0e26-e526-477d-bf39-488e21adcda5', 'kietv@nhom50.com', 'kietvo@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:23', NULL, 0),
('c24c8878-9713-4dd2-ab1a-4c757f5feebd', 'thanhson@nhom50.com', 'kietv@nhom50.com', 'Xin chào', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      Ch&#224;o Ki&#7879;t \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'DELETED', '2022-11-27 14:22:00', NULL, 0),
('c5720596-1e60-46ca-8d4d-97e1784c45a7', 'kietv@nhom50.com', 'test@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:23', NULL, 0),
('caa566b1-c58a-4514-8582-da716c29fd0d', 'kietv@nhom50.com', 'annguyen@nhom50.com', 'Xin chào An', '', '', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      Ch&#224;o H&#7919;u An\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'READ', '2022-11-27 14:12:39', NULL, 0),
('ce54642e-67b5-4ebf-860b-9cc3eddd1453', 'kietv@nhom50.com', 'test@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:21', NULL, 0),
('cfc1f3c0-5d41-4e5a-ba19-04429f4a5f22', 'test3@nhom50.com', 'test1@nhom50.com', 'Xin chào các bạn', 'kietv@nhom50.com,kietvo@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      &#272;&#226;y l&#224; final test \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'READ', '2022-11-25 23:43:52', NULL, 0),
('d7e0e4e9-f377-481a-819c-8f5af5ab9691', 'kietv@nhom50.com', 'test@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:21', NULL, 0),
('e32515bb-370b-420a-bd51-2a08ac2c6010', 'kietv@nhom50.com', 'kietvo@nhom50.com', 'Xin chào', 'test@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      H&#7919;u Kh&#432;&#417;ng\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:26:23', NULL, 0),
('e4ee9340-1909-44f3-b3fd-eea297a33705', 'admin@nhom50.com', 'test3@nhom50.com', 'Đây là email test admin guiwr', '', '', 'Chào các bạnnnn', 'INBOX', '2022-11-26 02:02:53', NULL, 0),
('ee7a7cc9-4819-4afc-8625-1a7a11dab0b3', 'admin@nhom50.com', 'test1@nhom50.com', 'Đây là email test admin guiwr', '', '', 'Chào các bạnnnn', 'INBOX', '2022-11-26 02:02:53', NULL, 0),
('f3f684bf-ea5f-44cf-99ef-59e046503010', 'test3@nhom50.com', 'kietv@nhom50.com', 'Xin chào các bạn', 'kietv@nhom50.com,kietvo@nhom50.com', 'test1@nhom50.com,test2@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      &#272;&#226;y l&#224; final test \r\n    </p>\r\n  </body>\r\n</html>\r\n', 'SPAM', '2022-11-25 23:43:52', NULL, 0),
('f809baa2-fd17-4494-8dae-eb5b3d989366', 'admin@nhom50.com', 'admin@nhom50.com', 'Đây là email test admin guiwr', '', '', 'Chào các bạnnnn', 'INBOX', '2022-11-26 02:02:53', NULL, 0),
('f9d590c5-75ac-4c32-9b80-fd5a3e479002', 'kietv@nhom50.com', 'kietvo@nhom50.com', 'dsda', 'test@nhom50.com,test2@nhom50.com', 'thanhson@nhom50.com,kietvo@nhom50.com', '<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      ahsdhashdhah\r\n    </p>\r\n  </body>\r\n</html>\r\n', 'INBOX', '2022-11-27 14:24:21', NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `email` varchar(255) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `password` varchar(32) NOT NULL,
  `is_online` tinyint(4) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `storage` float NOT NULL DEFAULT 500,
  `status` varchar(36) NOT NULL,
  `storage_used` float NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`email`, `first_name`, `last_name`, `password`, `is_online`, `created_at`, `updated_at`, `storage`, `status`, `storage_used`) VALUES
('admin@nhom50.com', 'ADMIN', '', '25d55ad283aa400af464c76d713c07ad', 0, '2022-11-26 02:01:59', '2022-11-26 02:01:59', 9999, 'UNLOCK', 0),
('annguyen@nhom50.com', 'Nguyễn', 'Hữu An', '25d55ad283aa400af464c76d713c07ad', 0, '2022-11-27 14:07:58', '2022-11-27 14:07:58', 500, 'UNLOCK', 0),
('kietv@nhom50.com', 'kiet', 'vo', '25d55ad283aa400af464c76d713c07ad', 0, '2022-11-15 06:07:06', '2022-11-15 06:07:06', 199.954, '', 0),
('kietvo@nhom50.com', 'Kiệt', 'Võ', '25d55ad283aa400af464c76d713c07ad', 1, '2022-11-15 06:05:33', '2022-11-15 06:05:33', 200, 'UNLOCK', 0),
('test1@nhom50.com', 'test', 'tes1', '25d55ad283aa400af464c76d713c07ad', 0, '2022-11-22 14:27:59', '2022-11-22 14:27:59', 200, 'UNLOCK', 0),
('test2@nhom50.com', 'test2', 'qưe', '25d55ad283aa400af464c76d713c07ad', 0, '2022-11-22 14:28:25', '2022-11-22 14:28:25', 200, 'UNLOCK', 0),
('test3@nhom50.com', 'Test', '3', '25d55ad283aa400af464c76d713c07ad', 0, '2022-11-25 23:41:20', '2022-11-25 23:41:20', 199.935, 'UNLOCK', 0),
('test@nhom50.com', 'Hữu ', 'Khương', '25d55ad283aa400af464c76d713c07ad', 0, '2022-11-18 10:26:19', '2022-11-18 10:26:19', 200, 'UNLOCK', 0),
('thanhson@nhom50.com', 'Lê Thái', 'Thanh Sơn', '25d55ad283aa400af464c76d713c07ad', 1, '2022-11-27 14:17:16', '2022-11-27 14:17:16', 500, 'BLOCK', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attachments`
--
ALTER TABLE `attachments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `email_id` (`email_id`);

--
-- Indexes for table `emails`
--
ALTER TABLE `emails`
  ADD PRIMARY KEY (`id`),
  ADD KEY `CC` (`CC`),
  ADD KEY `sender_id` (`sender_email`),
  ADD KEY `receiver_id` (`receiver_email`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`email`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
