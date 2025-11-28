--CREATE THE DATABASE;
CREATE DATABASE IF NOT EXISTS auth_db;
USE auth_db;

--CREATING TABLE user_auth
Create Table: CREATE TABLE `user_auth` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `role` enum('ADMIN','INSTRUCTOR','STUDENT') NOT NULL,
  `password_hash` varchar(100) NOT NULL,
  `status` enum('ACTIVE','INACTIVE','ONLINE','OFFLINE','BLOCKED') NOT NULL DEFAULT 'INACTIVE',
  `last_login` timestamp NULL DEFAULT NULL,
  `password_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `password_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING TABLE password_history
Create Table: CREATE TABLE `password_history` (
  `oldpass_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `old_hash` varchar(100) NOT NULL,
  `password_changed` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`oldpass_id`),
  KEY `fk_ph_user` (`user_id`),
  CONSTRAINT `fk_ph_user` FOREIGN KEY (`user_id`) REFERENCES `user_auth` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci