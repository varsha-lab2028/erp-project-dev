package edu.univ.erp.domain;
import java.time.LocalDateTime;
import java.util.*;

public class Enrollment {
    private long enrollment_id; //like the section one
    private long student_id;
    private long section_id;
    private EnrollmentStatus e_status;
    private LocalDateTime registered_when;
    private LocalDateTime dropped_when;
    private LocalDateTime completed_when;

    public Enrollment(long enrollment_id, long student_id, long section_id,
                      EnrollmentStatus e_status, LocalDateTime registered_when,
                      LocalDateTime dropped_when, LocalDateTime completed_when){
        if (enrollment_id <= 0) {
            throw new IllegalArgumentException("Appropriate ID should be mentioned");
        }
        if (student_id <= 0) {
            throw new IllegalArgumentException("Appropriate student ID should be mentioned");
        }
        if (section_id <= 0) {
            throw new IllegalArgumentException("Appropriate section ID should be mentioned");
        }
        if (e_status == null) {
            throw new IllegalArgumentException("Status should be visible");
        }

        //status specific timestamp requirements
        switch(e_status){
            case REGISTERED -> {
                if(registered_when == null){
                    throw new IllegalArgumentException("registeredAt required");
                }
            }
            case DROPPED -> {
                if(dropped_when == null){
                    throw new IllegalArgumentException("droppedAt required");
                }
            }
            case COMPLETED -> {
                if(completed_when == null){
                    throw new IllegalArgumentException("completedAt required");
                }
            }
        }

        this.enrollment_id = enrollment_id;
        this.student_id = student_id;
        this.section_id = section_id;
        this.e_status = e_status;
        this.registered_when = registered_when;
        this.dropped_when = dropped_when;
        this.completed_when = completed_when;

    }

    //getters
    public boolean isActive(){
        return e_status == EnrollmentStatus.DROPPED;
    }
}
