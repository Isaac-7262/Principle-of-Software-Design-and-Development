package com.customer.service;

import com.customer.model.Customer;
import com.customer.repository.CustomerRepository;
import com.customer.web.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public Customer create(String name, String email) {
        // กัน email ซ้ำแบบสวย ๆ ก่อนส่งเข้า DB
        if (repo.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists: " + email);
        }
        return repo.save(new Customer(name, email));
    }

    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer %d not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public List<Customer> getAll() {
        return repo.findAll();
    }

    public Customer update(Long id, String name, String email) {
        Customer c = getById(id);

        // ถ้าเปลี่ยนอีเมล ให้เช็คชนกัน
        if (!c.getEmail().equals(email) && repo.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists: " + email);
        }

        c.setName(name);
        c.setEmail(email);
        return repo.save(c); // save ชัด ๆ
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Customer %d not found".formatted(id));
        }
        repo.deleteById(id);
    }
}