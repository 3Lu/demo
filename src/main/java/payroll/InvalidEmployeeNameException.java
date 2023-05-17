package payroll;

public class InvalidEmployeeNameException extends Throwable {
    public InvalidEmployeeNameException(String name) {
        super("Invalid employee name: " + name);
    }
}
