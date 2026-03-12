package fi.metropolia.neal.demo.controller;

import fi.metropolia.neal.demo.dto.ProductDTO;
import fi.metropolia.neal.demo.entity.Product;
import fi.metropolia.neal.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable int id) {
        return productService.getProductById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getProductsByCategory(@PathVariable int categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO dto) {
        Product saved = productService.save(dto);
        return toDTO(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id,
                                                    @RequestBody ProductDTO dto) {
        return productService.update(id, dto)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/update-price")
    public void updatePrice(@RequestBody Map<String, Double> request) {
        productService.updatePriceByPercentage(request.get("percentage"));
    }

    @PostMapping("/search")
    public List<ProductDTO> searchByPrice(@RequestBody Map<String, Double> range) {
        return productService.findByPriceRange(range.get("minPrice"), range.get("maxPrice"))
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private ProductDTO toDTO(Product p) {
        Integer categoryId = p.getCategory() != null ? p.getCategory().getId() : null;
        List<Integer> supplierIds = p.getSuppliers().stream()
                .map(s -> s.getId())
                .toList();

        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStockQuantity(),
                categoryId,
                p.getStartDate(),
                p.getEndDate(),
                supplierIds
        );
    }
}