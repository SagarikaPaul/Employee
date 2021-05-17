package com.practice.employee.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Name should not be blank")
	@Pattern(regexp = "^[A-Za-z\\s]*$", message = "Name should contain only alphabets")
	private String name;
	
	@NotBlank(message = "Department should not be blank")
	private String department;
	private double salary;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	List<Address> addresses;
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}
	
	public Employee(String name, String department, double salary, List<Address> addresses) {
		this.name = name;
		this.department = department;
		this.salary = salary;
		this.addresses = addresses;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
}
