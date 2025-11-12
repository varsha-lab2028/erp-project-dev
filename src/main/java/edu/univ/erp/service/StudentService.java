/*this acts as a business logic layer. to ensure that a clean design is maintained*/

package edu.univ.erp.service;
import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class StudentService {
    private final CourseDAO course_dao = new CourseDAO();
    private final SectionDAO section_dao = new SectionDAO();
    private final EnrollmentDAO enrollment_dao = new EnrollmentDAO();
    private final TimeTableDAO timetable_dao = new TimeTableDAO();

    //Catalog = seeing the available courses during a particular semester
    public List<Course> browseCatalog(String q) throws SQLException {
        // If no search keyword is given, show the full catalog
        if (q == null || q.isBlank()) {
            return course_dao.listAll(); //fetches all the courses
        }
        else {
            // If a keyword is entered, search by course code or name
            return course_dao.search(q);
        }
    }

    //register = registering a student into a section
    public String registerForSection(long studentId, long sectionId) throws SQLException {
        if (enrollment_dao.checkRecordExistence(studentId, sectionId)) {
            return "You have already registered for this section";
        }
        int total_capacity = section_dao.capacityOfSection(sectionId);
        int current_capacity = enrollment_dao.countEnrolledInSection(sectionId);
        if (total_capacity >= current_capacity) {
            return "Section full";
        }
        enrollment_dao.insertStudentEnrollment(studentId, sectionId);
        return "Registered";
    }

    /*drop method = students are provided the option to drop a course they
    * are not interested in 10 days after the registration of courses window ends.
    * otherwise it will be frozen*/
    public String dropSection(long studentId, long sectionId) throws SQLException{
        if (!enrollment_dao.checkRecordExistence(studentId, sectionId)) {
            return "Not registered in this section";
        }

        /*can be shown in the user interface*/
        //check the final registration date for the current semester
        LocalDateTime final_reg_date = SettingsDAO.getDateTime("registration.finalDate");
        if (final_reg_date == null) {
            return "Drop policy not configured";
        }

        //calculate the drop window
        //start time = midnight after the final registration date
        //end = 10 days after the start time at 23:59:59
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

        EnrollmentDAO.removeStudentEnrollment(studentId, sectionId);
        return "Dropped successfully (" + message + ")";
    }

    //to display and view the timetable
    public List<TimeTableRow> timetable(long studentId) throws SQLException {
        return TimeTableDAO.getTimetableForStudent(studentId);
    }

    //to display and view the grades
    public List<GradeComponent> getGrades(long studentId, int semNo, String semSeason, int year) throws SQLException {
        return GradeDAO.listGradeComponents(studentId, semNo, semSeason, year);
    }

    //getting transcript of the completed courses only, will be shown in UI
    public List<FinalGrade> getTranscript(long studentId, int semNo, String semSeason, int year) throws SQLException {
        return GradeDAO.listFinalGrades(studentId, semNo, semSeason, year);
    }

}
