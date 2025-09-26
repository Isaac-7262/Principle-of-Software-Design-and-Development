package com.cpLab10sec3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="customers")
public class Customer {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", nullable = false)
	@NotBlank
	@Size(max=100)
	private String name;
	
	@Column(name="email", unique = true, nullable = false)
	@Email
	@NotBlank
	@Size(max=120)
	private String email;
	
	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;
	
	public Customer() {}
	
	public Customer(String name, String email) {
		this.email = email;
		this.name = name;
	}
	
	public Long getId() { return id;}
	public String getName() { return name;}
	public String getEmail() { return email;}
	public Address getAddress() { return address; }
	
	public void setId(Long id) { this.id = id;}
	public void setName(String name) { this.name = name;}
	public void setEmail(String email) { this.email = email;}
	
	public void setAddress(Address address) { 
		if (address == null) {
			if (this.address != null) {
				this.address.setCustomer(null);
			}
			this.address = null;
		} else {
			address.setCustomer(this);
			this.address = address;
		}
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
}