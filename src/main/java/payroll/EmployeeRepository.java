package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByName(String name);
    List<Employee> findByRole(String role);
    Employee findByNameAndRole(String name, String role);

}
