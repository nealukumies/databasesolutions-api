package fi.metropolia.neal.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    public Product() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "stock_quantity")
    private int stockQuantity;
    @Column(name ="category_id")
    private int categoryId;
    @Column(name ="startdate")
    private LocalDate startDate;
    @Column(name ="enddate")
    private LocalDate endDate;
    @ManyToMany
    
    @JoinTable(
        name = "supplier_product",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    private Set<Supplier> suppliers = new HashSet<>();

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
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public int getStockQuantity() {
        return stockQuantity;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public Set<Supplier> getSuppliers() {
        return suppliers;
    }
    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
    public void addSupplier(Supplier supplier) {
    this.suppliers.add(supplier);
    supplier.getProducts().add(this);
    }
    public void removeSupplier(Supplier supplier) {
        this.suppliers.remove(supplier);
        supplier.getProducts().remove(this);
    }
}
