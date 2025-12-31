package reports;

/**
 * Small runtime exception wrapper for DAO layer.
 * Keeps ReportDAO signatures clean while still surfacing failures.
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}