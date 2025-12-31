package marks;

public class GradeService {

    public String calculateGrade(double marks) {
        if (marks >= 90) return "A+";
        else if (marks >= 80) return "A";
        else if (marks >= 70) return "B";
        else if (marks >= 60) return "C";
        else if (marks >= 50) return "D";
        else return "F";
    }

    public boolean assignGrade(Marks marks) {
        if (marks == null) return false;

        String grade = calculateGrade(marks.getMarks());
        marks.setGrade(grade);
        return true;
    }
}
