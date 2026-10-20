package edu.univ.erp.data;

import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Section;
import org.junit.jupiter.api.Test;
import edu.univ.erp.auth.AuthDAO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DaoSmokeTest {
    //this will run every query against the seeded supabase database
    static final long INST1 = 2;
    static final long STU1 = 3;

    @Test
    void settings() throws Exception {
        SettingsDAO settingsDAO = new SettingsDAO();
        String before = SettingsDAO.get("maintenance_on");
        assertNotNull(before);
        settingsDAO.upsertSetting("maintenance_on", before);
        assertEquals(before, SettingsDAO.get("maintenance_on"));
    }

    @Test
    void courses() throws Exception {
        CourseDAO courseDAO = new CourseDAO();
        assertFalse(courseDAO.listCourses().isEmpty());
        assertFalse(courseDAO.searchCourse("cse").isEmpty());
        assertEquals("CSE101", courseDAO.findByCourseCode("CSE101").getCourseCode());
    }

    @Test
    void sections() throws Exception {
        SectionDAO dao = new SectionDAO();
        assertFalse(dao.listAllSections().isEmpty());
        assertFalse(dao.searchSection("cse").isEmpty());
        assertFalse(dao.listSectionsByInstructor(INST1).isEmpty());
        assertNotNull(dao.findBySectionId(1));
        assertEquals(1, dao.capacityOfSection(3));
    }

    @Test
    void sectionInsertUpdateDelete() throws Exception {
        SectionDAO dao = new SectionDAO();
        //inserting data in a fake section in room "TEST-ROOM", capacity 5
        dao.insertSection(new Section(1,
                "SSH101", INST1,
                "x",
                "Friday",
                "16:00-17:00",
                "TEST-ROOM",
                5,
                1,
                "MONSOON",
                2025));

        // find the row we just made, to learn the id the DB gave it
        Section made = dao.listAllSections().stream()
                .filter(s -> s.getClassroom().equals("TEST-ROOM")).findFirst().orElseThrow();

        //update it, same value but with capacity 9
        dao.updateSection(new Section(made.getSectionId(), "SSH101", INST1, "x", "Friday", "16:00-17:00", "TEST-ROOM", 9, 1, "MONSOON", 2025));
        //check if the update reached the DB or not
        assertEquals(9, dao.capacityOfSection(made.getSectionId()));
        //delete the section, the check if it is gone
        dao.deleteSection(made.getSectionId());
        assertNull(dao.findBySectionId(made.getSectionId()));
    }

    @Test
    void enrollments() throws Exception {
        EnrollmentDAO dao = new EnrollmentDAO();
        List<Section> registered = dao.listRegisteredSections(STU1);
        assertFalse(registered.isEmpty());
        assertEquals("Meera Nair", registered.get(0).getInstructorName());
        assertFalse(dao.listEnrolledStudents(STU1).isEmpty());
        assertFalse(dao.listEnrolledStudentsBySection(1).isEmpty());
        assertTrue(dao.countEnrolledInSection(1) > 0);
        assertTrue(dao.checkRecordExistence(STU1, 1));
    }

    @Test
    void timetable() throws Exception {
        // one timetable row per REGISTERED section, no dropped or completed ones
        int registered = new EnrollmentDAO().listRegisteredSections(STU1).size();
        assertEquals(registered, TimeTableDAO.getTimetableForStudent(STU1).size());
    }

    @Test
    void grades() throws Exception {
        assertFalse(GradeDAO2.listGradeComponents(STU1, 1, "MONSOON", 2025).isEmpty());
        assertEquals(1, GradeDAO2.listFinalGrades(STU1, 1, "MONSOON", 2025).size());   // the COMPLETED CSE101
        assertFalse(new GradeDAO2().listGradesBySection(1).isEmpty());
    }

    @Test
    void scoreEntryAndFinalGrade() throws Exception {
        // stu1 in section 1 has Quizzes 80, Assignments 75, Midsem 68 and no Endsem yet
        GradeDAO2 dao = new GradeDAO2();
        dao.upsertComponentScore(1, STU1, "Endsem", 50);   // first time: INSERT
        dao.upsertComponentScore(1, STU1, "Endsem", 70);   // second time: UPDATE, not a duplicate row
        dao.calculateFinalGrades(1);
        // 80*0.10 + 75*0.20 + 68*0.30 + 70*0.40 = 71.4 -> B+
        Object[] stu1Row = dao.listGradesBySection(1).get(0);
        assertEquals("B+", stu1Row[7]);
    }

    @Test
    void transcript() throws Exception {
        TranscriptDAO dao = new TranscriptDAO();
        assertFalse(dao.fetchTranscript(STU1).isEmpty());
        assertFalse(dao.listCurrentRegistrations(STU1).isEmpty());
    }

    @Test
    void deletesRefusedWhenStudentsEnrolled() throws Exception {
        assertThrows(IllegalStateException.class, () -> new SectionDAO().deleteSection(1));
        assertThrows(IllegalStateException.class, () -> new CourseDAO().deleteCourse("CSE201"));
        assertNotNull(new SectionDAO().findBySectionId(1));   // still there
    }

    @Test
    void authReads() throws Exception {
        AuthDAO dao = new AuthDAO();
        assertNotNull(dao.findByUsername("stu1"));
        assertTrue(dao.listUsers().size() >= 4);
    }

}
