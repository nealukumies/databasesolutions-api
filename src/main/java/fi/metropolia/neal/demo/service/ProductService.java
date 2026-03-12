package fi.metropolia.neal.demo.service;

import fi.metropolia.neal.demo.dto.ProductDTO;
import fi.metropolia.neal.demo.entity.Product;
import fi.metropolia.neal.demo.entity.Supplier;
import fi.metropolia.neal.demo.entity.ProductCategory;
import fi.metropolia.neal.demo.repository.ProductRepo;
import fi.metropolia.neal.demo.repository.SupplierRepo;
import fi.metropolia.neal.demo.repository.ProductCategoryRepo;
import fi.metropolia.neal.demo.repository.ProductCriteriaRepository;
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
    private final ProductCategoryRepo categoryRepo;
    private final ProductCriteriaRepository criteriaRepo;

    public ProductService(ProductRepo productRepo,
                          SupplierRepo supplierRepo,
                          ProductCategoryRepo categoryRepo,
                          ProductCriteriaRepository criteriaRepo) {
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
        this.categoryRepo = categoryRepo;
        this.criteriaRepo = criteriaRepo;
    }

    public Optional<Product> getProductById(int id) {
        return productRepo.findById(id);
    }

    public Product save(ProductDTO dto) {
        Product product = dto.id() != null
                ? productRepo.findById(dto.id()).orElse(new Product())
                : new Product();

        fromDTO(dto, product);
        return productRepo.save(product);
    }

    public Optional<Product> update(int id, ProductDTO dto) {
        return productRepo.findById(id)
                .map(product -> {
                    fromDTO(dto, product);
                    return productRepo.save(product);
                });
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public List<Product> findByCategoryId(int categoryId) {
        return productRepo.findByCategoryId(categoryId);
    }

    public List<ProductDTO> getProductsByCategory(int categoryId) {
        return findByCategoryId(categoryId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void updatePriceByPercentage(double percentage) {
        productRepo.updatePriceByPercentage(percentage);
    }

    public List<Product> findByPriceRange(double min, double max) {
        return criteriaRepo.findProductsByPriceRange(min, max);
    }

    private ProductDTO toDTO(Product product) {
        Integer categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        List<Integer> supplierIds = product.getSuppliers().stream()
                .map(Supplier::getId)
                .collect(Collectors.toList());

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                categoryId,
                product.getStartDate(),
                product.getEndDate(),
                supplierIds
        );
    }

    private void fromDTO(ProductDTO dto, Product product) {
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setStartDate(dto.startDate());
        product.setEndDate(dto.endDate());

        if (dto.categoryId() != null) {
            ProductCategory category = categoryRepo.findById(dto.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + dto.categoryId()));
            product.setCategory(category);
        }

        if (dto.supplierIds() != null) {
            product.getSuppliers().clear();
            dto.supplierIds().forEach(sid -> {
                Supplier supplier = supplierRepo.findById(sid)
                        .orElseThrow(() -> new RuntimeException("Supplier not found: " + sid));
                product.addSupplier(supplier);
            });
        }
    }
}