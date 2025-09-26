package com.cpLab10sec3.repository;

import com.cpLab10sec3.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
