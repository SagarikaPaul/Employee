package com.practice.employee.service;

import java.util.List;
import java.util.Optional;

import com.practice.employee.model.Address;
import com.practice.employee.model.Employee;

public interface EmployeeService {

	public Employee addEmployee(Employee employee);
	public Iterable<Employee> findAllEmployees();
	public Optional<Employee> getEmployeeById(Long id);
	public void deleteEmployee(Long id);
	public Employee updateEmployee(Employee employee);
	public void addAddress(List<Address> addresses);
}
