package fi.metropolia.neal.demo.controller;

import fi.metropolia.neal.demo.dto.CustomerDTO;
import fi.metropolia.neal.demo.dto.OrderDTO;
import fi.metropolia.neal.demo.dto.PaymentDetailDTO;
import fi.metropolia.neal.demo.dto.CompanyCustomerDTO;
import fi.metropolia.neal.demo.dto.CustomerAddressDTO;
import fi.metropolia.neal.demo.service.CustomerAddressService;
import fi.metropolia.neal.demo.service.CustomerService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerAddressService addressService;

    public CustomerController(CustomerService customerService, CustomerAddressService addressService) {
        this.customerService = customerService;
        this.addressService = addressService;
    }

    // --- CUSTOMER ---
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CustomerDTO createCustomer(@RequestBody CustomerDTO dto) {
        return customerService.createCustomer(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer id,
                                                      @RequestBody CustomerDTO dto) {
        return customerService.updateCustomer(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomer(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    // --- COMPANY CUSTOMER ---
    @GetMapping("/company/{id}")
    public ResponseEntity<CompanyCustomerDTO> getCompanyCustomer(@PathVariable Integer id) {
        return customerService.getCompanyCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/company")
    public CompanyCustomerDTO createCompanyCustomer(@RequestBody CompanyCustomerDTO dto) {
        return customerService.createCompanyCustomer(dto);
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<CompanyCustomerDTO> updateCompanyCustomer(@PathVariable Integer id,
                                                                    @RequestBody CompanyCustomerDTO dto) {
        return customerService.updateCompanyCustomer(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompanyCustomer(@PathVariable Integer id) {
        return customerService.deleteCompanyCustomer(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<CustomerAddressDTO> getCustomerAddress(@PathVariable Integer id) {
        return addressService.getAddressByCustomerId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/address")
    public CustomerAddressDTO createCustomerAddress(@PathVariable Integer id,
                                                    @RequestBody CustomerAddressDTO dto) {
        return addressService.createAddress(id, dto);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<CustomerAddressDTO> updateCustomerAddress(@PathVariable Integer id,
                                                                    @RequestBody CustomerAddressDTO dto) {
        return addressService.updateAddress(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getOrdersForCustomer(id));
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<List<PaymentDetailDTO>> getCustomerPayments(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.getPaymentsForCustomer(id));
    }   

}