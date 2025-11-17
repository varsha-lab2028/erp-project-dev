package edu.univ.erp.service;
import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/*Student service acts like an API to connect between the students table in Mysql
* and the UI interfaces under student package*/

public class StudentService {
    private final CourseDAO course_dao = new CourseDAO();
    private final SectionDAO section_dao = new SectionDAO();
    private final EnrollmentDAO enrollment_dao = new EnrollmentDAO();
    private final TimeTableDAO timetable_dao = new TimeTableDAO();
    private final GradeDAO grade_dao = new GradeDAO();

    //course catalog = seeing the available courses during a particular semester
    public List<Course> browseCourseCatalog(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            //fetches all the available courses
            return course_dao.listCourses();
        }
        else {
            // If a keyword is entered, course is search either through name or course code
            return course_dao.searchCourse(keyword);
        }
    }

    //section catalog = seeing the professors and other section information
    public List<Section> browseSectionCatalog(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            //fetches all the course sections
            return section_dao.listAllSections();
        }
        else {
            // if a keyword is entered, search the section
            return section_dao.searchSection(keyword);
        }
    }

    //registering a student into a section
    public String registerForSection(long student_id, long section_id) throws SQLException {
        if (enrollment_dao.checkRecordExistence(student_id, section_id)) {
            return "You have already registered for this section";
        }
        int total_capacity = section_dao.capacityOfSection(section_id);
        int current_capacity = enrollment_dao.countEnrolledInSection(section_id);
        if (current_capacity >= total_capacity) {
            return "Section full";
        }
        enrollment_dao.insertStudentEnrollment(student_id, section_id);
        return "Registered successfully";
    }

    //drop rule = only after registration ends
    public String dropSection(long student_id, long section_id) throws SQLException{
        if (!enrollment_dao.checkRecordExistence(student_id, section_id)) {
            return "Not registered in this section";
        }

        //check the final registration date for the current semester
        LocalDateTime final_reg_date = SettingsDAO.getDateTime("registration.finalDate");
        if (final_reg_date == null) {
            return "Drop policy not configured";
        }

        //calculating the drop window
        LocalDateTime start_drop = final_reg_date.toLocalDate().plusDays(1).atStartOfDay();
        LocalDateTime end_drop = start_drop.plusDays(10).minusSeconds(1);
        LocalDateTime current_time = LocalDateTime.now();

        if (current_time.isBefore(start_drop)) {
            return "Drop window has not started yet. It opens on " + start_drop.toLocalDate();
        }
        if (current_time.isAfter(end_drop)) {
            return "Drop deadline has passed. Last date was " + end_drop.toLocalDate();
        }

        //showing the remaining days before the deadline
        Duration duration = java.time.Duration.between(current_time, end_drop);
        long days_left = duration.toDays();
        String message;
        if (days_left <= 0) {
            message = "less than 1 day left";
        }
        else if (days_left == 1) {
            message = "1 day left";
        }
        else {
            message = days_left + " days left";
        }

        EnrollmentDAO.removeStudentEnrollment(student_id, section_id);
        return "Dropped successfully (" + message + ")";
    }

    //returning the currently registered sections of a student
    public List<Section> getRegisteredSectionsList(long student_id) throws SQLException {
        List<Enrollment> all = enrollment_dao.listEnrolledStudents(student_id);

        //keep only active registrations
        List<Enrollment> active_registrations = all.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.REGISTERED)
                .toList();

        //converting Enrollment to Section, through enrollments we get section information
        List<Section> result = new java.util.ArrayList<>();
        for (Enrollment e : active_registrations) {
            Section s = section_dao.findBySectionId(e.getSectionId());
            if (s != null) result.add(s);
        }
        return result;
    }

    //to display and view the timetable
    public List<TimeTableRow> timetable(long studentId) throws SQLException {
        return timetable_dao.getTimetableForStudent(studentId);
    }

    //to display and view the grades
    public List<GradeComponent> getGrades(long studentId, int semNo, String semSeason, int year) throws SQLException {
        return grade_dao.listGradeComponents(studentId, semNo, semSeason, year);
    }

    //getting transcript of the completed courses, will be shown in UI
    public List<FinalGrade> getTranscript(long studentId, int semNo, String semSeason, int year) throws SQLException {
        return grade_dao.listFinalGrades(studentId, semNo, semSeason, year);
    }
}
