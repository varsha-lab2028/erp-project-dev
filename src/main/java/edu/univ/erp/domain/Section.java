package edu.univ.erp.domain;

public class Section{
    private final long section_id;
    private final String course_code;
    private final long instructor_id;
    private final String instructor_name;
    private final DayOfTheWeek day; //day when this section has a class
    private final String timings; //time when this section has class on that particular day
    private final String classroom;
    private final int capacity;
    private int sem_no; //the number of the semester, like 1, 2
    private SemesterSeason sem_season; //winter sem, summer sem
    private final int year;

    public Section(long section_id, String course_code, long instructor_id,
                   String instructor_name, DayOfTheWeek day, String timings,
                   String classroom, int capacity, int sem_no, SemesterSeason sem_season,
                   int year){
        //constructor
        if(section_id<=0){
            throw new IllegalArgumentException("Appropriate ID should exist");
        }
        if(course_code == null || course_code.isBlank()){
            throw new IllegalArgumentException("Course code should be mentioned");
        }
        if(instructor_id<= 0){
            throw new IllegalArgumentException("Appropriate User ID should be there");
        }
        if(instructor_name == null || instructor_name.isBlank()){
            throw new IllegalArgumentException("Instructor name should be mentioned");
        }
        if(day == null || day.isBlank()){
            throw new IllegalArgumentException("The class's day should be mentioned");
        }
        if(timings == null || timings.isBlank()){
            throw new IllegalArgumentException("Class timings field should be mentioned");
        }
        if(classroom == null || classroom.isBlank()){
            throw new IllegalArgumentException("Classroom number should be mentioned");
        }
        if(capacity <= 0){
            throw new IllegalArgumentException("Capacity of students in the course should be appropriate");
        }
        if(sem_no <= 0){
            throw new IllegalArgumentException("Semester number field should be mentioned");
        }
        if(sem_season == null || sem_season.isBlank()){
            throw new IllegalArgumentException("Semester season should be mentioned");
        }
        if(year <= 2000){
            throw new IllegalArgumentException("Capacity of students in the course should be appropriate");
        }

        this.section_id = section_id;
        this.course_code = course_code;
        this.instructor_id = instructor_id;
        this.instructor_name = instructor_name;
        this.day = day;
        this.timings = timings;
        this.classroom = classroom;
        this.capacity = capacity;
        this.sem_no = sem_no;
        this.sem_season = sem_season;
        this.year = year;
    }

    //getters
    public long getId(){
        return section_id;
    }
    public String course_code(){
        return course_code;
    }
    public long getInstructor_userid(){return instructor_id;}
    public String getInstructor_name(){
        return instructor_name;
    }
    public DayOfTheWeek getDay(){
        return day;
    }
    public String getTimings(){
        return timings;
    }
    public String getClassroom(){
        return classroom;
    }
    public int getCapacity(){
        return capacity;
    }
    public int getSemNumber(){
        return sem_no;
    }
    public SemesterSeason getSemSeason() {
        return sem_season;
    }
    public int getYear(){
        return year;
    }
}
