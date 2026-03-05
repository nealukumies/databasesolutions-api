package fi.metropolia.neal.demo.service;

import fi.metropolia.neal.demo.dto.ProductDTO;
import fi.metropolia.neal.demo.entity.Product;
import fi.metropolia.neal.demo.entity.Supplier;
import fi.metropolia.neal.demo.repository.ProductCriteriaRepository;
import fi.metropolia.neal.demo.repository.ProductRepo;
import fi.metropolia.neal.demo.repository.SupplierRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepo productRepo;
    private final SupplierRepo supplierRepo;
    private final ProductCriteriaRepository criteriaRepo;

    public ProductService(ProductRepo productRepo, SupplierRepo supplierRepo, ProductCriteriaRepository criteriaRepo) {
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
        this.criteriaRepo = criteriaRepo;
    }

    public Optional<Product> getProductById(int id) {
        return productRepo.findById(id);
    }

    public Product save(ProductDTO dto) {
        Product product = dto.id() != null
                ? productRepo.findById(dto.id()).orElse(new Product())
                : new Product();

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setCategoryId(dto.categoryId());
        product.setStartDate(dto.startDate());
        product.setEndDate(dto.endDate());

        if (dto.supplierIds() != null) {
            product.getSuppliers().clear();
            dto.supplierIds().forEach(sid -> {
                Supplier s = supplierRepo.findById(sid)
                        .orElseThrow(() -> new RuntimeException("Supplier not found: " + sid));
                product.addSupplier(s);
            });
        }

        return productRepo.save(product);
    }

    public Optional<Product> update(int id, ProductDTO dto) {
        return productRepo.findById(id).map(product -> {
            product.setName(dto.name());
            product.setDescription(dto.description());
            product.setPrice(dto.price());
            product.setStockQuantity(dto.stockQuantity());
            product.setCategoryId(dto.categoryId());
            product.setStartDate(dto.startDate());
            product.setEndDate(dto.endDate());

            if (dto.supplierIds() != null) {
                List<Supplier> managedSuppliers = dto.supplierIds().stream()
                        .map(sid -> supplierRepo.findById(sid)
                                .orElseThrow(() -> new RuntimeException("Supplier not found: " + sid)))
                        .collect(Collectors.toList());

                product.getSuppliers().removeIf(s -> {
                    if (!managedSuppliers.contains(s)) {
                        s.getProducts().remove(product);
                        return true;
                    }
                    return false;
                });

                managedSuppliers.forEach(product::addSupplier);
            }

            return productRepo.save(product);
        });
    }

    public void updatePriceByPercentage(double percentage) {
        productRepo.updatePriceByPercentage(percentage);
    }

    public List<Product> findByPriceRange(double min, double max) {
        return criteriaRepo.findProductsByPriceRange(min, max);
    }
}