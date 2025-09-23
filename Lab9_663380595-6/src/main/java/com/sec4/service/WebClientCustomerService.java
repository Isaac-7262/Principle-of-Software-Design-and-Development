package com.sec4.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import com.sec4.model.Customer;

@Service
public class WebClientCustomerService {
    
	@Autowired
    private final WebClient webClient;
    
    @Autowired
    public WebClientCustomerService(WebClient webClient) {
        this.webClient = webClient;
    }

    // GET all customers
    public Flux<Customer> getAllCustomers() {
        return webClient.get()
                .uri("/customers")
                .retrieve()
                .bodyToFlux(Customer.class);
    }

    // GET customer by id
    public Mono<Customer> getCustomerById(Long id) {
        return webClient.get()
                .uri("/customers/{id}", id)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    // POST create new customer
    public Mono<Customer> createCustomer(Customer customer) {
        System.out.println("Sending Customer: " + customer);

        return webClient.post()
                .uri("/customers")
                .body(Mono.just(customer), Customer.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Client error during createCustomer")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Server error during createCustomer")))
                .bodyToMono(Customer.class)
                .doOnNext(response ->
                        System.out.println("Response received: " + response))
                .doOnError(error ->
                        System.out.println("Error occurred: " + error.getMessage()));
    }

    // PUT update customer
    public Mono<Customer> updateCustomer(Long id, Customer customer) {
        System.out.println("Edited Customer: " + customer.toString());
        return webClient.put()
                .uri("/customers/" + id)
                .bodyValue(customer)
                .retrieve()
                .bodyToMono(Customer.class);
    }

    // DELETE customer
    public Mono<Void> deleteCustomerById(Long id) {
        return webClient.delete()
                .uri("/customers/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
