package fi.metropolia.neal.demo.service;

import org.springframework.stereotype.Service;
import fi.metropolia.neal.demo.repository.OrderRepo;
import fi.metropolia.neal.demo.repository.CustomerRepo;
import fi.metropolia.neal.demo.repository.PaymentDetailRepo;
import fi.metropolia.neal.demo.entity.Order;
import fi.metropolia.neal.demo.entity.Customer;
import fi.metropolia.neal.demo.entity.PaymentDetail;
import fi.metropolia.neal.demo.dto.OrderDTO;

import java.util.Optional;

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

    // Converts Order entity → OrderDTO
    private OrderDTO toDTO(Order order) {
        Integer paymentId = order.getPaymentDetail() != null ? order.getPaymentDetail().getId() : null;
        Integer customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;
        return new OrderDTO(
            order.getId(),
            customerId,
            order.getOrderDate(),
            order.getDeliveryDate(),
            order.getShippingAddressId(),
            order.getStatus(),
            paymentId
        );
    }

    // Converts OrderDTO → Order entity (for create/update)
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
}