--CREATING THE DATABASE
CREATE DATABASE IF NOT EXISTS erp_db;
USE erp_db;

--CREATING THE students TABLE
 CREATE TABLE `students` (`user_id` bigint NOT NULL,`roll_no` varchar(64) NOT NULL,
 `degree` varchar(64) NOT NULL,`branch` varchar(64) NOT NULL,`term_year` int NOT NULL,
 `status` enum('ACTIVE','INACTIVE','ONLINE','OFFLINE','BLOCKED') DEFAULT 'OFFLINE',
 PRIMARY KEY (`user_id`),UNIQUE KEY `roll_no` (`roll_no`), CONSTRAINT `students_ibfk_1`
 FOREIGN KEY (`user_id`) REFERENCES `auth_db`.`user_auth` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE instructors TABLE
 CREATE TABLE `instructors` (`user_id` bigint NOT NULL,`instructor_name` varchar(100) NOT NULL,
`department` varchar(64) NOT NULL,`status` enum('ACTIVE','INACTIVE','ONLINE','OFFLINE','BLOCKED') NOT NULL DEFAULT 'OFFLINE',
PRIMARY KEY (`user_id`),CONSTRAINT `fk_instructor_user` FOREIGN KEY (`user_id`) REFERENCES `auth_db`.`user_auth` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE sections TABLE
 CREATE TABLE `sections` (`section_id` bigint NOT NULL AUTO_INCREMENT,`course_code` varchar(64) NOT NULL,
`instructor_id` bigint NOT NULL,`instructor_name` varchar(100) NOT NULL,`day` varchar(64) NOT NULL,
`timings` varchar(32) NOT NULL,`classroom` varchar(32) NOT NULL,`capacity` int NOT NULL,
`sem_no` int NOT NULL,`sem_season` enum('MONSOON','WINTER','SUMMER') NOT NULL,
`year` int NOT NULL,PRIMARY KEY (`section_id`),KEY `fk_sections_course` (`course_code`),
  KEY `fk_sections_instructor` (`instructor_id`),CONSTRAINT `fk_sections_course` FOREIGN KEY (`course_code`) REFERENCES `courses` (`course_code`),
  CONSTRAINT `fk_sections_instructor` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- CREATING THE settings TABLE
CREATE TABLE IF NOT EXISTS settings (k VARCHAR(64) PRIMARY KEY, v VARCHAR(256) NOT NULL);
-- insert the default row for maintenance mode
INSERT INTO settings (k, v) VALUES ('maintenance_on', 'false') ON DUPLICATE KEY UPDATE v = VALUES(v);

--keeping a drop date in the system so that the deadline is set
--example, for now, setting deadline to Nov 15 2025, 23:59:59
INSERT INTO settings(k,v) VALUES ('registration.finalDate', '2025-11-15T23:59:59') ON DUPLICATE KEY UPDATE v = VALUES(v);

--CREATING THE courses TABLE
CREATE TABLE `courses` (
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `course_code` varchar(32) NOT NULL,
  `credits` int NOT NULL,
  PRIMARY KEY (`course_id`),
  UNIQUE KEY `course_code` (`course_code`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE assessment scores TABLE
 CREATE TABLE `assessment_scores` (
  `score_id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `section_id` bigint NOT NULL,
  `assessment_name` varchar(100) NOT NULL,
  `enrollment_id` bigint NOT NULL,
  `ass_weightage` int NOT NULL,
  `ass_score` double NOT NULL,
  PRIMARY KEY (`score_id`),
  UNIQUE KEY `uq_enroll_assess` (`enrollment_id`,`assessment_name`),
  KEY `course_id` (`course_id`),
  KEY `section_id` (`section_id`),
  CONSTRAINT `assessment_scores_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `assessment_scores_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`),
  CONSTRAINT `assessment_scores_ibfk_3` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`enrollment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE enrollments TABLE
 CREATE TABLE `enrollments` (
  `enrollment_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `section_id` bigint NOT NULL,
  `e_status` enum('REGISTERED','DROPPED','COMPLETED') NOT NULL DEFAULT 'REGISTERED',
  `registered_when` datetime DEFAULT NULL,
  `dropped_when` datetime DEFAULT NULL,
  `completed_when` datetime DEFAULT NULL,
  PRIMARY KEY (`enrollment_id`),
  UNIQUE KEY `uq_student_section` (`student_id`,`section_id`),
  KEY `idx_section` (`section_id`),
  CONSTRAINT `fk_enr_section` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`),
  CONSTRAINT `fk_enr_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE final_grades TABLE
CREATE TABLE `final_grades` (
  `final_grade_id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `section_id` bigint NOT NULL,
  `enrollment_id` bigint NOT NULL,
  `grade_letter` enum('A+','A','A-','B','B-','C','C-','D','D-','F') NOT NULL,
  `course_cg` decimal(3,2) NOT NULL,
  PRIMARY KEY (`final_grade_id`),
  UNIQUE KEY `uq_enrollment` (`enrollment_id`),
  KEY `course_id` (`course_id`),
  KEY `section_id` (`section_id`),
  CONSTRAINT `final_grades_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `final_grades_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`),
  CONSTRAINT `final_grades_ibfk_3` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`enrollment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE grade_components TABLE
CREATE TABLE `grade_components` (
  `component_id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `section_id` bigint NOT NULL,
  `instructor_id` bigint NOT NULL,
  `assessment_name` varchar(100) NOT NULL,
  `weightage` int NOT NULL,
  PRIMARY KEY (`component_id`),
  UNIQUE KEY `uq_section_component` (`section_id`,`assessment_name`),
  KEY `course_id` (`course_id`),
  KEY `instructor_id` (`instructor_id`),
  CONSTRAINT `grade_components_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`),
  CONSTRAINT `grade_components_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`),
  CONSTRAINT `grade_components_ibfk_3` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci