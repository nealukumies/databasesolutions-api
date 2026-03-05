package fi.metropolia.neal.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.metropolia.neal.demo.entity.Product;
import fi.metropolia.neal.demo.entity.Supplier;
import fi.metropolia.neal.demo.repository.ProductRepo;
import fi.metropolia.neal.demo.repository.SupplierRepo;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepo productRepo;
    private final SupplierRepo supplierRepo;

    public ProductController(ProductRepo productRepo, SupplierRepo supplierRepo) {
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        if (product.getSuppliers() != null) {
            List<Supplier> managedSuppliers = new ArrayList<>();
            for (Supplier s : product.getSuppliers()) {
                Supplier managedSupplier = supplierRepo.findById(s.getId())
                        .orElseThrow(() -> new RuntimeException("Supplier not found: " + s.getId()));
                managedSuppliers.add(managedSupplier);
            }
            product.getSuppliers().clear();
            managedSuppliers.forEach(product::addSupplier);
        }

        return productRepo.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                @RequestBody Product updatedProduct) {
        return productRepo.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
                    existingProduct.setCategoryId(updatedProduct.getCategoryId());
                    existingProduct.setStartDate(updatedProduct.getStartDate());
                    existingProduct.setEndDate(updatedProduct.getEndDate());

                    if (updatedProduct.getSuppliers() != null) {
                        List<Supplier> managedSuppliers = new ArrayList<>();
                        for (Supplier s : updatedProduct.getSuppliers()) {
                            Supplier managedSupplier = supplierRepo.findById(s.getId())
                                    .orElseThrow(() -> new RuntimeException("Supplier not found: " + s.getId()));
                            managedSuppliers.add(managedSupplier);
                        }

                        existingProduct.getSuppliers().removeIf(s -> {
                            if (!managedSuppliers.contains(s)) {
                                s.getProducts().remove(existingProduct);
                                return true;
                            }
                            return false;
                        });

                        for (Supplier s : managedSuppliers) {
                            if (!existingProduct.getSuppliers().contains(s)) {
                                existingProduct.addSupplier(s);
                            }
                        }
                    }

                    Product savedProduct = productRepo.save(existingProduct);
                    return ResponseEntity.ok(savedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/update-price")
    public ResponseEntity<Void> updatePriceByPercentage(@RequestBody Map<String, Double> request) {
        productRepo.updatePriceByPercentage(request.get("percentage"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<Product>> getProductsByPriceRange(@RequestBody Map<String, Double> range) {

        double minPrice = range.get("minPrice");
        double maxPrice = range.get("maxPrice");

        List<Product> products = productRepo.findProductsByPriceRange(minPrice, maxPrice);

        return ResponseEntity.ok(products);
    }
}