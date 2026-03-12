package fi.metropolia.neal.demo.service;

import fi.metropolia.neal.demo.dto.CustomerDTO;
import fi.metropolia.neal.demo.dto.OrderDTO;
import fi.metropolia.neal.demo.dto.PaymentDetailDTO;
import fi.metropolia.neal.demo.dto.CompanyCustomerDTO;
import fi.metropolia.neal.demo.entity.CompanyCustomer;
import fi.metropolia.neal.demo.entity.Customer;
import fi.metropolia.neal.demo.entity.Order;
import fi.metropolia.neal.demo.repository.CustomerRepo;
import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final OrderService orderService;
    private final PaymentDetailService paymentService;

    public CustomerService(CustomerRepo customerRepo,
                           OrderService orderService,
                           PaymentDetailService paymentService) {
        this.customerRepo = customerRepo;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    public Optional<CustomerDTO> getCustomerById(Integer id) {
        return customerRepo.findById(id)
                .map(this::toDTO);
    }

    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        Customer saved = customerRepo.save(customer);
        return toDTO(saved);
    }

    public Optional<CustomerDTO> updateCustomer(Integer id, CustomerDTO dto) {
        return customerRepo.findById(id)
                .map(customer -> {
                    customer.setFirstName(dto.firstName());
                    customer.setLastName(dto.lastName());
                    customer.setEmail(dto.email());
                    customer.setPhone(dto.phone());
                    return toDTO(customerRepo.save(customer));
                });
    }

    public boolean deleteCustomer(Integer id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return true;
        }
        return false;
    }


    public Optional<CompanyCustomerDTO> getCompanyCustomerById(Integer id) {
        return customerRepo.findById(id)
                .filter(c -> c instanceof CompanyCustomer)
                .map(c -> (CompanyCustomer) c)
                .map(this::toCompanyDTO);
    }

    public CompanyCustomerDTO createCompanyCustomer(CompanyCustomerDTO dto) {
        CompanyCustomer cc = new CompanyCustomer();
        cc.setFirstName(dto.firstName());
        cc.setLastName(dto.lastName());
        cc.setEmail(dto.email());
        cc.setPhone(dto.phone());
        cc.setCompanyName(dto.companyName());
        cc.setVatNumber(dto.vatNumber());
        Customer saved = customerRepo.save(cc);
        return toCompanyDTO((CompanyCustomer) saved);
    }

    public Optional<CompanyCustomerDTO> updateCompanyCustomer(Integer id, CompanyCustomerDTO dto) {
        return customerRepo.findById(id)
                .filter(c -> c instanceof CompanyCustomer)
                .map(c -> (CompanyCustomer) c)
                .map(existing -> {
                    existing.setFirstName(dto.firstName());
                    existing.setLastName(dto.lastName());
                    existing.setEmail(dto.email());
                    existing.setPhone(dto.phone());
                    existing.setCompanyName(dto.companyName());
                    existing.setVatNumber(dto.vatNumber());
                    return toCompanyDTO((CompanyCustomer) customerRepo.save(existing));
                });
    }

    public boolean deleteCompanyCustomer(Integer id) {
        return customerRepo.findById(id)
                .filter(c -> c instanceof CompanyCustomer)
                .map(c -> {
                    customerRepo.delete(c);
                    return true;
                })
                .orElse(false);
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhone());
    }

    private CompanyCustomerDTO toCompanyDTO(CompanyCustomer c) {
        return new CompanyCustomerDTO(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhone(),
                c.getCompanyName(),
                c.getVatNumber()
        );
    }

    public List<OrderDTO> getOrdersForCustomer(Integer customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return orderService.getOrdersByCustomer(customer);
    }

    public List<PaymentDetailDTO> getPaymentsForCustomer(Integer customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return paymentService.getPaymentsByCustomer(customer); 
    }

}