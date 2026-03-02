package fi.metropolia.neal.demo.entity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "orders")
public class Order {
    public Order() {
    }

    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
    @Column(name = "shipping_address_id")
    private int shippingAddressId;
    @Column(name = "status")
    private String status;

    // Getters and setters
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
    public void setShippingAddressId(int shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }
    public int getShippingAddressId() {
        return shippingAddressId;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
