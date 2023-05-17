package payroll.advanced;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import payroll.Employee;
import payroll.EmployeeNotFoundException;
import payroll.EmployeeRepository;

@RestController
class EmployeeControllerV2 {

	private final EmployeeRepository repository;

	private final EmployeeModelAssembler assembler;

	EmployeeControllerV2(EmployeeRepository repository, EmployeeModelAssembler assembler) {

		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping("/v2/employees")
	CollectionModel<EntityModel<Employee>> all() {
		List<Employee> employees =	repository.findAll();
		List<EntityModel<Employee>> employees2 = Collections.emptyList();
		for (int i = 0; i < repository.findAll().size() ; i++) {
			employees2.add(assembler.toModel(employees.get(i)));
		}

		return CollectionModel.of(employees2, linkTo(methodOn(EmployeeControllerV2.class).all()).withSelfRel());
	}

	@PostMapping("/v2/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) {

		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

		return entityModel.getContent();
	}

	@GetMapping("/v2/employees/{id}")
	EntityModel<Employee> one(@PathVariable Long id) {

		Employee employee = repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));

		return assembler.toModel(employee);
	}

	@PutMapping("/v2/employees/{id}")
	ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		Employee updatedEmployee = repository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(() -> {
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});

		EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}

	@DeleteMapping("/v2/employees/{id}")
	ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

		repository.deleteById(id);

		return ResponseEntity.noContent().build();
	}
}
