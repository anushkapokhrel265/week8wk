package marks;

import java.util.List;

public interface MarksDAO {

    boolean addMarks(Marks marks);

    boolean updateMarks(Marks marks);

    Marks getMarks(int studentId, int courseId);

    List<Marks> getMarksByStudent(int studentId);
}
