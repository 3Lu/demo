package payroll.advanced;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import payroll.Employee;

@Component public
class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

	@Override
	public EntityModel<Employee> toModel(Employee employee) {

		return EntityModel.of(employee,
				linkTo(methodOn(EmployeeControllerV2.class).one(employee.getId())).withSelfRel(),
				linkTo(methodOn(EmployeeControllerV2.class).all()).withRel("employees"));
	}
}
