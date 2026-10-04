-- Sample data. Run schema.sql first, then this file.
-- Safe to re-run: empties every table and restarts the id counters at 1.

truncate authdb.user_auth,
         erp.final_grades, erp.assessment_scores, erp.grade_components,
         erp.enrollments, erp.sections, erp.courses,
         erp.instructors, erp.students, erp.settings
    restart identity cascade;

-- ---------- accounts ----------
-- Passwords: admin1 / admin123, inst1 / inst123, stu1 / stud123, stu2 / stud234
-- The 4 value lines are the output of auth/GeneratingHashes.java.
-- Inserted in this order they get user_id 1, 2, 3, 4.
insert into authdb.user_auth (username, role, password_hash) values
     ('admin1', 'ADMIN', '$2a$12$7xzFiEY1xuTgM1SGcdloJulF34B2NO8WA1tyIvlnnok.7CI5yAwrO'),
     ('inst1', 'INSTRUCTOR', '$2a$12$wTLtS0dg4/CpbcmKS/BmHOEtZCLTPYQiMsAdXOmqw6R0AKYBaMbOO'),
     ('stu1', 'STUDENT', '$2a$12$a2ThgpO./HQXWh/O/mCPbeG.N4MRyzSm878JEWkwkBH82v2UYosg6'),
     ('stu2', 'STUDENT', '$2a$12$.ceGMY62SkpyHCHEc2p.yeg32jcrUSC4CS02Pyh3oGZ7MapgCLIqW');

-- ---------- profiles: same id as the login row ----------
insert into erp.instructors (instructor_id, name, email, department) values
    (2, 'Meera Nair', 'inst1@univ.edu', 'CSE');

insert into erp.students (user_id, roll_no, name, email, program, year) values
    (3, '2024101', 'Aarav Sharma', 'stu1@univ.edu', 'B.Tech CSE', 2),
    (4, '2024102', 'Diya Patel',   'stu2@univ.edu', 'B.Tech ECE', 2);

-- ---------- courses ----------
insert into erp.courses (course_code, name, credits) values
    ('CSE101', 'Introduction to Programming', 4),
    ('CSE102', 'Data Structures and Algorithms', 4),
    ('CSE201', 'Advanced Programming', 4),
    ('MTH100', 'Linear Algebra', 4),
    ('ECE111', 'Digital Circuits', 4),
    ('SSH101', 'Introduction to Sociology', 2);

-- ---------- sections: all taught by inst1; they get section_id 1, 2, 3, 4 ----------
-- Section 3 has capacity 1 (for the "section full" test).
-- Section 4 is the course stu1 has already completed.
insert into erp.sections (course_code, instructor_id, classroom, day, timings, capacity, sem_no, sem_season, year) values
    ('CSE201', 2, 'C101', 'Monday',    '10:00-11:30', 60, 1, 'MONSOON', 2025),
    ('MTH100', 2, 'C102', 'Tuesday',   '11:30-13:00', 40, 1, 'MONSOON', 2025),
    ('ECE111', 2, 'C201', 'Wednesday', '14:00-15:30',  1, 1, 'MONSOON', 2025),
    ('CSE101', 2, 'C101', 'Thursday',  '09:00-10:30', 60, 1, 'MONSOON', 2025);

-- ---------- grade components: the same 4 for every section, adding to 100 ----------
insert into erp.grade_components (section_id, assessment_name, weightage)
select s.section_id, c.assessment_name, c.weightage
from erp.sections s
cross join (values ('Quizzes', 10), ('Assignments', 20), ('Midsem', 30), ('Endsem', 40))
    as c (assessment_name, weightage);

-- ---------- enrollments: they get enrollment_id 1, 2, 3, 4 ----------
insert into erp.enrollments (student_id, section_id, e_status, completed_when) values
    (3, 1, 'REGISTERED', null),
    (3, 2, 'REGISTERED', null),
    (3, 4, 'COMPLETED',  now()),
    (4, 3, 'REGISTERED', null);

-- ---------- scores ----------
insert into erp.assessment_scores (enrollment_id, assessment_name, ass_score) values
    (1, 'Quizzes', 80), (1, 'Assignments', 75), (1, 'Midsem', 68),
    (3, 'Quizzes', 85), (3, 'Assignments', 90), (3, 'Midsem', 78), (3, 'Endsem', 82);

-- Enrollment 3: 85*0.10 + 90*0.20 + 78*0.30 + 82*0.40 = 82.70 -> A, CG 8.27
insert into erp.final_grades (enrollment_id, grade_letter, course_cg) values
    (3, 'A', 8.27);

-- ---------- settings ----------
insert into erp.settings (k, v) values
    ('maintenance_on', 'false'),
    ('semester_deadline', '2026-12-31');