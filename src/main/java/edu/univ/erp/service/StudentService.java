/*this acts as a business logic layer. to ensure that a clean design is maintained*/

package edu.univ.erp.service;
import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.sql.SQLException;
import java.util.List;

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

}
