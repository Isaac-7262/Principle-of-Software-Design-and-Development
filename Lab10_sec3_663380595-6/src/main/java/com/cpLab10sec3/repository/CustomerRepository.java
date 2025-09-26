package com.cpLab10sec3.repository;
import org.springframework.data.repository.CrudRepository;

import com.cpLab10sec3.entity.*;
public interface CustomerRepository extends CrudRepository<Customer,Long> {

}
