package fi.metropolia.neal.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import fi.metropolia.neal.demo.repository.CustomerRepo;
import fi.metropolia.neal.demo.entity.CompanyCustomer;
import fi.metropolia.neal.demo.entity.Customer;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepo customerRepo;

    public CustomerController(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Customer customer = customerRepo.findById(id).orElse(null);
        if (customer != null) {
            System.out.println("Customer:" + customer.getFirstName() + " " + customer.getLastName());
        }
        return customerRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepo.save(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Integer id,
            @RequestBody Customer updatedCustomer) {

        return customerRepo.findById(id)
                .map(customer -> {
                    customer.setFirstName(updatedCustomer.getFirstName());
                    customer.setLastName(updatedCustomer.getLastName());
                    customer.setEmail(updatedCustomer.getEmail());
                    customer.setPhone(updatedCustomer.getPhone());

                    Customer saved = customerRepo.save(customer);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<CompanyCustomer> getCompanyCustomerById(@PathVariable Integer id) {
        return customerRepo.findById(id)
                .filter(c -> c instanceof CompanyCustomer)
                .map(c -> (CompanyCustomer) c)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/company")
    public CompanyCustomer createCompanyCustomer(@RequestBody CompanyCustomer companyCustomer) {
        return customerRepo.save(companyCustomer);
}

    @PutMapping("/company/{id}")
    public ResponseEntity<CompanyCustomer> updateCompanyCustomer(
            @PathVariable Integer id,
            @RequestBody CompanyCustomer updatedCustomer) {

        return customerRepo.findById(id)
                .filter(c -> c instanceof CompanyCustomer)
                .map(c -> (CompanyCustomer) c)
                .map(existing -> {
                    existing.setCompanyName(updatedCustomer.getCompanyName());
                    existing.setVatNumber(updatedCustomer.getVatNumber());
                    existing.setFirstName(updatedCustomer.getFirstName());
                    existing.setLastName(updatedCustomer.getLastName());
                    existing.setEmail(updatedCustomer.getEmail());
                    existing.setPhone(updatedCustomer.getPhone());

                    CompanyCustomer saved = customerRepo.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompanyCustomer(@PathVariable Integer id) {
        return customerRepo.findById(id)
                .filter(c -> c instanceof CompanyCustomer)
                .map(c -> {
                    customerRepo.delete(c);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
