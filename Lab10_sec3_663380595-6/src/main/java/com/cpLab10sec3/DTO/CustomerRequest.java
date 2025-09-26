package com.cpLab10sec3.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Email
    @Size(max = 120)
    private String email;
    @Valid
    private AddressRequest address;

    public CustomerRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public AddressRequest getAddress() { return address; }
    public void setAddress(AddressRequest address) { this.address = address; }
}