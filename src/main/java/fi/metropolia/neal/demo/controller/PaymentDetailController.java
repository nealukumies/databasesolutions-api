package fi.metropolia.neal.demo.controller;

import fi.metropolia.neal.demo.dto.PaymentDetailDTO;
import fi.metropolia.neal.demo.service.PaymentDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentDetailController {

    private final PaymentDetailService service;

    public PaymentDetailController(PaymentDetailService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentDetailDTO> createPayment(@RequestBody PaymentDetailDTO dto) {
        PaymentDetailDTO saved = service.save(dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDetailDTO> updatePayment(@PathVariable int id,
                                                          @RequestBody PaymentDetailDTO dto) {
        PaymentDetailDTO updated = new PaymentDetailDTO(
                id,
                dto.orderId(),
                dto.cardNumber(),
                dto.paymentStatus(),
                dto.paymentDate()
        );
        PaymentDetailDTO saved = service.save(updated);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetailDTO> getPayment(@PathVariable int id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}