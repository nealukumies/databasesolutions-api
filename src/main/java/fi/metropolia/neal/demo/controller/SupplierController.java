package fi.metropolia.neal.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.metropolia.neal.demo.repository.SupplierRepo;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import fi.metropolia.neal.demo.entity.Product;
import fi.metropolia.neal.demo.entity.Supplier;
import fi.metropolia.neal.demo.repository.ProductRepo;



@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierRepo supplierRepository;
    private final ProductRepo productRepo;
    public SupplierController(SupplierRepo supplierRepository, ProductRepo productRepo) {
        this.supplierRepository = supplierRepository;
        this.productRepo = productRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Integer id) {
        return supplierRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        List<Product> managedProducts = new ArrayList<>();

        if (supplier.getProducts() != null) {
            for (Product p : supplier.getProducts()) {
                Product managedProduct = productRepo.findById(p.getId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + p.getId()));
                managedProducts.add(managedProduct);
            }
        }
        supplier.getProducts().clear();
        managedProducts.forEach(managedProduct -> supplier.addProduct(managedProduct));

        return supplierRepository.save(supplier);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Integer id,
                                                @RequestBody Supplier updatedSupplier) {
        return supplierRepository.findById(id)
                .map(existingSupplier -> {
                    existingSupplier.setName(updatedSupplier.getName());
                    existingSupplier.setContactName(updatedSupplier.getContactName());
                    existingSupplier.setPhone(updatedSupplier.getPhone());
                    existingSupplier.setEmail(updatedSupplier.getEmail());

                    List<Product> managedProducts = new ArrayList<>();
                    if (updatedSupplier.getProducts() != null) {
                        for (Product p : updatedSupplier.getProducts()) {
                            Product managedProduct = productRepo.findById(p.getId())
                                    .orElseThrow(() -> new RuntimeException("Product not found: " + p.getId()));
                            managedProducts.add(managedProduct);
                        }
                    }

                    existingSupplier.getProducts().removeIf(p -> {
                        if (!managedProducts.contains(p)) {
                            p.getSuppliers().remove(existingSupplier);
                            return true;
                        }
                        return false;
                    });

                    for (Product p : managedProducts) {
                        if (!existingSupplier.getProducts().contains(p)) {
                            existingSupplier.addProduct(p);
                        }
                    }

                    Supplier savedSupplier = supplierRepository.save(existingSupplier);
                    return ResponseEntity.ok(savedSupplier);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
