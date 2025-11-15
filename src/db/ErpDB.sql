

--STUDENTS TABLE

--altering table


--INSTRUCTORS TABLE
--altering table to accommodate instructor name column
ALTER TABLE instructors ADD COLUMN instructor_name VARCHAR(100) NOT NULL AFTER user_id;

--SECTIONS TABLE
--altering table to accommodate the instructor name column


-- SETTINGS TABLE
CREATE TABLE IF NOT EXISTS settings (k VARCHAR(64) PRIMARY KEY, v VARCHAR(256) NOT NULL);
-- insert the default row for maintenance mode
INSERT INTO settings (k, v) VALUES ('maintenance_on', 'false') ON DUPLICATE KEY UPDATE v = VALUES(v);

--keeping a drop date in the system so that the deadline is set
--example, for now, setting deadline to Nov 15 2025, 23:59:59
INSERT INTO settings(k,v) VALUES ('registration.finalDate', '2025-11-15T23:59:59') ON DUPLICATE KEY UPDATE v = VALUES(v);