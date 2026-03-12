package fi.metropolia.neal.demo.service;

import fi.metropolia.neal.demo.dto.CustomerAddressDTO;
import fi.metropolia.neal.demo.entity.Customer;
import fi.metropolia.neal.demo.entity.CustomerAddress;
import fi.metropolia.neal.demo.repository.CustomerRepo;
import fi.metropolia.neal.demo.repository.CustomerAddressRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepo addressRepo;
    private final CustomerRepo customerRepo;

    public CustomerAddressService(CustomerAddressRepo addressRepo,
                                  CustomerRepo customerRepo) {
        this.addressRepo = addressRepo;
        this.customerRepo = customerRepo;
    }

    public Optional<CustomerAddressDTO> getAddressByCustomerId(Integer customerId) {
        return addressRepo.findByCustomerId(customerId)
                .map(this::toDTO);
    }

    public CustomerAddressDTO createAddress(Integer customerId, CustomerAddressDTO dto) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
                
        if (addressRepo.findByCustomerId(customerId).isPresent()) {
            throw new RuntimeException("Address already exists for this customer");
        }

        CustomerAddress address = new CustomerAddress();
        address.setCustomer(customer);
        address.setStreetAddress(dto.streetAddress());
        address.setPostalCode(dto.postalCode());
        address.setCity(dto.city());
        address.setCountry(dto.country());

        return toDTO(addressRepo.save(address));
    }

    public Optional<CustomerAddressDTO> updateAddress(Integer customerId, CustomerAddressDTO dto) {
        return addressRepo.findByCustomerId(customerId)
                .map(address -> {
                    address.setStreetAddress(dto.streetAddress());
                    address.setPostalCode(dto.postalCode());
                    address.setCity(dto.city());
                    address.setCountry(dto.country());
                    return toDTO(addressRepo.save(address));
                });
    }

    private CustomerAddressDTO toDTO(CustomerAddress address) {
        return new CustomerAddressDTO(
                address.getId(),
                address.getStreetAddress(),
                address.getPostalCode(),
                address.getCity(),
                address.getCountry()
        );
    }
}