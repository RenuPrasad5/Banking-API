package com.example.Banking.Controller;

import com.example.Banking.dto.ApiResponse;
import com.example.Banking.model.Customer;
import com.example.Banking.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCustomer(@RequestParam String name,
                                                      @RequestParam String email) {
        Customer c = customerService.createCustomer(name, email);
        return ResponseEntity.ok(new ApiResponse("Customer created", c));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listCustomers() {
        List<Customer> customers = customerService.listCustomers();
        return ResponseEntity.ok(new ApiResponse("Customers list", customers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCustomer(@PathVariable Long id) {
        Customer c = customerService.getCustomer(id);
        return ResponseEntity.ok(new ApiResponse("Customer fetched", c));
    }
}
