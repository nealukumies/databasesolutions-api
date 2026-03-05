package fi.metropolia.neal.demo.service;

import fi.metropolia.neal.demo.dto.PaymentDetailDTO;
import fi.metropolia.neal.demo.entity.Order;
import fi.metropolia.neal.demo.entity.PaymentDetail;
import fi.metropolia.neal.demo.repository.OrderRepo;
import fi.metropolia.neal.demo.repository.PaymentDetailRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PaymentDetailService {

    private final PaymentDetailRepo paymentRepo;
    private final OrderRepo orderRepo;

    public PaymentDetailService(PaymentDetailRepo paymentRepo, OrderRepo orderRepo) {
        this.paymentRepo = paymentRepo;
        this.orderRepo = orderRepo;
    }

    // Create or update
    public PaymentDetailDTO save(PaymentDetailDTO dto) {
        PaymentDetail payment = new PaymentDetail();

        if (dto.id() != null) {
            payment = paymentRepo.findById(dto.id())
                    .orElse(new PaymentDetail());
        }

        // Link order if provided
        if (dto.orderId() != null) {
            Order order = orderRepo.findById(dto.orderId())
                    .orElseThrow(() -> new RuntimeException("Order not found: " + dto.orderId()));
            payment.setOrder(order);
        }

        payment.setCardNumber(dto.cardNumber());
        payment.setPaymentStatus(dto.paymentStatus());
        payment.setPaymentDate(dto.paymentDate());

        PaymentDetail saved = paymentRepo.save(payment);
        return toDTO(saved);
    }

    // Get by id
    public Optional<PaymentDetailDTO> getById(int id) {
        return paymentRepo.findById(id).map(this::toDTO);
    }

    // Mapper
    private PaymentDetailDTO toDTO(PaymentDetail payment) {
        return new PaymentDetailDTO(
                payment.getId(),
                payment.getOrder() != null ? payment.getOrder().getId() : null,
                payment.getCardNumber(),
                payment.getPaymentStatus(),
                payment.getPaymentDate()
        );
    }
}