package fi.metropolia.neal.demo.service;

import org.springframework.stereotype.Service;
import fi.metropolia.neal.demo.repository.OrderRepo;
import fi.metropolia.neal.demo.repository.CustomerRepo;
import fi.metropolia.neal.demo.repository.PaymentDetailRepo;
import fi.metropolia.neal.demo.entity.Order;
import fi.metropolia.neal.demo.entity.Customer;
import fi.metropolia.neal.demo.entity.PaymentDetail;
import fi.metropolia.neal.demo.dto.OrderDTO;
import fi.metropolia.neal.demo.dto.OrderItemDTO;

import java.util.Optional;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final PaymentDetailRepo paymentRepo;

    public OrderService(OrderRepo orderRepo, CustomerRepo customerRepo, PaymentDetailRepo paymentRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.paymentRepo = paymentRepo;
    }

    public Optional<OrderDTO> getOrderById(int id) {
        return orderRepo.findById(id).map(this::toDTO);
    }

    public OrderDTO createOrder(OrderDTO dto) {
        Order order = new Order();
        fromDTO(dto, order);
        Order saved = orderRepo.save(order);
        return toDTO(saved);
    }

    public Optional<OrderDTO> updateOrder(int id, OrderDTO dto) {
        return orderRepo.findById(id).map(order -> {
            fromDTO(dto, order);
            Order saved = orderRepo.save(order);
            return toDTO(saved);
        });
    }

    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> items = order.getOrderItems().stream()
            .map(item -> new OrderItemDTO(
                item.getOrder().getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getUnitPrice()
            ))
            .toList();

        Integer paymentId = order.getPaymentDetail() != null ? order.getPaymentDetail().getId() : null;
        Integer customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;

        return new OrderDTO(
            order.getId(),
            customerId,
            order.getOrderDate(),
            order.getDeliveryDate(),
            order.getShippingAddressId(),
            order.getStatus(),
            paymentId,
            items
        );
}

    private void fromDTO(OrderDTO dto, Order order) {
        if (dto.customerId() != null) {
            Customer customer = customerRepo.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + dto.customerId()));
            order.setCustomer(customer);
        }

        order.setOrderDate(dto.orderDate());
        order.setDeliveryDate(dto.deliveryDate());
        order.setShippingAddressId(dto.shippingAddressId());
        order.setStatus(dto.status());

        if (dto.paymentDetailId() != null) {
            PaymentDetail payment = paymentRepo.findById(dto.paymentDetailId())
                .orElseThrow(() -> new RuntimeException("PaymentDetail not found: " + dto.paymentDetailId()));
            order.setPaymentDetail(payment);
        }
    }

    public List<OrderDTO> getOrdersByCustomer(Customer customer) {
    customerRepo.findById(customer.getId())
        .orElseThrow(() -> new RuntimeException("Customer not found"));

    return orderRepo.findByCustomerId(customer.getId())
            .stream()
            .map(this::toDTO)
            .toList();
}
}