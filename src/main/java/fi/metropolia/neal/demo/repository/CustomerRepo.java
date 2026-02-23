package fi.metropolia.neal.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import fi.metropolia.neal.demo.entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    
}
