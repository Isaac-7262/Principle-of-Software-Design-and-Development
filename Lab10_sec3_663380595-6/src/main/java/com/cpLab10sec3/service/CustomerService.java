package com.cpLab10sec3.service;

import com.cpLab10sec3.DTO.AddressRequest;
import com.cpLab10sec3.DTO.AddressResponse;
import com.cpLab10sec3.DTO.CustomerRequest;
import com.cpLab10sec3.DTO.CustomerResponse;
import com.cpLab10sec3.entity.Address;
import com.cpLab10sec3.entity.Customer;
import com.cpLab10sec3.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository repo;
    public CustomerService(CustomerRepository repo) { this.repo = repo; }

    @Transactional
    public CustomerResponse create(CustomerRequest req) {
        Customer c = new Customer();
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        AddressRequest ar = req.getAddress();
        if (ar != null) {
            Address a = new Address();
            a.setLine1(ar.getLine1());
            a.setLine2(ar.getLine2());
            a.setCity(ar.getCity());
            a.setState(ar.getState());
            a.setPostalCode(ar.getPostalCode());
            a.setCountry(ar.getCountry());
            c.setAddress(a);
        }
        Customer saved = repo.save(c);
        Address sa = saved.getAddress();
        AddressResponse addrResp = null;
        if (sa != null) {
            addrResp = new AddressResponse(sa.getId(), sa.getLine1(), sa.getLine2(), sa.getCity(), sa.getState(), sa.getPostalCode(), sa.getCountry());
        }
        return new CustomerResponse(saved.getId(), saved.getName(), saved.getEmail(), addrResp);
    }

    @Transactional(readOnly = true)
    public CustomerResponse get(Long id) {
        Customer c = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer %d not found".formatted(id)));
        Address a = c.getAddress();
        AddressResponse addrResp = null;
        if (a != null) {
            addrResp = new AddressResponse(a.getId(), a.getLine1(), a.getLine2(), a.getCity(), a.getState(), a.getPostalCode(), a.getCountry());
        }
        return new CustomerResponse(c.getId(), c.getName(), c.getEmail(), addrResp);
    }

    public List<CustomerResponse> getCustomerList() {
        List<Customer> customers = (List<Customer>) repo.findAll();
        return customers.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    private CustomerResponse mapToResponse(Customer customer) {
        AddressResponse addressResponse = null;
        Address address = customer.getAddress();
        if (address != null) {
            addressResponse = new AddressResponse(
                address.getId(), address.getLine1(), address.getLine2(),
                address.getCity(), address.getState(), address.getPostalCode(), address.getCountry()
            );
        }
        return new CustomerResponse(
            customer.getId(), customer.getName(), customer.getEmail(), addressResponse
        );
    }

    public CustomerResponse getCustomerById(Long id) {
        return get(id);
    }
    public CustomerResponse save(CustomerRequest req) {
        return create(req);
    }
    public CustomerResponse updateCustomer(Long id, CustomerRequest req) {
        Customer customer = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer %d not found".formatted(id)));
        customer.setName(req.getName());
        customer.setEmail(req.getEmail());
        AddressRequest ar = req.getAddress();
        if (ar != null) {
            Address address = customer.getAddress();
            if (address == null) address = new Address();
            address.setLine1(ar.getLine1());
            address.setLine2(ar.getLine2());
            address.setCity(ar.getCity());
            address.setState(ar.getState());
            address.setPostalCode(ar.getPostalCode());
            address.setCountry(ar.getCountry());
            customer.setAddress(address);
        }
        Customer saved = repo.save(customer);
        return mapToResponse(saved);
    }
    public void deleteCustomer(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer %d not found".formatted(id));
        }
        repo.deleteById(id);
    }
}