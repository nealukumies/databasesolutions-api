package fi.metropolia.neal.demo.repository;
import fi.metropolia.neal.demo.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface CustomerAddressRepo extends JpaRepository<CustomerAddress, Integer> {
    Optional<CustomerAddress> findByCustomerId(Integer customerId);

}