package com.sec4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sec4.service.WebClientCustomerService;
import com.sec4.model.Customer;

import reactor.core.publisher.Mono;

@Controller
public class CustomerController {
    @Autowired
    WebClientCustomerService customerService;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/web/customers")
    public String getAllCustomers(Model model) {
        model.addAttribute("customers",
                customerService.getAllCustomers()
                        .collectList().block()); // Flux<Customer>.block() -> List<Customer>
        return "customerListCRUD";
    }

    @GetMapping("/web/customers/{id}") // view
    public String getViewCustomerById(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id).block(); // Mono<Customer>.block()->Customer
        model.addAttribute("customer", customer);
        return "customerDetail";
    }

    @GetMapping("/web/createcustomer") // Enter a new Customer
    public String createCustomer(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "addCustomerForm";
    }

    @PostMapping("/web/addCustomer") // save the inputed new customer
    public String addCustomer(@ModelAttribute Customer customerRequest,
                            Model model) {
        System.out.println("add a new customer");
        Mono<Customer> monoCustomer = customerService.createCustomer(customerRequest);
        model.addAttribute("customer", monoCustomer.block());
        return "redirect:/web/customers";
    }

    @GetMapping("/web/editcustomer/{id}") // editing a specific customer by id
    public String editCustomer(@PathVariable Long id, Model model) {
        Mono<Customer> monoCustomer = customerService.getCustomerById(id);
        Customer customerRequest = monoCustomer.blockOptional().get();
        model.addAttribute("customer", customerRequest);
        return "editCustomerForm";
    }

    @PostMapping("/web/updatecustomer/{id}") // Edit, save the edited customer
    public String updateCustomer(@PathVariable Long id,
                               @ModelAttribute Customer customerRequest, Model model) {

        Mono<Customer> monoCustomer = customerService.updateCustomer(id, customerRequest);
        model.addAttribute("customer", monoCustomer.block());
        return "redirect:/web/customers";
    }

    @GetMapping("/web/deletecustomer/{id}") // delete
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id).block();
        return "redirect:/web/customers";
    }
}