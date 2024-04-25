package com.mycompany.testfx;

import java.util.ArrayList;

public class Enrollment {

    private Course course;
    private Student student;

    public Enrollment(Course course, Student student) {
        this.course = course;
        this.student = student;
    }

    public Course getCourse() {
        return this.course;
    }

    public Student getStudent() {
        return this.student;
    }

    public void edit(Course course, Student student) {
        this.course = course;
        this.student = student;
    }

    public boolean equals(Enrollment schedule) {
        return this.course.equals(schedule.getCourse()) && 
            this.student.equals(schedule.getStudent());
    }
    
    public void assign(ArrayList<Student> students, ArrayList<Course> courses) {
        
    }

}