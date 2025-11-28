package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.auth.session.Session; // Needed for Identity Check

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class StudentService {
    private final CourseDAO course_dao = new CourseDAO();
    private final SectionDAO section_dao = new SectionDAO();
    private final EnrollmentDAO enrollment_dao = new EnrollmentDAO();
    private final TimeTableDAO timetable_dao = new TimeTableDAO();
    private final GradeDAO2 grade_dao = new GradeDAO2();
    private final TranscriptDAO transcript_dao = new TranscriptDAO();
    private final SettingsDAO settings_dao = new SettingsDAO();

    // --- HELPER: Enforce Data Privacy ---
    private void verifyIdentity(long targetStudentId) {
        // If you are an ADMIN, you can see anyone.
        if (Session.hasRole("ADMIN")) return;

        // If you are a STUDENT, you can only see YOURSELF.
        if (Session.userId() != targetStudentId) {
            throw new SecurityException("Access Denied: You cannot view or modify another student's data.");
        }
    }

    // --- HELPER: Check Deadline ---
    private void checkDeadline() throws SQLException {
        String deadlineStr = settings_dao.get("semester_deadline");
        if (deadlineStr != null) {
            LocalDate deadline = LocalDate.parse(deadlineStr);
            if (LocalDate.now().isAfter(deadline)) {
                throw new IllegalStateException("The registration deadline (" + deadline + ") has passed.");
            }
        }
    }

    // 1. Browse Catalogs (Public info, no checks needed)
    public List<Course> browseCourseCatalog(String keyword) throws SQLException {
        return (keyword == null || keyword.isBlank()) ? course_dao.listCourses() : course_dao.searchCourse(keyword);
    }

    public List<Section> browseSectionCatalog(String keyword) throws SQLException {
        return (keyword == null || keyword.isBlank()) ? section_dao.listAllSections() : section_dao.searchSection(keyword);
    }

    // 2. Register (Protected)
    public void registerForSection(long student_id, long section_id) throws SQLException {
        AccessControl.checkRole("STUDENT");
        AccessControl.checkWritable(); // Maintenance Check
        verifyIdentity(student_id);    // Identity Check
        checkDeadline();               // Deadline Check

        if (enrollment_dao.checkRecordExistence(student_id, section_id)) {
            throw new IllegalStateException("You are already registered in this section.");
        }
        
        int total_capacity = section_dao.capacityOfSection(section_id);
        int current_capacity = enrollment_dao.countEnrolledInSection(section_id);
        
        if (current_capacity >= total_capacity) {
            throw new IllegalStateException("This section is full.");
        }

        enrollment_dao.insertStudentEnrollment(student_id, section_id);
    }

    // 3. Drop (Protected)
    public String dropSection(long student_id, long section_id) throws SQLException {
        AccessControl.checkRole("STUDENT");
        AccessControl.checkWritable(); // Maintenance Check
        verifyIdentity(student_id);    // Identity Check
        checkDeadline();               // Deadline Check

        if (!enrollment_dao.checkRecordExistence(student_id, section_id)) {
            return "Not registered in this section";
        }

        EnrollmentDAO.removeStudentEnrollment(student_id, section_id);
        return "Dropped successfully";
    }

    // 4. View Data (Privacy Protected)
    public List<Section> getRegisteredSectionsList(long student_id) throws SQLException {
        verifyIdentity(student_id); // Prevent viewing others' registrations
        return enrollment_dao.listRegisteredSections(student_id);
    }

    public List<TimeTableRow> getTimeTable(long studentId) throws SQLException {
        verifyIdentity(studentId); // Prevent viewing others' timetable
        return timetable_dao.getTimetableForStudent(studentId);
    }

    public List<Object[]> getGradeComponentTable(long student_id, int sem_no, String sem_season, int year) throws SQLException {
        verifyIdentity(student_id); // Prevent viewing others' grades
        return grade_dao.listGradeComponents(student_id, sem_no, sem_season, year);
    }

    public List<TranscriptRow> getTranscript(long studentId) throws SQLException {
        verifyIdentity(studentId); // Prevent viewing others' transcripts
        return transcript_dao.fetchTranscript(studentId);
    }
}