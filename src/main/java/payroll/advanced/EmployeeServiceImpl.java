package payroll.advanced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import payroll.Employee;
import payroll.EmployeeNotFoundException;
import payroll.EmployeeRepository;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeServiceImpl(EmployeeRepository repository,EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override public EntityModel<Employee> getOne(Long id) {
        logger.debug("getOne() called");
        logger.info("searching for employee with id: " + id);
        var employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @Override
    public CollectionModel<EntityModel<Employee>> getAll() {
        logger.debug("getAll() called");
        logger.info("searching for all employees");
        var employees = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeControllerV3.class).all()).withSelfRel());
    }

    @Override
    public ResponseEntity<?> replaceEmployee(Employee newEmployee, @PathVariable Long id) {
        logger.debug("replaceEmployee() called");
        logger.info("replacing employee with id: " + id);
        var updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });

        var entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public ResponseEntity<?> deleteOne(Long id) {
        logger.debug("deleteOne() called");
        logger.info("deleting employee with id: " + id);
        try {
            repository.deleteById(id);
        } catch (EmployeeNotFoundException e) {
            logger.error("employee with id: " + id + " not found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> newEmployee(Employee newEmployee) {
        logger.debug("newEmployee() called");
        logger.info("creating new employee");
        var entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
