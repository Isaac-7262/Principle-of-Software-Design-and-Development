package com.cpLab10sec3.webapi;

import com.cpLab10sec3.DTO.AddressResponse;
import com.cpLab10sec3.entity.Address;
import com.cpLab10sec3.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressRestController {
    @Autowired
    private AddressRepository addressRepository;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        List<AddressResponse> addresses = addressRepository.findAll().stream()
            .map(a -> new AddressResponse(
                a.getId(), a.getLine1(), a.getLine2(), a.getCity(), a.getState(), a.getPostalCode(), a.getCountry()
            ))
            .collect(Collectors.toList());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        if (address == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AddressResponse response = new AddressResponse(
            address.getId(), address.getLine1(), address.getLine2(), address.getCity(), address.getState(), address.getPostalCode(), address.getCountry()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
