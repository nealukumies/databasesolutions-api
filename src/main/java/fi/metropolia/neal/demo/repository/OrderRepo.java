package fi.metropolia.neal.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fi.metropolia.neal.demo.entity.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer>{
    List<Order> findByCustomerId(Integer customerId);
}