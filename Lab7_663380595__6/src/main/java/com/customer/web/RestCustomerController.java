package com.customer.web;

import com.customer.dto.CustomerReq;
import com.customer.model.Customer;
import com.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class RestCustomerController {

    private final CustomerService service;

    public RestCustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Customer> all() {
        return service.getAll();
    }

    @PostMapping
    public Customer create(@Valid @RequestBody CustomerReq req) {
        return service.create(req.name(), req.email());
    }

    @GetMapping("/{id}")
    public Customer one(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @Valid @RequestBody CustomerReq req) {
        return service.update(id, req.name(), req.email());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
