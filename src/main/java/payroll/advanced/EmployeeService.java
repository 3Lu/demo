package payroll.advanced;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import payroll.Employee;

public interface EmployeeService {
    EntityModel<Employee> getOne(Long id);
    CollectionModel<EntityModel<Employee>> getAll();
    ResponseEntity<?> replaceEmployee(Employee newEmployee, @PathVariable Long id);
    ResponseEntity<?> deleteOne(Long id);
    ResponseEntity<?> newEmployee(Employee newEmployee);
}
