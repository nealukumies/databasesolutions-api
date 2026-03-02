package fi.metropolia.neal.demo.controller;

import fi.metropolia.neal.demo.entity.Order;
import fi.metropolia.neal.demo.entity.PaymentDetail;
import fi.metropolia.neal.demo.repository.OrderRepo;
import fi.metropolia.neal.demo.repository.PaymentDetailRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentDetailController {
    private final PaymentDetailRepo paymentRepo;
    private final OrderRepo orderRepo;

    public PaymentDetailController(PaymentDetailRepo paymentRepo, OrderRepo orderRepo) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
    }

    @PostMapping
    public ResponseEntity<PaymentDetail> createPayment(@RequestBody PaymentDetail payment) {
        if (payment.getOrder() != null) {
            Order order = orderRepo.findById(payment.getOrder().getId())
                    .orElseThrow(() -> new RuntimeException("Order not found: " + payment.getOrder().getId()));
            payment.setOrder(order);
        }
        PaymentDetail saved = paymentRepo.save(payment);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDetail> updatePayment(@PathVariable int id,
                                                       @RequestBody PaymentDetail updated) {
        return paymentRepo.findById(id)
                .map(existing -> {
                    existing.setCardNumber(updated.getCardNumber());
                    existing.setPaymentStatus(updated.getPaymentStatus());
                    existing.setPaymentDate(updated.getPaymentDate());
                    PaymentDetail saved = paymentRepo.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetail> getPayment(@PathVariable int id) {
        return paymentRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}