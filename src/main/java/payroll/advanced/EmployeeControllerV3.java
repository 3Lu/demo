package payroll.advanced;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import payroll.Employee;

@RestController
class EmployeeControllerV3 {
	private final EmployeeService service;

	EmployeeControllerV3(EmployeeService service) {
		this.service = service;
	}

	@GetMapping("/v3/employees")
	CollectionModel<EntityModel<Employee>> all() {
		return service.getAll();
	}

	@PostMapping("/v3/employees")
	ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
		return service.newEmployee(newEmployee);
	}

	@GetMapping("/v3/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {
		return service.getOne(id);
	}

	@PutMapping("/v3/employees/{id}")
	ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
		return service.replaceEmployee(newEmployee, id);
	}

	@DeleteMapping("/v3/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		return service.deleteOne(id);
	}
}
