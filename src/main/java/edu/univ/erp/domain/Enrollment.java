package edu.univ.erp.domain;
import java.time.LocalDateTime;
import java.util.*;

public class Enrollment {
    private long id; //like the section one
    private long student_userid;
    private long section_userid;
    EnrollmentStatus e_status;
    LocalDateTime registered_when;
    LocalDateTime dropped_when;

    public Enrollment(long id, long student_userid, long section_userid,
                      EnrollmentStatus e_status, LocalDateTime registered_when,
                      LocalDateTime dropped_when){
        if (id <= 0) {
            throw new IllegalArgumentException("Appropriate ID should be mentioned");
        }
        if (student_userid <= 0) {
            throw new IllegalArgumentException("Appropriate student ID should be mentioned");
        }
        if (section_userid <= 0) {
            throw new IllegalArgumentException("Appropriate section ID should be mentioned");
        }
        if (e_status == null) {
            throw new IllegalArgumentException("Status should be visible");
        }
        if (registered_when == null) {
            throw new IllegalArgumentException("Time when registered has not been recorded");
        }
        if (e_status == EnrollmentStatus.DROPPED && dropped_when == null)
            throw new IllegalArgumentException("droppedAt required if DROPPED");
    }

    public boolean isActive(){
        return e_status == EnrollmentStatus.DROPPED;
    }
}
