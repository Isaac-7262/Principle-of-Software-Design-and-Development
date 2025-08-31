package com.customer.web;

import com.customer.dto.CustomerReq;
import com.customer.dto.CustomerResponse;
import com.customer.model.Customer;
import com.customer.repository.CustomerRepository;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class RestCustomerController {

    private final CustomerRepository repo;

    public RestCustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerReq req) {
        Customer saved = repo.save(new Customer(req.getName(), req.getEmail()));
        return ResponseEntity.ok(CustomerResponse.fromEntity(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return repo.findById(id)
                   .map(CustomerResponse::fromEntity)
                   .map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<CustomerResponse> getAll() {
        return repo.findAll().stream()
                   .map(CustomerResponse::fromEntity)
                   .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @Valid @RequestBody CustomerReq req) {
        return repo.findById(id)
                   .map(customer -> {
                       customer.setName(req.getName());
                       customer.setEmail(req.getEmail());
                       Customer updated = repo.save(customer);
                       return ResponseEntity.ok(CustomerResponse.fromEntity(updated));
                   })
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Customer> customerOpt = repo.findById(id);
        if (customerOpt.isPresent()) {
            repo.delete(customerOpt.get());
            return ResponseEntity.noContent().build(); // คืนค่า Void
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
