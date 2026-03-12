package fi.metropolia.neal.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
    int id,
    Integer customerId,
    LocalDateTime orderDate,
    LocalDateTime deliveryDate,
    int shippingAddressId,
    String status,
    Integer paymentDetailId,
    List<OrderItemDTO> orderItems
) {}