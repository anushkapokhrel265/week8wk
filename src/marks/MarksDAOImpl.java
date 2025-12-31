package marks;

import java.util.ArrayList;
import java.util.List;

public class MarksDAOImpl implements MarksDAO {

    private List<Marks> marksList = new ArrayList<>();

    @Override
    public boolean addMarks(Marks marks) {
        return marksList.add(marks);
    }

    @Override
    public boolean updateMarks(Marks marks) {
        for (Marks m : marksList) {
            if (m.getStudentId() == marks.getStudentId()
                    && m.getCourseId() == marks.getCourseId()) {
                m.setMarks(marks.getMarks());
                m.setGrade(marks.getGrade());
                return true;
            }
        }
        return false;
    }

    @Override
    public Marks getMarks(int studentId, int courseId) {
        for (Marks m : marksList) {
            if (m.getStudentId() == studentId && m.getCourseId() == courseId) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Marks> getMarksByStudent(int studentId) {
        List<Marks> result = new ArrayList<>();
        for (Marks m : marksList) {
            if (m.getStudentId() == studentId) {
                result.add(m);
            }
        }
        return result;
    }
}
