package fi.metropolia.neal.demo.service;

import fi.metropolia.neal.demo.dto.SupplierDTO;
import fi.metropolia.neal.demo.entity.Product;
import fi.metropolia.neal.demo.entity.Supplier;
import fi.metropolia.neal.demo.repository.ProductRepo;
import fi.metropolia.neal.demo.repository.SupplierRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {
    private final SupplierRepo supplierRepo;
    private final ProductRepo productRepo;

    public SupplierService(SupplierRepo supplierRepo, ProductRepo productRepo) {
        this.supplierRepo = supplierRepo;
        this.productRepo = productRepo;
    }

    public Optional<Supplier> getSupplierById(int id) {
        return supplierRepo.findById(id);
    }

    public Supplier save(SupplierDTO dto) {
        Supplier supplier = dto.id() != null
                ? supplierRepo.findById(dto.id()).orElse(new Supplier())
                : new Supplier();

        supplier.setName(dto.name());
        supplier.setContactName(dto.contactName());
        supplier.setPhone(dto.phone());
        supplier.setEmail(dto.email());

        if (dto.productIds() != null) {
            supplier.getProducts().clear();
            dto.productIds().forEach(pid -> {
                Product p = productRepo.findById(pid)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + pid));
                supplier.addProduct(p);
            });
        }

        return supplierRepo.save(supplier);
    }

    public Optional<Supplier> update(int id, SupplierDTO dto) {
        return supplierRepo.findById(id).map(supplier -> {
            supplier.setName(dto.name());
            supplier.setContactName(dto.contactName());
            supplier.setPhone(dto.phone());
            supplier.setEmail(dto.email());

            if (dto.productIds() != null) {
                List<Product> managedProducts = dto.productIds().stream()
                        .map(pid -> productRepo.findById(pid)
                                .orElseThrow(() -> new RuntimeException("Product not found: " + pid)))
                        .collect(Collectors.toList());

                supplier.getProducts().removeIf(p -> {
                    if (!managedProducts.contains(p)) {
                        p.getSuppliers().remove(supplier);
                        return true;
                    }
                    return false;
                });

                managedProducts.forEach(supplier::addProduct);
            }

            return supplierRepo.save(supplier);
        });
    }
}