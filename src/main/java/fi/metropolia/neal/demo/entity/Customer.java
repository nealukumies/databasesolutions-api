package fi.metropolia.neal.demo.entity;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "customers")
public class Customer {
    public Customer() {
    }

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "first_name ")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();
    
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    public List<Order> getOrders() {
        return orders;
    }
}


