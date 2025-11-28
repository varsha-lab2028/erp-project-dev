--CREATING THE DATABASE
CREATE DATABASE IF NOT EXISTS erp_db;
USE erp_db;

--CREATING THE students TABLE
CREATE TABLE `students` (
   `user_id` bigint NOT NULL,
   `roll_no` varchar(20) DEFAULT NULL,
   `name` varchar(100) DEFAULT NULL,
   `email` varchar(100) DEFAULT NULL,
   `cgpa` decimal(4,2) DEFAULT '0.00',
   PRIMARY KEY (`user_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE instructors TABLE
CREATE TABLE `instructors` (
  `instructor_id` bigint NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`instructor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE sections TABLE
CREATE TABLE `sections` (
  `section_id` bigint NOT NULL AUTO_INCREMENT,
  `course_code` varchar(20) DEFAULT NULL,
  `instructor_id` bigint DEFAULT NULL,
  `classroom` varchar(20) DEFAULT NULL,
  `day` varchar(20) DEFAULT NULL,
  `timings` varchar(50) DEFAULT NULL,
  `capacity` int DEFAULT NULL,
  `sem_no` int DEFAULT NULL,
  `sem_season` varchar(20) DEFAULT NULL,
  `year` int DEFAULT NULL,
  PRIMARY KEY (`section_id`),
  KEY `course_code` (`course_code`),
  KEY `instructor_id` (`instructor_id`),
  CONSTRAINT `sections_ibfk_1` FOREIGN KEY (`course_code`) REFERENCES `courses` (`course_code`),
  CONSTRAINT `sections_ibfk_2` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- CREATING THE settings TABLE
CREATE TABLE IF NOT EXISTS settings (k VARCHAR(64) PRIMARY KEY, v VARCHAR(256) NOT NULL);

--CREATING THE courses TABLE
 CREATE TABLE `courses` (
  `course_code` varchar(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `credits` int DEFAULT NULL,
  PRIMARY KEY (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE assessment scores TABLE
CREATE TABLE `assessment_scores` (
  `score_id` bigint NOT NULL AUTO_INCREMENT,
  `enrollment_id` bigint DEFAULT NULL,
  `section_id` bigint DEFAULT NULL,
  `student_id` bigint DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `assessment_name` varchar(50) DEFAULT NULL,
  `ass_score` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`score_id`),
  KEY `enrollment_id` (`enrollment_id`),
  CONSTRAINT `assessment_scores_ibfk_1` FOREIGN KEY (`enrollment_id`) REFERENCES `enrollments` (`enrollment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

--CREATING THE enrollments TABLE
 CREATE TABLE `enrollments` (
  `enrollment_id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint DEFAULT NULL,
  `section_id` bigint DEFAULT NULL,
  `e_status` varchar(20) DEFAULT 'REGISTERED',
  `registered_when` datetime DEFAULT CURRENT_TIMESTAMP,
  `dropped_when` datetime DEFAULT NULL,
  `completed_when` datetime DEFAULT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `student_id` (`student_id`),
  KEY `section_id` (`section_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`user_id`),
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

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
  `component_id` bigint NOT NULL AUTO_INCREMENT,
  `section_id` bigint DEFAULT NULL,
  `assessment_name` varchar(50) DEFAULT NULL,
  `weightage` int DEFAULT NULL,
  PRIMARY KEY (`component_id`),
  KEY `section_id` (`section_id`),
  CONSTRAINT `grade_components_ibfk_1` FOREIGN KEY (`section_id`) REFERENCES `sections` (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci