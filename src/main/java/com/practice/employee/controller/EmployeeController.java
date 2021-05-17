package com.practice.employee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.practice.employee.model.Address;
import com.practice.employee.model.Employee;
import com.practice.employee.service.EmployeeService;
import com.practice.employee.util.ErrorMessage;
import com.practice.employee.util.Message;

@RestController
@RequestMapping("api/v1/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	// ADD EMPLOYEEE
	@PostMapping
	public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
		return new ResponseEntity<Employee>(employeeService.addEmployee(employee), HttpStatus.CREATED);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	List<ErrorMessage> exceptionHandler(MethodArgumentNotValidException e) {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		List<ErrorMessage> fieldErrorMessages = fieldErrors.stream().
				map(fieldError -> new ErrorMessage("failure", fieldError.getDefaultMessage()))
				.collect(Collectors.toList());
		return fieldErrorMessages;
	}
	
	// FIND ALL EMPLOYEE
	@GetMapping
	public ResponseEntity<List<Employee>> findAllEmployees() {
		Iterable<Employee> availableEmployees = employeeService.findAllEmployees();
		List<Employee> employeeList = new ArrayList<>(10);
		availableEmployees.forEach(employeeList::add);
		if(!employeeList.isEmpty()) {
			return new ResponseEntity<List<Employee>>(employeeList, HttpStatus.OK);
		}
		throw new ValidationException("No Employee Found");
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(ValidationException.class)
	Message exceptionHandler(ValidationException e) {
		return new Message(e.getMessage());
	}
	
	//FIND EMPLOYEE BY ID
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		if(employeeService.getEmployeeById(id).isPresent()) {
			return new ResponseEntity<Employee>(employeeService.getEmployeeById(id).get(), HttpStatus.OK);
		}
		throw new ValidationException("No Employee Found for the id " + id);
	}
	
	//DELETE EMPLOYEE BY ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
		Map<String, String> map = new HashMap<>();
		if(employeeService.getEmployeeById(id).isPresent()) {
			employeeService.deleteEmployee(id);
			map.put("status", "success");
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.ACCEPTED);
		}
		throw new ValidationException("No Employee Found for the id " + id);
	}
	
	// UPDATE EMPLOYEE BY ID
	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,@Valid @RequestBody Employee employee) {
		if(employeeService.getEmployeeById(id).isPresent()) {
			Employee existingEmployee = employeeService.getEmployeeById(id).get();
			BeanUtils.copyProperties(employee, existingEmployee, "id");
			Employee updatedEmployee = employeeService.updateEmployee(existingEmployee);
			return new ResponseEntity<Employee>(updatedEmployee, HttpStatus.OK);
		}
		throw new ValidationException("No Employee Found for the id " + id);
	}
	
	
	// ADD ADDRESS BY EMPLOYEE ID
	@PostMapping("/address/{id}")
	public ResponseEntity<Employee> addAddressesToEmployee(@PathVariable Long id, @RequestBody List<Address> address) {
		if(employeeService.getEmployeeById(id).isPresent()) {
			Employee employee = employeeService.getEmployeeById(id).get();
			address.stream().forEach(add -> add.setEmployee(employee));
			employeeService.addAddress(address);
			return new ResponseEntity<Employee>(employeeService.getEmployeeById(id).get(), HttpStatus.OK);
		}
		throw new ValidationException("No Employee Found for the id " + id);
	}

}
