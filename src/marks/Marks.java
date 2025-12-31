package marks;

public class Marks {

    private int marksId;
    private int studentId;
    private int courseId;
    private double marks;
    private String grade;

    // Constructor
    public Marks(int marksId, int studentId, int courseId, double marks) {
        this.marksId = marksId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
    }

    // Getters and Setters
    public int getMarksId() {
        return marksId;
    }

    public void setMarksId(int marksId) {
        this.marksId = marksId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

