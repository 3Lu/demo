package payroll;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import payroll.advanced.EmployeeModelAssembler;

class EmployeeModelAssemblerTest {

    @Test
    void toModel() {
        Employee employee = new Employee("John Doe", "Manager");
        EmployeeModelAssembler employeeModelAssembler = new EmployeeModelAssembler();
        assertEquals(employeeModelAssembler.toModel(employee).getContent(), employee);
    }
}
