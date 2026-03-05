package fi.metropolia.neal.demo.dto;

import java.time.LocalDateTime;

public record OrderDTO(
    int id,
    Integer customerId,
    LocalDateTime orderDate,
    LocalDateTime deliveryDate,
    int shippingAddressId,
    String status,
    Integer paymentDetailId
) {}