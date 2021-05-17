package com.practice.employee.repository;

import org.springframework.data.repository.CrudRepository;

import com.practice.employee.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

}
