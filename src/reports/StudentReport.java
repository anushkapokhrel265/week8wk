package reports;

import course.Course;
import marks.Marks;

import java.util.ArrayList;
import java.util.List;

public class StudentReport {

    private int studentId;
    private List<Course> courses;
    private List<Marks> marks;
    private double attendancePercentage;

    public StudentReport() {
        this.courses = new ArrayList<>();
        this.marks = new ArrayList<>();
    }

    public StudentReport(int studentId, List<Course> courses, List<Marks> marks, double attendancePercentage) {
        this.studentId = studentId;
        this.courses = (courses != null) ? courses : new ArrayList<>();
        this.marks = (marks != null) ? marks : new ArrayList<>();
        this.attendancePercentage = attendancePercentage;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = (courses != null) ? courses : new ArrayList<>();
    }

    public List<Marks> getMarks() {
        return marks;
    }

    public void setMarks(List<Marks> marks) {
        this.marks = (marks != null) ? marks : new ArrayList<>();
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    @Override
    public String toString() {
        return "StudentReport{" +
                "studentId=" + studentId +
                ", courses=" + courses +
                ", marks=" + marks +
                ", attendancePercentage=" + attendancePercentage +
                '}';
    }
}