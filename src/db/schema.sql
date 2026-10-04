-- ERP database schema (Postgres / Supabase)
-- Safe to re-run: drops both schemas and rebuilds them from zero.
-- Run this file first, then seed.sql.

drop schema if exists authdb cascade;
drop schema if exists erp cascade;
create schema authdb;
create schema erp;

-- ---------- authdb: logins only ----------

create table authdb.user_auth (
    user_id       bigint generated always as identity primary key,
    username      varchar(64)  not null unique,
    role          varchar(20)  not null check (role in ('ADMIN', 'INSTRUCTOR', 'STUDENT')),
    password_hash varchar(100) not null,
    status        varchar(20)  not null default 'ACTIVE' check (status in ('ACTIVE', 'BLOCKED')),
    last_login    timestamp
);

-- ---------- erp: everything else. No password column anywhere. ----------

-- user_id / instructor_id hold the same number as authdb.user_auth.user_id
create table erp.students (
    user_id bigint primary key,
    roll_no varchar(20) unique,
    name    varchar(100) not null,
    email   varchar(100),
    program varchar(50),
    year    int
);

create table erp.instructors (
    instructor_id bigint primary key,
    name          varchar(100) not null,
    email         varchar(100),
    department    varchar(50)
);

create table erp.courses (
    course_code varchar(20) primary key,
    name        varchar(100) not null,
    credits     int not null check (credits > 0)
);

create table erp.sections (
    section_id    bigint generated always as identity primary key,
    course_code   varchar(20) not null references erp.courses (course_code),
    instructor_id bigint      not null references erp.instructors (instructor_id),
    classroom     varchar(20),
    day           varchar(20) check (day in ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')),
    timings       varchar(50),
    capacity      int not null check (capacity > 0),
    sem_no        int,
    sem_season    varchar(20) check (sem_season in ('MONSOON', 'WINTER', 'SUMMER')),
    year          int
);

create table erp.enrollments (
    enrollment_id   bigint generated always as identity primary key,
    student_id      bigint not null references erp.students (user_id),
    section_id      bigint not null references erp.sections (section_id),
    e_status        varchar(20) not null default 'REGISTERED'
                    check (e_status in ('REGISTERED', 'DROPPED', 'COMPLETED')),
    registered_when timestamp default now(),
    dropped_when    timestamp,
    completed_when  timestamp
);

-- A student can hold only one REGISTERED row per section.
-- Dropped rows are ignored, so drop-then-register-again still works.
create unique index one_active_enrollment
    on erp.enrollments (student_id, section_id)
    where e_status = 'REGISTERED';

create table erp.grade_components (
    component_id    bigint generated always as identity primary key,
    section_id      bigint not null references erp.sections (section_id) on delete cascade,
    assessment_name varchar(50) not null,
    weightage       int not null check (weightage between 0 and 100),
    unique (section_id, assessment_name)
);

create table erp.assessment_scores (
    score_id        bigint generated always as identity primary key,
    enrollment_id   bigint not null references erp.enrollments (enrollment_id) on delete cascade,
    assessment_name varchar(50) not null,
    ass_score       numeric(5, 2) not null check (ass_score between 0 and 100),
    unique (enrollment_id, assessment_name)
);

create table erp.final_grades (
    enrollment_id bigint primary key references erp.enrollments (enrollment_id) on delete cascade,
    grade_letter  varchar(2) not null
                  check (grade_letter in ('A+', 'A', 'A-', 'B+', 'B', 'B-', 'C', 'C-', 'D', 'D-', 'F')),
    course_cg     numeric(4, 2) not null check (course_cg between 0 and 10)
);

create table erp.settings (
    k varchar(64) primary key,
    v varchar(256) not null
);