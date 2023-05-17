package payroll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import payroll.advanced.EmployeeServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @BeforeEach
    void setUp() {
        employeeRepository.save(new Employee("Bilbo Baggins", "burglar"));
        employeeRepository.save(new Employee("Frodo Baggins", "thief"));
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void getAllEmployees() {
        assertEquals(2, employeeServiceImpl.getAll().getContent().size());
    }

    @Test
    void deleteEmployee() {
        employeeServiceImpl.deleteOne(employeeRepository.findByName("Bilbo Baggins").getId());
        assertEquals(1, employeeServiceImpl.getAll().getContent().size());
    }

    @Test
    void getEmployee() {
        Employee employee = employeeServiceImpl.getOne(employeeRepository.findByRole("burglar").get(0).getId())
                .getContent();
        assertEquals("Bilbo Baggins", employee.getName());
    }

    @Test
    void replaceEmployee() {
        Employee newEmployee = new Employee("John Doe", "Manager");
        employeeServiceImpl.replaceEmployee(newEmployee, employeeRepository.findByName("Bilbo Baggins").getId());
        Employee employee = employeeServiceImpl.getOne(employeeRepository.findByName("John Doe").getId())
                .getContent();
        assertEquals("John Doe", employee.getName());
    }

    @Test
    void exceptionHandling() {
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeServiceImpl.getOne(3L);
        });
    }
}
