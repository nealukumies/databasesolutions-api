package fi.metropolia.neal.demo.dto;
import java.math.BigDecimal;

public record OrderItemDTO(
    Integer orderId,
    Integer productId,
    int quantity,
    BigDecimal unitPrice
) {}
