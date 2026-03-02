package fi.metropolia.neal.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fi.metropolia.neal.demo.repository.OrderRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import fi.metropolia.neal.demo.entity.Order;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepo orderRepo;

    public OrderController(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        return orderRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
}
