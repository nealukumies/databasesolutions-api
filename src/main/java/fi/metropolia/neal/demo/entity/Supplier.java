package fi.metropolia.neal.demo.entity;

import java.util.ArrayList;
import java.util.HashSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GenerationType;

@Entity
@Table(name = "suppliers")
public class Supplier {
    public Supplier() {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "contact_name")
    private String contactName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @ManyToMany(mappedBy = "suppliers")
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactName() {
        return contactName;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public Set<Product> getProducts() {
        return products;
    }
    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    public void addProduct(Product product) {
        this.products.add(product);
        product.getSuppliers().add(this);
    }
    public void removeProduct(Product product) {
        this.products.remove(product);
        product.getSuppliers().remove(this);
    }
}
