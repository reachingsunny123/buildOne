package com.learn2gether.dao;


import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.learn2gether.domain.Customer;


public interface CustomerRepository extends CrudRepository<Customer, Long> {

    public List<Customer> findByFirstName(String firstName);
    public Customer findOne(Long customerId); 
    public boolean exists(Long customerId);
}