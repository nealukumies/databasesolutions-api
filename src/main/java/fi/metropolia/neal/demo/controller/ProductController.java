package fi.metropolia.neal.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import fi.metropolia.neal.demo.entity.Product;
import fi.metropolia.neal.demo.service.ProductService;
import fi.metropolia.neal.demo.dto.ProductDTO;


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

    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return toDTO(productService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable int id, @RequestBody ProductDTO dto) {
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
                .stream().map(this::toDTO).toList();
    }

    private ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStockQuantity(),
                p.getCategoryId(),
                p.getStartDate(),
                p.getEndDate(),
                p.getSuppliers().stream().map(s -> s.getId()).toList()
        );
    }
}
