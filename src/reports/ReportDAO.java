package reports;

import course.Course;
import marks.Marks;
import student.Student;

import java.sql.*;
import java.util.*;

/**
 * Reports module DAO.
 * Uses JDBC and aggregates data across modules (students/courses/enrollments/attendance/marks).
 */
public class ReportDAO {

    // If your project already supplies a Connection, you can inject it via constructor.
    // This module defaults to DBUtil.getConnection() for simplicity.
    public ReportDAO() { }

    /**
     * Builds a complete report for a student:
     * - enrolled courses
     * - marks per course (if present)
     * - overall attendance percentage across all courses
     */
    public StudentReport generateStudentReport(int studentId) {
        try (Connection con = DBUtil.getConnection()) {

            List<Course> courses = getCoursesByStudent(con, studentId);
            List<Marks> marksList = getMarksByStudent(con, studentId);
            double attendancePct = calculateOverallAttendancePercentage(con, studentId);

            return new StudentReport(studentId, courses, marksList, attendancePct);

        } catch (SQLException e) {
            throw new DataAccessException("Failed to generate student report for studentId=" + studentId, e);
        }
    }

    /**
     * Course-wise attendance report: for a given course, returns map of Course -> attendancePercentage.
     * Your spec says Map<Course, Double>. That’s unusual because it’s one courseId,
     * but we follow the spec exactly: map will contain one entry for that course.
     */
    public Map<Course, Double> generateCourseAttendanceReport(int courseId) {
        try (Connection con = DBUtil.getConnection()) {

            Course course = getCourseById(con, courseId);
            if (course == null) {
                return Collections.emptyMap();
            }

            double pct = calculateCourseAttendancePercentage(con, courseId);

            Map<Course, Double> result = new LinkedHashMap<>();
            result.put(course, pct);
            return result;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to generate course attendance report for courseId=" + courseId, e);
        }
    }

    /**
     * Returns students whose overall attendance percentage (across all courses) is below minAttendance.
     * minAttendance is expected in percentage form, e.g., 75.0.
     */
    public List<Student> getDefaulterList(double minAttendance) {
        try (Connection con = DBUtil.getConnection()) {

            // We compute per-student overall attendance:
            // presentCount / totalCount * 100
            // If a student has no attendance records at all, percentage is treated as 0.
            String sql =
                    "SELECT s.student_id, s.name, s.roll_no, s.email, " +
                            "       COALESCE(ROUND( (SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) / NULLIF(COUNT(a.attendance_id),0)) * 100, 2), 0) AS pct " +
                            "FROM students s " +
                            "LEFT JOIN attendance a ON a.student_id = s.student_id " +
                            "GROUP BY s.student_id, s.name, s.roll_no, s.email " +
                            "HAVING pct < ? " +
                            "ORDER BY pct ASC, s.student_id ASC";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDouble(1, minAttendance);

                try (ResultSet rs = ps.executeQuery()) {
                    List<Student> defaulters = new ArrayList<>();
                    while (rs.next()) {
                        Student st = new Student();
                        st.setStudentId(rs.getInt("student_id"));
                        st.setName(rs.getString("name"));
                        st.setRollNo(rs.getString("roll_no"));
                        st.setEmail(rs.getString("email"));
                        defaulters.add(st);
                    }
                    return defaulters;
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch defaulter list for minAttendance=" + minAttendance, e);
        }
    }

    // -----------------------
    // Internal helper methods
    // -----------------------

    private List<Course> getCoursesByStudent(Connection con, int studentId) throws SQLException {
        String sql =
                "SELECT c.course_id, c.course_name, c.credits " +
                        "FROM courses c " +
                        "JOIN enrollments e ON e.course_id = c.course_id " +
                        "WHERE e.student_id = ? " +
                        "ORDER BY c.course_id ASC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (rs.next()) {
                    Course c = new Course();
                    c.setCourseId(rs.getInt("course_id"));
                    c.setCourseName(rs.getString("course_name"));
                    c.setCredits(rs.getInt("credits"));
                    courses.add(c);
                }
                return courses;
            }
        }
    }

    private List<Marks> getMarksByStudent(Connection con, int studentId) throws SQLException {
        String sql =
                "SELECT m.marks_id, m.student_id, m.course_id, m.marks, m.grade " +
                        "FROM marks m " +
                        "WHERE m.student_id = ? " +
                        "ORDER BY m.course_id ASC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Marks> marksList = new ArrayList<>();
                while (rs.next()) {
                    Marks m = new Marks();
                    m.setMarksId(rs.getInt("marks_id"));
                    m.setStudentId(rs.getInt("student_id"));
                    m.setCourseId(rs.getInt("course_id"));
                    m.setMarks(rs.getDouble("marks"));
                    m.setGrade(rs.getString("grade"));
                    marksList.add(m);
                }
                return marksList;
            }
        }
    }

    private double calculateOverallAttendancePercentage(Connection con, int studentId) throws SQLException {
        String sql =
                "SELECT " +
                        "  COALESCE(ROUND( (SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) / NULLIF(COUNT(attendance_id),0)) * 100, 2), 0) AS pct " +
                        "FROM attendance " +
                        "WHERE student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("pct");
                return 0.0;
            }
        }
    }

    private Course getCourseById(Connection con, int courseId) throws SQLException {
        String sql =
                "SELECT course_id, course_name, credits " +
                        "FROM courses " +
                        "WHERE course_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseName(rs.getString("course_name"));
                c.setCredits(rs.getInt("credits"));
                return c;
            }
        }
    }

    private double calculateCourseAttendancePercentage(Connection con, int courseId) throws SQLException {
        String sql =
                "SELECT " +
                        "  COALESCE(ROUND( (SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) / NULLIF(COUNT(attendance_id),0)) * 100, 2), 0) AS pct " +
                        "FROM attendance " +
                        "WHERE course_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("pct");
                return 0.0;
            }
        }
    }
}