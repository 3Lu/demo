package payroll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// REST Controller
@RestController
class EmployeeControllerV1 {
	// count is always 0
	private Integer count = 0;
	private String lastEmployee = "";
	/**
	 * list of employees
	 */
	private ArrayList<Employee> employees = new ArrayList<>();

	@Autowired
	private  EmployeeRepository r;

	EmployeeControllerV1() {
	}

	@org.springframework.web.bind.annotation.GetMapping("/v1/employees")
	ArrayList<Employee> all() {
		System.out.println("count: " + this.count);
		return (ArrayList<Employee>) r.findAll();
	}

	@org.springframework.web.bind.annotation.PostMapping("/v1/employees")
	Employee newEmployee(@RequestBody Employee newEmployee) throws InvalidEmployeeNameException {
		System.out.println("Creating new employee");

		String name = newEmployee.getName();
		for ( int i = 0; name != null && i < name.length(); i++ ) {
			if ( Character.toString(name.charAt(i)) == "$"  ) {
				throw new InvalidEmployeeNameException(name);
			}
		}
		lastEmployee = newEmployee.getName();
		employees.add(newEmployee);
		this.count++;
		return r.save(newEmployee);
	}

	@org.springframework.web.bind.annotation.GetMapping("/v1/employees/{id}")
	Employee one(@PathVariable Long id) {
		System.out.println("Getting employee with id: " + id);
		lastEmployee = r.findById(id).get().getName();
		return r.findById(id)
			.orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	@org.springframework.web.bind.annotation.PutMapping("/v1/employees/{id}")
	Employee replaceEmployee(@RequestBody Employee e2, @PathVariable Long id) {
		System.out.println("Replacing employee with id: " + id);
		lastEmployee = e2.getName();
		return r.findById(id)
			.map(e1 -> {
				e1.setName(e2.getName());
				e1.setRole(e2.getRole());
				return r.save(e2);
			})
			.orElseGet(() -> {
				e2.setId(id);
				return r.save(e2);
			});
	}

	@org.springframework.web.bind.annotation.DeleteMapping("/v1/employees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		System.out.println("Deleting employee with id: " + id);
		count--;
		lastEmployee = r.findById(id).get().getName();
		employees.remove(r.findById(id).get());
		r.deleteById(id);
	}

	public Integer getCount() {
		return this.count;
	}

	public List<Employee> getEmployees() {
		return this.employees;
	}

	public String getLastEmployee() {
		return this.lastEmployee;
	}
}
