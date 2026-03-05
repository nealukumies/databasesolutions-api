package fi.metropolia.neal.demo.dto;

import java.time.LocalDateTime;

public record PaymentDetailDTO(
        Integer id,
        Integer orderId,
        String cardNumber,
        String paymentStatus,
        LocalDateTime paymentDate
) {}